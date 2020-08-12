package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.wms.baseinfo.filter.WareSupplierFilter;
import com.zjft.usp.wms.baseinfo.model.WareSupplier;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareSupplierService extends IService<WareSupplier> {

    /**
     * 物料供应商列表
     * @datetime 2019/11/26 10:14
     * @version
     * @author dcyu
     * @param
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareSupplier>
     */
    List<WareSupplier> listWareSupplier();

    /**
     * 根据条件查找物料供应商信息
     * @datetime 2019/11/26 10:14
     * @version
     * @author dcyu
     * @param wareSupplierFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WareSupplier>
     */
    ListWrapper<WareSupplier> queryWareSupplier(WareSupplierFilter wareSupplierFilter);

    /**
     * 新增物料供应商信息
     * @datetime 2019/11/25 18:53
     * @version
     * @author dcyu
     * @param wareSupplier
     * @return void
     */
    void insertWareSupplier(WareSupplier wareSupplier);

    /**
     * 更新物料供应商信息
     * @datetime 2019/11/25 18:53
     * @version
     * @author dcyu
     * @param wareSupplier
     * @return void
     */
    void updateWareSupplier(WareSupplier wareSupplier);

    /**
     * 删除物料供应商信息
     * @datetime 2019/11/25 18:53
     * @version
     * @author dcyu
     * @param wareSupplierId
     * @return void
     */
    void deleteWareSupplier(Long wareSupplierId);

}
