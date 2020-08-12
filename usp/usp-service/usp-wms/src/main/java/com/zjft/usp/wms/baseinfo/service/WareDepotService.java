package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.wms.baseinfo.dto.WareDepotDto;
import com.zjft.usp.wms.baseinfo.filter.WareDepotFilter;
import com.zjft.usp.wms.baseinfo.model.WareDepot;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 库房表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareDepotService extends IService<WareDepot> {

    /**
     * 根据corpId获取库房编号和名称映射Map
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param corpId
     * @return java.util.Map<java.lang.Long, java.lang.String>
     */
    Map<Long, String> mapDepotIdAndName(Long corpId);

    /**
     * 物料库房列表
     * @datetime 2019/11/19 9:55
     * @version 
     * @author dcyu 
     * @param 
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareDepot>
     */
    List<WareDepot> listWareDepot();

    /**
      * 物料库房结构型列表（树状）
      * @datetime 2019/11/28 11:23
      * @version
      * @author dcyu
      * @param corpId
      * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareDepotDto>
      */
    List<WareDepotDto> treeWareDepot(Long corpId);

    /**
     * 根据条件查找物料库房信息
     * @datetime 2019/11/20 14:58
     * @version
     * @author dcyu
     * @param wareDepotFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareDepotDto>
     */
    ListWrapper<WareDepotDto> queryWareDepot(WareDepotFilter wareDepotFilter);

    /**
     * 根据ID查找物料库房
     * @datetime 2019/11/19 9:55
     * @version 
     * @author dcyu 
     * @param wareDepotId
     * @return com.zjft.usp.wms.baseinfo.dto.WareDepotDto
     */
    WareDepotDto findWareDepotBy(Long wareDepotId);

    /**
     * 新增物料库房信息
     * @datetime 2019/11/19 9:55
     * @version 
     * @author dcyu 
     * @param wareDepotDto
     * @return void
     */
    void insertWareDepot(WareDepotDto wareDepotDto);

    /**
     * 更新物料库房信息
     * @datetime 2019/11/19 9:55
     * @version 
     * @author dcyu 
     * @param wareDepotDto
     * @return void
     */
    void updateWareDepot(WareDepotDto wareDepotDto);

    /**
     * 删除物料库房信息
     * @datetime 2019/11/19 9:55
     * @version 
     * @author dcyu 
     * @param wareDepotId
     * @return void
     */
    void deleteWareDepot(Long wareDepotId);
}
