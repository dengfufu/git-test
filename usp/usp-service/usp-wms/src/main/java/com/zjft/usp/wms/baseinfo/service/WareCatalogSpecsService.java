package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.dto.WareCatalogSpecsDto;
import com.zjft.usp.wms.baseinfo.model.WareCatalogSpecs;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 物料分类规格定义表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareCatalogSpecsService extends IService<WareCatalogSpecs> {

    /**
     * 物料分类规格信息列表
     * @datetime 2019/11/18 17:14
     * @version
     * @author dcyu
     * @param wareCatalogId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareCatalogSpecs>
     */
    List<WareCatalogSpecs> listWareCatalogSpecs(Long wareCatalogId);

    /**
     * 根据分类编号查询物料规格
     *
     * @author canlei
     * @param catalogId
     * @return
     */
    List<WareCatalogSpecsDto> listByCatalogId(Long catalogId);

    /**
     * 新增物料分类规格信息
     * @datetime 2019/11/18 17:14
     * @version
     * @author dcyu
     * @param wareCatalogSpecs
     * @return void
     */
    void insertWareCatalogSpecs(List<WareCatalogSpecs> wareCatalogSpecs);

    /**
     * 删除物料分类规格定义信息
     * @datetime 2019/11/18 17:11
     * @version
     * @author dcyu
     * @param wareCatalog
     * @return void
     */
    void deleteWareCatalogSpecs(Long wareCatalog);
}
