package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.wms.baseinfo.model.WareDepotUser;
import com.zjft.usp.wms.baseinfo.mapper.WareDepotUserMapper;
import com.zjft.usp.wms.baseinfo.service.WareDepotUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
 * @since 2019-11-06
 */
@Service
public class WareDepotUserServiceImpl extends ServiceImpl<WareDepotUserMapper, WareDepotUser> implements WareDepotUserService {

    @Autowired
    UasFeignService uasFeignService;

    /**
     * 物料库房人员信息列表
     *
     * @param wareDepotId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareDepotUser>
     * @datetime 2019/11/19 10:35
     * @version
     * @author dcyu
     */
    @Override
    public List<WareDepotUser> listWareDepotUser(Long wareDepotId) {
        Assert.notNull(wareDepotId, "wareDepotId 不能为 NULL");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("depot_id", wareDepotId);
        return this.list(wrapper);
    }

    /**
     * 物料库房人员信息列表（已映射中文名）
     *
     * @param wareDepotId
     * @param corpId
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareDepotUser>
     * @datetime 2019/11/26 16:37
     * @version
     * @author dcyu
     */
    @Override
    public List<WareDepotUser> listWareDepotUser(Long wareDepotId, Long corpId) {
        Assert.notNull(wareDepotId, "wareDepotId 不能为 Null");
        Assert.notNull(corpId, "corpId 不能为 Null");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("depot_id", wareDepotId);
        List<WareDepotUser> userList = this.list(wrapper);
        Map<Long, String> userIdAndNameMap = uasFeignService.mapUserIdAndNameByCorpId(corpId).getData();
        if(CollectionUtil.isNotEmpty(userList) && userIdAndNameMap != null){
            userList.forEach(wareDepotUser -> {
                wareDepotUser.setUserName(userIdAndNameMap.get(wareDepotUser.getUserId()));
            });
        }
        return userList;
    }

    /**
     * 新增物料库房人员信息
     *
     * @param wareDepotUserList
     * @return void
     * @datetime 2019/11/19 10:35
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareDepotUser(List<WareDepotUser> wareDepotUserList) {
        Assert.notNull(wareDepotUserList, "wareDepotUserList 不能为 NULL");
        for(WareDepotUser wareDepotUser : wareDepotUserList){
            wareDepotUser.setUserId(KeyUtil.getId());
            this.save(wareDepotUser);
        }
    }

    /**
     * 新增物料库房人员信息
     *
     * @param depotId
     * @param users
     * @param type
     * @return void
     * @datetime 2019/11/26 16:06
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareDepotUser(Long depotId, Long[] users, int type) {
        Assert.notNull(depotId, "wareDepotId 不能为 Null");
        Assert.notNull(users, "wareDepotUsers 不能为 Null");
        WareDepotUser wareDepotUser;
        int currentMaxSortNo = 0;
        Optional<WareDepotUser> optional = this.listWareDepotUser(depotId).stream().max(Comparator.comparing(WareDepotUser::getSortNo));
        if(optional.isPresent()){
            currentMaxSortNo = optional.get().getSortNo();
        }
        for(Long userId : users){
            wareDepotUser = new WareDepotUser();
            wareDepotUser.setDepotId(depotId);
            wareDepotUser.setUserId(userId);
            wareDepotUser.setType(type);
            // 顺序号 生成策略 最大值 +10
            currentMaxSortNo += 10;
            wareDepotUser.setSortNo(currentMaxSortNo);
            this.save(wareDepotUser);
        }
    }

    /**
     * 删除物料库房人员信息
     *
     * @param wareDepotId
     * @return void
     * @datetime 2019/11/19 10:35
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareDepotUser(Long wareDepotId) {
        Assert.notNull(wareDepotId, "wareDepotId 不能为 NULL");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("depot_id", wareDepotId);
        this.remove(wrapper);
    }
}
