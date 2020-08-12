package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.filter.WareSupplierFilter;
import com.zjft.usp.wms.baseinfo.model.WareSupplier;
import com.zjft.usp.wms.baseinfo.mapper.WareSupplierMapper;
import com.zjft.usp.wms.baseinfo.service.WareSupplierService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 供应商表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareSupplierServiceImpl extends ServiceImpl<WareSupplierMapper, WareSupplier> implements WareSupplierService {
    /**
     * 物料供应商列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareSupplier>
     * @datetime 2019/11/26 10:14
     * @version
     * @author dcyu
     */
    @Override
    public List<WareSupplier> listWareSupplier() {
        return null;
    }

    /**
     * 根据条件查找物料供应商信息
     *
     * @param wareSupplierFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WareSupplier>
     * @datetime 2019/11/26 10:14
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<WareSupplier> queryWareSupplier(WareSupplierFilter wareSupplierFilter) {
        Assert.notNull(wareSupplierFilter, "wareStatusFilter 不能为 Null");
        ListWrapper wrapperList = new ListWrapper();
        if(LongUtil.isZero(wareSupplierFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper<WareSupplier> wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(wareSupplierFilter.getName())){
            wrapper.like("name", wareSupplierFilter.getName());
        }
        wrapper.eq("corp_id", wareSupplierFilter.getCorpId());
        Page page = new Page(wareSupplierFilter.getPageNum(), wareSupplierFilter.getPageSize());
        IPage<WareSupplier> iPage = this.page(page, wrapper);
        if(iPage != null){
            wrapperList.setList(iPage.getRecords());
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 新增物料供应商信息
     *
     * @param wareSupplier
     * @return void
     * @datetime 2019/11/25 18:53
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareSupplier(WareSupplier wareSupplier) {
        Assert.notNull(wareSupplier, "wareSupplier 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareSupplier.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "供应商名称【%s】已存在", wareSupplier.getName());
        wareSupplier.setId(KeyUtil.getId());
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareSupplier.getSortNo())){
            int sortNo = 0;
            Optional<WareSupplier> optional = this.list().stream().max(Comparator.comparing(WareSupplier::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareSupplier.setSortNo(sortNo + 10);
        }
        this.save(wareSupplier);
    }

    /**
     * 更新物料供应商信息
     *
     * @param wareSupplier
     * @return void
     * @datetime 2019/11/25 18:53
     * @version
     * @author dcyu
     */
    @Override
    public void updateWareSupplier(WareSupplier wareSupplier) {
        Assert.notNull(wareSupplier, "wareSupplier 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareSupplier.getName());
        WareSupplier isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(wareSupplier.getId()), "物料供应商名称【%s】已存在", wareSupplier.getName());
        this.updateById(wareSupplier);
    }

    /**
     * 删除物料供应商信息
     *
     * @param wareSupplierId
     * @return void
     * @datetime 2019/11/25 18:53
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareSupplier(Long wareSupplierId) {
        Assert.notNull(wareSupplierId, "wareSupplierId 不能为空");
        this.removeById(wareSupplierId);
    }
}
