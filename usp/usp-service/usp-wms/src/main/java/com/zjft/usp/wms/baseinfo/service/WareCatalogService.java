package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.WareCatalogDto;
import com.zjft.usp.wms.baseinfo.filter.WareCatalogFilter;
import com.zjft.usp.wms.baseinfo.model.WareCatalog;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料分类表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareCatalogService extends IService<WareCatalog> {

    /**
     * 分类名称映射
     * @datetime 2019/11/22 10:52
     * @version 
     * @author dcyu 
     * @param corpId
     * @return java.util.Map
     */
    Map mapCatalogIdAndName(Long corpId);

    /**
     * 物料分类信息列表
     * @datetime 2019/11/18 17:02
     * @version
     * @author dcyu
     * @param
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     */
    List<WareCatalogDto> listWareCatalog();

    /**
     * 物料分类信息列表（已中文映射）
     * @datetime 2019/11/27 10:51
     * @version
     * @author dcyu
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     */
    List<WareCatalogDto> listWareCatalog(Long corpId);
    
    /**
     * 物料分类信息结构型列表
     * @datetime 2019/11/27 16:07
     * @version 
     * @author dcyu 
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     */
    List<WareCatalogDto> treeWareCatalog(Long corpId);

    /**
     * 根据条件查找物料分类信息
     * @datetime 2019/11/20 14:56
     * @version
     * @author dcyu
     * @param wareCatalogFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     */
    ListWrapper<WareCatalogDto> queryWareCatalog(WareCatalogFilter wareCatalogFilter);

    /**
     * 根据ID查找物料分类信息
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     * @param id
     * @return com.zjft.usp.wms.baseinfo.dto.WareCatalogDto
     */
    WareCatalogDto findWareCatalogBy(Long id);

    /**
     * 新增物料分类信息
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     * @param wareCatalogDto
     * @return void
     */
    void insertWareCatalog(WareCatalogDto wareCatalogDto);

    /**
     * 更新物料分类信息
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     * @param wareCatalogDto
     * @return void
     */
    void updateWareCatalog(WareCatalogDto wareCatalogDto);

    /**
     * 删除物料分类信息
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     * @param id
     * @return void
     */
    void deleteWareCatalog(Long id);

    /**
     * 模糊匹配
     *
     * @author canlei
     * @param wareCatalogFilter
     * @return
     */
    List<WareCatalog> match(WareCatalogFilter wareCatalogFilter);
}
