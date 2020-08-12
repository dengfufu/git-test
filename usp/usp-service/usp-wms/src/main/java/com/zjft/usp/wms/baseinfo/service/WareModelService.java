package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.WareModelDto;
import com.zjft.usp.wms.baseinfo.filter.WareModelFilter;
import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 型号表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareModelService extends IService<WareModel> {
    /***
     * 备件型号编号与名称映射
     *
     * @author Qiugm
     * @date 2019-11-15
     * @param corpId
     * @return java.util.Map<java.lang.Long, java.lang.String>
     */
    Map<Long, String> mapModelIdAndName(Long corpId);

    /**
     * 物料型号列表
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     * @param
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareModelDto>
     */
    List<WareModelDto> listWareModel(WareModelFilter wareModelFilter);

    /**
     * 根据条件查找物料型号
     * @datetime 2019/11/20 17:30
     * @version
     * @author dcyu
     * @param wareModelFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareModelDto>
     */
    ListWrapper<WareModelDto> queryWareModel(WareModelFilter wareModelFilter);

    /**
     * 根据ID查找物料型号信息
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     * @param wareModelId
     * @return com.zjft.usp.wms.baseinfo.model.WareModel
     */
    WareModel findWareModelBy(Long wareModelId);

    /**
     * 新增物料型号信息
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     * @param wareModelDto
     * @return void
     */
    void insertWareModel(WareModelDto wareModelDto);

    /**
     * 更新物料型号信息
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     * @param wareModelDto
     * @return void
     */
    void updateWareModel(WareModelDto wareModelDto);

    /**
     * 删除物料型号信息
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     * @param wareModelId
     * @return void
     */
    void deleteWareModel(Long wareModelId);

    /**
     * 获取id和model的映射
     * @param corpId
     * @return
     * @author canlei
     */
    Map<Long, WareModel> mapIdAndModel(Long corpId);
}
