package com.zjft.usp.device.device.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceBrandDto;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.device.dto.DeviceInfoDto;
import com.zjft.usp.device.device.filter.DeviceInfoFilter;
import com.zjft.usp.device.device.model.DeviceInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备档案基本表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceInfoService extends IService<DeviceInfo> {

    /**
     * 查询设备档案
     *
     * @param deviceInfoFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/16 6:52 下午
     **/
    ListWrapper<DeviceInfoDto> queryDeviceInfo(DeviceInfoFilter deviceInfoFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 添加设备档案
     *
     * @param deviceInfoDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/17 1:37 下午
     **/
    Long addDeviceInfo(DeviceInfoDto deviceInfoDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 远程调用：添加/修改设备档案
     *
     * @param deviceInfoDto
     * @return
     * @author zgpi
     * @date 2020/2/6 11:18
     **/
    DeviceInfoDto editDeviceInfo(DeviceInfoDto deviceInfoDto);

    /**
     * 修改设备档案
     *
     * @param deviceInfoDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/17 1:37 下午
     **/
    void updateDeviceInfo(DeviceInfoDto deviceInfoDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 删除设备档案
     *
     * @param deviceId
     * @return
     * @author zgpi
     * @date 2019/10/17 1:37 下午
     **/
    void deleteDeviceInfo(Long deviceId);

    /**
     * 查找设备档案
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2019/10/22 3:21 下午
     **/
    DeviceInfoDto findDeviceInfoDto(DeviceInfoFilter deviceInfoFilter);

    /**
     * 根据出厂序列号、设备编号、设备型号查找设备档案
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2020/1/17 18:18
     **/
    DeviceInfoDto findDeviceInfoBy(DeviceInfoFilter deviceInfoFilter);

    /**
     * 根据设备信息条件获取到负责工程师列表
     * @param deviceCode
     * @param smallClassId
     * @param brandId
     * @param modelId
     * @param serial
     * @param demanderCorp
     * @param serviceCorp
     * @return
     */
    List<Long> findDeviceEngineers(String deviceCode, Long smallClassId, Long brandId, Long modelId, String serial, Long demanderCorp, Long serviceCorp);

    /**
     * 根据设备条件获得设备分类列表
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2019/10/22 2:08 下午
     **/
    List<DeviceLargeClassDto> listDeviceClass(DeviceInfoFilter deviceInfoFilter);

    /**
     * 根据设备条件获得设备型号列表
     *
     * @param deviceInfoFilter
     * @return
     * @author zgpi
     * @date 2019/10/22 2:08 下午
     **/
    List<DeviceBrandDto> listDeviceModel(DeviceInfoFilter deviceInfoFilter);

    /**
     * 根据主键获取
     * @param deviceId
     * @return
     */
    DeviceInfoDto getByDeviceId(Long deviceId);

    /**
     * 批量保存设备档案
     *
     * @param deviceInfoDtoList
     * @param userInfo
     * @param demanderCorp
     * @return
     */
    void batchSaveDeviceInfo(List<DeviceInfoDto> deviceInfoDtoList, UserInfo userInfo, Long demanderCorp, Map<String,Object> needInsertMap);

    /**
     * 查询是否已存在设备档案时使用,key为序列号+设备型号,value为deviceId
     *
     * @param demanderCorp
     * @return
     */
    Map<String, Long> checkExistMap(Long demanderCorp);

    List<DeviceInfoDto> findDeviceInfoListBy(DeviceInfoFilter deviceInfoFilter);
}
