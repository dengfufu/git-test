package com.zjft.usp.uas.right.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.right.composite.SysRightCompoService;
import com.zjft.usp.uas.right.dto.SysRightDto;
import com.zjft.usp.uas.right.model.SysRight;
import com.zjft.usp.uas.right.model.SysRightExtraCorp;
import com.zjft.usp.uas.right.model.SysRightScopeType;
import com.zjft.usp.uas.right.model.SysRightUrl;
import com.zjft.usp.uas.right.service.SysRightExtraCorpService;
import com.zjft.usp.uas.right.service.SysRightScopeTypeService;
import com.zjft.usp.uas.right.service.SysRightService;
import com.zjft.usp.uas.right.service.SysRightUrlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统权限聚合实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2020/3/11 16:54
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRightCompoServiceImpl implements SysRightCompoService {

    @Autowired
    private SysRightService sysRightService;
    @Autowired
    private SysRightScopeTypeService sysRightScopeTypeService;
    @Autowired
    private SysRightExtraCorpService sysRightExtraCorpService;
    @Autowired
    private SysRightUrlService sysRightUrlService;

    @Resource
    private RedisRepository redisRepository;

    /**
     * 添加系统权限
     *
     * @param sysRightDto
     * @return
     * @author zgpi
     * @date 2020/3/11 16:52
     */
    @Override
    public void addSysRight(SysRightDto sysRightDto) {
        List<Integer> scopeTypeList = sysRightDto.getScopeTypeList();
        List<SysRightExtraCorp> extraCorpList = sysRightDto.getExtraCorpList();
        if ("Y".equalsIgnoreCase(sysRightDto.getHasScope()) && CollectionUtil.isEmpty(scopeTypeList)) {
            throw new AppException("请选择范围类型！");
        }
        if ("Y".equalsIgnoreCase(sysRightDto.getHasExtraCorp()) && CollectionUtil.isEmpty(extraCorpList)) {
            throw new AppException("请选择额外企业！");
        }
        // 权限码不能重复
        if (!StringUtils.isEmpty(sysRightDto.getRightCode())) {
            List<SysRight> sysRightList = sysRightService.list(new QueryWrapper<SysRight>().eq("right_code", sysRightDto.getRightCode()));
            if (sysRightList.size() > 0) {
                throw new AppException("权限码已经存在,请修改！");
            }
        }

        SysRight sysRight = new SysRight();
        BeanUtils.copyProperties(sysRightDto, sysRight);
        sysRightService.addSysRight(sysRight);

        // 设置范围权限类型
        if ("Y".equalsIgnoreCase(sysRightDto.getHasScope()) && CollectionUtil.isNotEmpty(scopeTypeList)) {
            List<SysRightScopeType> sysRightScopeTypeList = new ArrayList<>();
            SysRightScopeType sysRightScopeType;
            for (Integer scopeType : scopeTypeList) {
                sysRightScopeType = new SysRightScopeType();
                sysRightScopeType.setRightId(sysRight.getRightId());
                sysRightScopeType.setScopeType(scopeType);
                sysRightScopeTypeList.add(sysRightScopeType);
            }
            sysRightScopeTypeService.saveBatch(sysRightScopeTypeList);
        }

        // 设置额外企业信息
        if ("Y".equalsIgnoreCase(sysRightDto.getHasExtraCorp()) && CollectionUtil.isNotEmpty(extraCorpList)) {
            List<SysRightExtraCorp> sysRightExtraCorpList = new ArrayList<>();
            for (SysRightExtraCorp extraCorp : extraCorpList) {
                SysRightExtraCorp sysRightExtraCorp = new SysRightExtraCorp();
                sysRightExtraCorp.setRightId(sysRight.getRightId());
                sysRightExtraCorp.setCorpId(extraCorp.getCorpId());
                sysRightExtraCorp.setCommon(extraCorp.getCommon());
                sysRightExtraCorpList.add(sysRightExtraCorp);
            }
            sysRightExtraCorpService.saveBatch(sysRightExtraCorpList);
        }
    }

    /**
     * 修改系统权限
     *
     * @param sysRightDto
     * @return
     * @author zgpi
     * @date 2020/3/11 16:53
     */
    @Override
    public void updateSysRight(SysRightDto sysRightDto) {
        List<Integer> scopeTypeList = sysRightDto.getScopeTypeList();
        List<SysRightExtraCorp> extraCorpList = sysRightDto.getExtraCorpList();
        if ("Y".equalsIgnoreCase(sysRightDto.getHasScope()) && CollectionUtil.isEmpty(scopeTypeList)) {
            throw new AppException("请选择范围类型！");
        }
        if ("Y".equalsIgnoreCase(sysRightDto.getHasExtraCorp()) && CollectionUtil.isEmpty(extraCorpList)) {
            throw new AppException("请选择额外企业！");
        }
        // 权限码不能重复
        if (!StringUtils.isEmpty(sysRightDto.getRightCode())) {
            List<SysRight> sysRightList = sysRightService.list(new QueryWrapper<SysRight>()
                    .eq("right_code", sysRightDto.getRightCode()));
            if (sysRightList.size() > 0) {
                // 排除自身
                if (!(sysRightList.size() == 1 && sysRightList.get(0).getRightId().equals(sysRightDto.getRightId()))) {
                    throw new AppException("权限码已经存在,请修改！");
                }
            }
        }
        SysRight sysRight = new SysRight();
        BeanUtils.copyProperties(sysRightDto, sysRight);
        sysRightService.updateSysRight(sysRight);

        // 修改范围权限类型
        sysRightScopeTypeService.remove(new UpdateWrapper<SysRightScopeType>().eq("right_id", sysRightDto.getRightId()));
        if ("Y".equalsIgnoreCase(sysRightDto.getHasScope()) && CollectionUtil.isNotEmpty(scopeTypeList)) {
            List<SysRightScopeType> sysRightScopeTypeList = new ArrayList<>();
            SysRightScopeType sysRightScopeType;
            for (Integer scopeType : scopeTypeList) {
                sysRightScopeType = new SysRightScopeType();
                sysRightScopeType.setRightId(sysRight.getRightId());
                sysRightScopeType.setScopeType(scopeType);
                sysRightScopeTypeList.add(sysRightScopeType);
            }
            sysRightScopeTypeService.saveBatch(sysRightScopeTypeList);
        }

        // 设置额外企业信息
        sysRightExtraCorpService.remove(new UpdateWrapper<SysRightExtraCorp>().eq("right_id", sysRightDto.getRightId()));
        if ("Y".equalsIgnoreCase(sysRightDto.getHasExtraCorp()) && CollectionUtil.isNotEmpty(extraCorpList)) {
            List<SysRightExtraCorp> sysRightExtraCorpList = new ArrayList<>();
            for (SysRightExtraCorp extraCorp : extraCorpList) {
                SysRightExtraCorp sysRightExtraCorp = new SysRightExtraCorp();
                sysRightExtraCorp.setRightId(sysRight.getRightId());
                sysRightExtraCorp.setCorpId(extraCorp.getCorpId());
                sysRightExtraCorp.setCommon(extraCorp.getCommon());
                sysRightExtraCorpList.add(sysRightExtraCorp);
            }
            sysRightExtraCorpService.saveBatch(sysRightExtraCorpList);
        }
    }

    /**
     * 删除系统权限
     *
     * @param rightId
     * @return
     * @author zgpi, CK
     * @date 2020/3/11 16:53
     */
    @Override
    public void delSysRight(Long rightId) {
        SysRight sysRight = sysRightService.getById(rightId);
        if (sysRight != null) {
            List<SysRight> sysRightList = sysRightService.list();
            List<Long> rightIdList = new ArrayList<>();
            rightIdList.add(sysRight.getRightId());
            this.getChild(sysRight, sysRightList, rightIdList);
            sysRightService.removeByIds(rightIdList);
        }
        // 删除额外企业定义
        sysRightExtraCorpService.remove(new UpdateWrapper<SysRightExtraCorp>()
                .eq("right_id", rightId));
        // 删除范围权限定义
        sysRightScopeTypeService.remove(new UpdateWrapper<SysRightScopeType>()
                .eq("right_id", rightId));
    }

    /**
     * 递归查找子节点编号
     *
     * @param parent      当前菜单id
     * @param rootMenu    要查找的列表
     * @param rightIdList 权限编号列表
     * @return
     */
    private void getChild(SysRight parent,
                          List<SysRight> rootMenu,
                          List<Long> rightIdList) {
        List<SysRight> childList = new ArrayList<>();
        for (SysRight sysRight : rootMenu) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (sysRight.getParentId().equals(parent.getRightId())) {
                childList.add(sysRight);
                rightIdList.add(sysRight.getRightId());
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (SysRight menu : childList) {
            // 递归
            this.getChild(menu, rootMenu, rightIdList);
        }
    }

    /**
     * 初始化系统公共权限
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 15:22
     **/
    @Override
    public void initSysCommonRightRedis() {
        // 若已经初始化过系统公共权限，则不再初始化
        String appRightInitFlag = StrUtil.trimToEmpty((String) redisRepository
                .get(RedisRightConstants.getRightCommonInit()));
        if ("Y".equalsIgnoreCase(appRightInitFlag)) {
            return;
        }
        Map<String, List<Right>> rightMap = this.mapUrlAndRightMap();
        if (CollectionUtil.isEmpty(rightMap)) {
            return;
        }
        Map<String, List<Right>> commonRightMap = new HashMap<>();
        Map<String, List<Right>> commonStarRightMap = new HashMap<>();
        for (String url : rightMap.keySet()) {
            List<Right> sysRightList = rightMap.get(url);
            List<Right> commonRightList = new ArrayList<>();
            List<Right> commonStarRightList = new ArrayList<>();
            for (Right right : sysRightList) {
                if (right.getRightType() == 1) {
                    if (url.contains("*")) {
                        commonStarRightList.add(right);
                    } else {
                        commonRightList.add(right);
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(commonStarRightList)) {
                commonStarRightMap.put(url, commonStarRightList);
            }
            if (CollectionUtil.isNotEmpty(commonRightList)) {
                commonRightMap.put(url, commonRightList);
            }
        }
        if (CollectionUtil.isNotEmpty(commonRightMap)) {
            for (String url : commonRightMap.keySet()) {
                redisRepository.set(RedisRightConstants.getRightUrlCommon(url), commonRightMap.get(url));
            }
        }
        if (CollectionUtil.isNotEmpty(commonStarRightMap)) {
            redisRepository.set(RedisRightConstants.getRightStarCommon(), commonStarRightMap);
        }
        // 设置标记位，已经初始化过系统权限
        redisRepository.set(RedisRightConstants.getRightCommonInit(), "Y");
    }

    /**
     * 将鉴权列表转换为url与权限列表映射
     *
     * @param
     * @return
     * @author zgpi
     * @date 2020/6/8 15:25
     **/
    public Map<String, List<Right>> mapUrlAndRightMap() {
        List<SysRightUrl> sysRightUrlList = sysRightUrlService.listAuthRight();
        Map<String, List<Right>> rightMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(sysRightUrlList)) {
            Right right;
            List<Right> list;
            for (SysRightUrl sysRightUrl : sysRightUrlList) {
                list = new ArrayList<>();
                String url = StrUtil.trimToEmpty(sysRightUrl.getUri());
                if (StrUtil.isBlank(url)) {
                    continue;
                }
                if (rightMap.containsKey(url)) {
                    list = rightMap.get(url);
                }
                right = new Right();
                BeanUtils.copyProperties(sysRightUrl, right);
                list.add(right);
                rightMap.put(url, list);
            }
        }
        return rightMap;
    }
}
