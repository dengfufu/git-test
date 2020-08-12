package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.wms.baseinfo.dto.WareBrandDto;
import com.zjft.usp.wms.baseinfo.filter.WareBrandFilter;
import com.zjft.usp.wms.baseinfo.model.WareBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料品牌表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareBrandService extends IService<WareBrand> {

    /**
     * 物料品牌名称映射
     * @datetime 2019/11/22 10:37
     * @version
     * @author dcyu
     * @param corpId
     * @return java.util.Map
     */
    Map mapBrandIdAndName(Long corpId);

    /**
     * 分页查询
     * @param wareBrandFilter
     * @return
     */
    ListWrapper<WareBrandDto> queryWareBrand(WareBrandFilter wareBrandFilter);

    /**
     * 物料品牌信息列表
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     * @param
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareBrand>
     */
    List<WareBrand> listWareBrand(Long corpId);

    /**
     * 根据ID查找物料品牌
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     * @param id
     * @return com.zjft.usp.wms.baseinfo.model.WareBrand
     */
    WareBrand findWareBrandBy(Long id);

    /**
     * 新增物料品牌信息
     * @datetime 2019/11/18 16:33
     * @version
     * @author dcyu
     * @param wareBrand
     * @return void
     */
    void insertWareBrand(WareBrand wareBrand);

    /**
     * 更新物料品牌信息
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     * @param wareBrand
     * @return void
     */
    void updateWareBrand(WareBrand wareBrand);

    /**
     * 删除物料品牌信息
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     * @param id
     * @return void
     */
    void deleteWareBrand(Long id);

    /**
     * 模糊匹配
     *
     * @author cnalei
     * @param wareBrandFilter
     * @return
     */
    List<WareBrand> match(WareBrandFilter wareBrandFilter);
}
