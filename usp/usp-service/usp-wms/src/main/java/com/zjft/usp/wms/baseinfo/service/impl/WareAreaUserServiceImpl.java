package com.zjft.usp.wms.baseinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.wms.baseinfo.model.WareArea;
import com.zjft.usp.wms.baseinfo.model.WareAreaUser;
import com.zjft.usp.wms.baseinfo.mapper.WareAreaUserMapper;
import com.zjft.usp.wms.baseinfo.service.WareAreaUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <p>
 * 库房人员表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-07
 */
@Service
public class WareAreaUserServiceImpl extends ServiceImpl<WareAreaUserMapper, WareAreaUser> implements WareAreaUserService {

    @Autowired
    UasFeignService uasFeignService;

    /**
     * 根据区域ID找到用户列表
     *
     * @param wareAreaId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareAreaUser>
     * @datetime 2019/11/26 16:29
     * @version
     * @author dcyu
     */
    @Override
    public List<WareAreaUser> listWareAreaUser(Long wareAreaId) {
        Assert.notNull(wareAreaId, "wareAreaId 不能为空");
        QueryWrapper<WareAreaUser> wrapper = new QueryWrapper<>();
        wrapper.eq("area_id", wareAreaId);
        return this.list(wrapper);
    }

    /**
     * 根据区域ID找到用户列表（已映射中文名）
     *
     * @param wareAreaId
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareAreaUser>
     * @datetime 2019/11/18 16:20
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WareAreaUser> listWareAreaUser(Long wareAreaId, Long corpId) {
        Assert.notNull(wareAreaId, "wareAreaId 不能为空");
        Assert.notNull(corpId, "corpId 不能为空");
        QueryWrapper<WareAreaUser> wrapper = new QueryWrapper<>();
        wrapper.eq("area_id", wareAreaId);
        Map<Long, String> userIdAndNameMap = uasFeignService.mapUserIdAndNameByCorpId(corpId).getData();
        List<WareAreaUser> userList = this.list(wrapper);
        if(userList != null && userList.size() > 0 && userIdAndNameMap != null){
            userList.forEach(wareAreaUser -> {
                wareAreaUser.setUserName(userIdAndNameMap.get(wareAreaUser.getUserId()));
            });
        }
        return userList;
    }

    /**
     * 新增区域用户信息
     *
     * @param wareAreaUsers
     * @return void
     * @datetime 2019/11/18 16:31
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareAreaUser(List<WareAreaUser> wareAreaUsers) {
        Assert.notNull(wareAreaUsers, "wareAreaUsers 不能为 NULL");
        for(WareAreaUser wareAreaUser: wareAreaUsers){
            this.save(wareAreaUser);
        }
    }

    /**
     * 新增区域用户信息
     *
     * @param areaId
     * @param wareAreaUsers
     * @param type
     * @param wareAreaUsers
     * @param type
     * @return void
     * @datetime 2019/11/25 14:30
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareAreaUser(Long areaId, Long[] wareAreaUsers, int type) {
        Assert.notNull(areaId,"areaId 不能为空");
        Assert.notNull(wareAreaUsers,"wareAreaUsers 不能为空");
        WareAreaUser areaUser;
        int currentMaxSortNo = 0;
        Optional<WareAreaUser> optional = this.listWareAreaUser(areaId).stream().max(Comparator.comparing(WareAreaUser :: getSortNo));
        if(optional.isPresent()){
            currentMaxSortNo = optional.get().getSortNo();
        }
        for(Long userId : wareAreaUsers){
            areaUser = new WareAreaUser();
            areaUser.setAreaId(areaId);
            areaUser.setUserId(userId);
            areaUser.setType(type);
            /* 顺序号 生成策略 最大值自增10 */
            currentMaxSortNo += 10;
            areaUser.setSortNo(currentMaxSortNo);
            this.save(areaUser);
        }
    }

    /**
     * 根据区域ID删除用户信息
     *
     * @param wareAreaId
     * @return void
     * @datetime 2019/11/18 16:29
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareAreaUserBy(Long wareAreaId) {
        QueryWrapper<WareAreaUser> wrapper = new QueryWrapper<>();
        wrapper.eq("area_id", wareAreaId);
        this.remove(wrapper);
    }
}
