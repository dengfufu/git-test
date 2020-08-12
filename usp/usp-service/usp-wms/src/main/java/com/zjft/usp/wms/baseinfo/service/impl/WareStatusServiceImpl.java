package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.filter.WareStatusFilter;
import com.zjft.usp.wms.baseinfo.mapper.WareStatusMapper;
import com.zjft.usp.wms.baseinfo.model.WareStatus;
import com.zjft.usp.wms.baseinfo.service.WareStatusService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 物料状态表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareStatusServiceImpl extends ServiceImpl<WareStatusMapper, WareStatus> implements WareStatusService {

    /**
     * 根据corpId获取物料状态编号与名称映射Map
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param corpId
     * @return java.util.Map<java.lang.Integer, java.lang.String>
     */
    @Override
    public Map<Integer, String> mapIdAndName(Long corpId) {
        Map<Integer, String> wareStatusMap = new HashMap<>(16);
        QueryWrapper<WareStatus> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", corpId);
        List<WareStatus> wareStatusList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(wareStatusList)) {
            for (WareStatus wareStatus : wareStatusList) {
                wareStatusMap.put(wareStatus.getId(), wareStatus.getName());
            }
        }
        return wareStatusMap;
    }

    /**
     * 物料状态列表
     *
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareStatus>
     * @datetime 2019/11/26 10:11
     * @version
     * @author dcyu
     */
    @Override
    public List<WareStatus> listWareStatus(Long corpId) {
        Assert.notNull(corpId, "corpId 不能为 Null");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("corp_id", corpId);
        return this.list(wrapper);
    }

    /**
     * 根据条件查找物料状态信息
     *
     * @param wareStatusFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WareStatus>
     * @datetime 2019/11/26 10:11
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<WareStatus> queryWareStatus(WareStatusFilter wareStatusFilter) {
        Assert.notNull(wareStatusFilter, "wareStatusFilter 不能为 Null");
        ListWrapper wrapperList = new ListWrapper();
        if(LongUtil.isZero(wareStatusFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(wareStatusFilter.getName())){
            wrapper.like("name", wareStatusFilter.getName());
        }
        wrapper.eq("corp_id", wareStatusFilter.getCorpId());
        Page page = new Page(wareStatusFilter.getPageNum(), wareStatusFilter.getPageSize());
        IPage iPage = this.page(page, wrapper);
        if(iPage != null){
            wrapperList.setList(iPage.getRecords());
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 新增物料状态
     *
     * @param wareStatus
     * @return void
     * @datetime 2019/11/22 17:29
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareStatus(WareStatus wareStatus) {
        QueryWrapper<WareStatus> wrapper = new QueryWrapper<>();
        // 查找名称是否已存在
        wrapper.eq("name", wareStatus.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "状态名称【%s】已存在", wareStatus.getName());
        // 主键生成策略 最大值 增加10
        Optional<WareStatus> optional = this.list().stream().max(Comparator.comparing(WareStatus::getId));
        int newGenId = 0;
        if(optional.isPresent()){
            newGenId = optional.get().getId() + 10;
        }
        wareStatus.setId(newGenId);
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareStatus.getSortNo())){
            int sortNo = 0;
            optional = this.list().stream().max(Comparator.comparing(WareStatus::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareStatus.setSortNo(sortNo + 10);
        }
        this.save(wareStatus);
    }

    /**
     * 更新物料状态
     *
     * @param wareStatus
     * @return void
     * @datetime 2019/11/25 16:26
     * @version
     * @author dcyu
     */
    @Override
    public void updateWareStatus(WareStatus wareStatus) {
        /* QueryWrapper<WareStatus> wrapper = new QueryWrapper<>();
        // 查找名称是否已存在
        wrapper.eq("name", wareStatus.getName());
        WareStatus isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(wareStatus.getId()), "状态名称【%s】已存在", wareStatus.getName()); */
        this.updateById(wareStatus);
    }

    /**
     * 删除物料状态信息
     *
     * @param wareStatusId
     * @return void
     * @datetime 2019-12-02 10:13
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareStatus(Long wareStatusId) {
        Assert.notNull(wareStatusId, "wareStatusId 不能为 Null");
        this.removeById(wareStatusId);
    }
}
