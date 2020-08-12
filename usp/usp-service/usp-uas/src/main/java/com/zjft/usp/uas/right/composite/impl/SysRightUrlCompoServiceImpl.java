package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.right.composite.SysRightUrlCompoService;
import com.zjft.usp.uas.right.model.SysRightUrl;
import com.zjft.usp.uas.right.service.SysRightUrlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * 系统权限聚合实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/26 15:14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRightUrlCompoServiceImpl implements SysRightUrlCompoService {

    @Autowired
    private SysRightUrlService sysRightUrlService;
    @Resource
    private RedisRepository redisRepository;

    /**
     * 添加系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/12/26 15:29
     **/
    @Override
    public void addSysRightUrl(SysRightUrl sysRightUrl) {
        // 添加系统权限
        sysRightUrlService.addSysRightUrl(sysRightUrl);
        if (sysRightUrl.getRightType() == null || sysRightUrl.getRightType() != 1) {
            return;
        }
        // 更新redis权限
        String url = StrUtil.trimToEmpty(sysRightUrl.getUri());
        if (url.contains("*")) {
            Map<String, List<Right>> rightMap = (Map<String, List<Right>>) redisRepository
                    .get(RedisRightConstants.getRightStarCommon());
            List<Right> rightList = new ArrayList<>();
            if (rightMap.containsKey(url)) {
                rightList = rightMap.get(url);
            }
            Right right = new Right();
            BeanUtils.copyProperties(sysRightUrl, right);
            rightList.add(right);
            rightMap.put(url, rightList);
            redisRepository.set(RedisRightConstants.getRightStarCommon(), rightMap);
        } else {
            List<Right> rightList = (List<Right>) redisRepository
                    .get(RedisRightConstants.getRightUrlCommon(url));
            if (CollectionUtil.isEmpty(rightList)) {
                rightList = new ArrayList<>();
            }
            Right right = new Right();
            BeanUtils.copyProperties(sysRightUrl, right);
            rightList.add(right);
            redisRepository.set(RedisRightConstants.getRightUrlCommon(url), rightList);
        }
    }

    /**
     * 修改系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/12/26 15:57
     **/
    @Override
    public void updateSysRightUrl(SysRightUrl sysRightUrl) {
        SysRightUrl dbSysRightUrl = sysRightUrlService.getById(sysRightUrl.getId());
        if (dbSysRightUrl == null) {
            throw new AppException("该权限已不存在！");
        }
        if (StrUtil.isBlank(sysRightUrl.getPathMethod())) {
            sysRightUrl.setPathMethod("");
        }
        // 修改系统权限
        sysRightUrlService.updateSysRightUrl(sysRightUrl);
        String dbUrl = StrUtil.trimToEmpty(dbSysRightUrl.getUri());
        String url = StrUtil.trimToEmpty(sysRightUrl.getUri());
        // 更新redis权限
        // 判断是否修改了权限类型->（公共改为非公共，非公共改为公共）
        int rightTypeChange = this.findRightTypeChange(dbSysRightUrl.getRightType(),
                sysRightUrl.getRightType());
        // 非公共改为公共，新增公共权限，暂时不处理原来用户中的旧权限，在用户登录时会自动更新
        if (rightTypeChange == 1) {
            this.addSysCommonRight(url);
        }
        // 没有修改权限类型
        else if (rightTypeChange == 0) {
            // 不是公共权限，新增用户权限
            if (dbSysRightUrl.getRightType() != 1) {
                this.addUserRight(url, dbUrl);
            }
            // 是公共权限，则新增公共权限的url
            else {
                List<Right> rightList = (List<Right>) redisRepository
                        .get(RedisRightConstants.getRightUrlCommon(dbUrl));
                redisRepository.set(RedisRightConstants.getRightUrlCommon(url), rightList);
            }
        }
        // 公共改为非公共，不处理
        else if (rightTypeChange == 2) {

        }
        // 上面的操作已经新增了新权限，再删除原来的公共权限
        if (dbSysRightUrl.getRightType() == 1) {
            if (dbUrl.contains("*")) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>) redisRepository
                        .get(RedisRightConstants.getRightStarCommon());
                rightMap.remove(dbUrl);
                redisRepository.set(RedisRightConstants.getRightStarCommon(), rightMap);
            } else {
                redisRepository.del(RedisRightConstants.getRightUrlCommon(dbUrl));
            }
        }
    }

    /**
     * 删除系统权限
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/12/26 17:27
     **/
    @Override
    public void delSysRightUrl(Long id) {
        SysRightUrl dbSysRightUrl = sysRightUrlService.getById(id);
        if (dbSysRightUrl != null) {
            sysRightUrlService.removeById(id);
            String dbUrl = StrUtil.trimToEmpty(dbSysRightUrl.getUri());
            if (dbUrl.contains("*")) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>) redisRepository
                        .get(RedisRightConstants.getRightStarCommon());
                rightMap.remove(dbUrl);
                redisRepository.set(RedisRightConstants.getRightStarCommon(), rightMap);
            } else {
                redisRepository.del(RedisRightConstants.getRightUrlCommon(dbUrl));
            }
            Set<String> userKeySet = this.listUserRedisKey(dbUrl);
            if (CollectionUtil.isNotEmpty(userKeySet)) {
                for (String userKey : userKeySet) {
                    Map<String, List<Right>> rightMap = (Map<String, List<Right>>) redisRepository
                            .get(userKey);
                    rightMap.remove(dbUrl);
                    redisRepository.set(userKey, rightMap);
                }
            }
        }
    }

    /**
     * 权限类型改变结果
     *
     * @param dbType
     * @param type
     * @return
     * @author zgpi
     * @date 2019/12/26 16:17
     **/
    private int findRightTypeChange(Integer dbType, Integer type) {
        if (dbType == null && type == null) {
            // 未改变
            return 0;
        }
        if (dbType == null && type != null) {
            if (type == 1) {
                // 非公共改为公共
                return 1;
            } else {
                // 未改变
                return 0;
            }
        }
        if (dbType != null && type == null) {
            if (dbType == 1) {
                // 公共改为非公共
                return 2;
            } else {
                // 未改变
                return 0;
            }
        }
        if (dbType != null && type != null && !dbType.equals(type)) {
            if (dbType == 1) {
                // 公共改为非公共
                return 2;
            } else {
                // 非公共改为公共
                return 1;
            }
        }
        return 0;
    }

    /**
     * 根据url获得用户权限key列表
     *
     * @param url
     * @return
     * @author zgpi
     * @date 2019/12/26 17:14
     **/
    private Set<String> listUserRedisKey(String url) {
        if (url.contains("*")) {
            return redisRepository.keys(RedisRightConstants.getUserStarKeyPatten());
        } else {
            return redisRepository.keys(RedisRightConstants.getUserUrlKeyPatten());
        }
    }

    /**
     * 新增系统公共权限
     *
     * @param url
     * @return
     * @author zgpi
     * @date 2019/12/27 11:06
     **/
    private void addSysCommonRight(String url) {
        List<SysRightUrl> sysRightUrlList = sysRightUrlService.listByUrl(url);
        List<Right> rightList = new ArrayList<>();
        Right right;
        for (SysRightUrl entity : sysRightUrlList) {
            right = new Right();
            BeanUtils.copyProperties(entity, right);
            rightList.add(right);
        }
        // 新增新权限
        if (url.contains("*")) {
            Map<String, List<Right>> rightMap = (Map<String, List<Right>>) redisRepository
                    .get(RedisRightConstants.getRightStarCommon());
            rightMap = rightMap == null ? new HashMap<>() : rightMap;
            rightMap.put(url, rightList);
            redisRepository.set(RedisRightConstants.getRightStarCommon(), rightMap);
        } else {
            redisRepository.set(RedisRightConstants.getRightUrlCommon(url), rightList);
        }
    }

    /**
     * 新增用户权限
     *
     * @param url
     * @param dbUrl
     * @return
     * @author zgpi
     * @date 2019/12/27 11:10
     **/
    private void addUserRight(String url, String dbUrl) {
        List<SysRightUrl> sysRightUrlList = sysRightUrlService.listByUrl(url);
        List<Right> rightList = new ArrayList<>();
        Right right;
        for (SysRightUrl entity : sysRightUrlList) {
            right = new Right();
            BeanUtils.copyProperties(entity, right);
            rightList.add(right);
        }
        Set<String> userKeySet = this.listUserRedisKey(url);
        if (CollectionUtil.isNotEmpty(userKeySet)) {
            for (String userKey : userKeySet) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>) redisRepository
                        .get(userKey);
                if (rightMap.containsKey(dbUrl)) {
                    rightMap = rightMap == null ? new HashMap<>() : rightMap;
                    rightMap.remove(dbUrl);
                    rightMap.put(url, rightList);
                    for (String aa : rightMap.keySet()) {
                        if (aa.contains("large")) {
                            System.out.println(aa);
                        }
                    }
                    redisRepository.set(userKey, rightMap);
                }
            }
        }
    }
}
