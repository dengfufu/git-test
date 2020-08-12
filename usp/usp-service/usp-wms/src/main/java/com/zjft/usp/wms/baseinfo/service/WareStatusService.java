package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.wms.baseinfo.filter.WareStatusFilter;
import com.zjft.usp.wms.baseinfo.model.WareStatus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物料状态表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareStatusService extends IService<WareStatus> {

    /**
     * 根据corpId获取物料状态编号与名称映射Map
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param corpId
     * @return java.util.Map<java.lang.Integer, java.lang.String>
     */
    Map<Integer, String> mapIdAndName(Long corpId);

    /**
     * 物料状态列表
     * @datetime 2019/11/26 10:11
     * @version
     * @author dcyu
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareStatus>
     */
    List<WareStatus> listWareStatus(Long corpId);

    /**
     * 根据条件查找物料状态信息
     * @datetime 2019/11/26 10:11
     * @version
     * @author dcyu
     * @param wareStatusFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WareStatus>
     */
    ListWrapper<WareStatus> queryWareStatus(WareStatusFilter wareStatusFilter);

    /**
     * 新增物料状态
     * @datetime 2019/11/22 17:29
     * @version
     * @author dcyu
     * @param wareStatus
     * @return void
     */
    void insertWareStatus(WareStatus wareStatus);

    /**
     * 更新物料状态
     * @datetime 2019/11/25 16:26
     * @version
     * @author dcyu
     * @param wareStatus
     * @return void
     */
    void updateWareStatus(WareStatus wareStatus);

    /** 
     * 删除物料状态信息
     * @datetime 2019-12-02 10:13
     * @version
     * @author dcyu 
     * @param wareStatusId
     * @return void
     */
    void deleteWareStatus(Long wareStatusId);
}
