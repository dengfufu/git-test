package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.model.WareClassDevice;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品类型适用设备表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WareClassDeviceService extends IService<WareClassDevice> {

    /**
     * 根据服务商企业编号获取物品分类编号和设备小类编号列表的映射
     * @param corpId
     * @return
     */
    Map<Long, List<Long>> mapIdAndSmallClassIdListByCorpId(Long corpId);

    /**
     * 根据服务商企业编号获取物品分类编号和设备小类名称（多个用,分隔）的映射
     * @param corpId
     * @return
     */
    Map<Long, String> mapIdAndSmallClassNamesByCorpId(Long corpId);

}
