package com.zjft.usp.device.baseinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceModelDto;
import com.zjft.usp.device.baseinfo.filter.DeviceModelFilter;
import com.zjft.usp.device.baseinfo.model.DeviceModel;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备型号表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceModelService extends IService<DeviceModel> {

    List<DeviceModelDto> listDeviceModel(DeviceModelFilter deviceModelFilter);

    /**
     * 根据企业编号列表获得编号与名称映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2019/10/16 7:51 下午
     **/
    Map<Long, String> mapIdAndNameByCorpIdList(List<Long> corpIdList);

    /**
     * 根据委托商编号获取型号编号与名称映射
     *
     * @param corpId
     * @return
     */
    Map<Long, String> mapIdAndNameByCorp(Long corpId);

    /**
     * 获得编号与名称映射
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/10/16 7:51 下午
     **/
    Map<Long, String> mapIdAndName();

    /**
     * 分页查询
     * @param deviceModelFilter
     * @return
     */
    ListWrapper<DeviceModelDto> query(DeviceModelFilter deviceModelFilter);

    /**
     * 模糊查询设备型号
     *
     * @param deviceModelFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:42
     **/
    List<DeviceModel> matchDeviceModel(DeviceModelFilter deviceModelFilter);


    /**
     * 保存设备小类
     * @param deviceModel
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(DeviceModel deviceModel, UserInfo userInfo, ReqParam reqParam);

    /**
     * 更新设备小类
     * @param deviceModel
     * @param userInfo
     * @return
     */
    void update(DeviceModel deviceModel, UserInfo userInfo);

    /**
     * 获取型号编号和型号的映射
     *
     * @param corpId
     * @return
     */
    Map<Long, DeviceModel> mapIdAndModelByCorp(Long corpId);

    /**
     * 批量增加设备型号
     * @param deviceModelList
     * @return
     */
    Map<DeviceModel,Long>  batchAddDeviceModel(List<DeviceModel> deviceModelList, Long userId);
}

