package com.zjft.usp.device.device.service;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.device.dto.DeviceExcelDto;
import com.zjft.usp.device.device.dto.DeviceExportDto;
import com.zjft.usp.device.device.filter.DeviceInfoFilter;

import java.util.List;
import java.util.Map;

/**
 * 设备Excel服务
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-27 14:56
 **/
public interface DeviceExcelService {

    /**
     * 获取导入模板
     *
     * @param demanderCorp
     * @return
     */
    List<DeviceExcelDto> getDeviceTemplate(Long demanderCorp);

    /**
     * 获取下拉框数据
     *
     * @param demanderCorp
     * @return
     */
    Map<Integer, String[]> getMapDropDown(Long demanderCorp);

    /**
     * 校验导入数据
     *
     * @param deviceExcelDtoList
     * @param userInfo
     * @param demanderCorp
     * @return
     */
    String checkImportData(List<DeviceExcelDto> deviceExcelDtoList, UserInfo userInfo, Long demanderCorp);

    /**
     * 获取导出的list集合
     * @param deviceInfoFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<DeviceExportDto> getDeviceExcelList(DeviceInfoFilter deviceInfoFilter, UserInfo userInfo, ReqParam reqParam);

}
