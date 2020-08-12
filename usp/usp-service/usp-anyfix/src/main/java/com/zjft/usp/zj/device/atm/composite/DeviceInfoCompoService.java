package com.zjft.usp.zj.device.atm.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.device.atm.dto.*;
import com.zjft.usp.zj.device.atm.filter.DeviceFilter;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.PartReplaceDto;

/**
 * ATM设备信息聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 17:10
 **/
public interface DeviceInfoCompoService {

    /**
     * 分页查询设备列表
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-29
     */
    ListWrapper<DeviceDto> queryDevice(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得机器详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    DeviceDto findDeviceDetail(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得设备档案
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    DeviceDto findDeviceArchive(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询安装记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    ListWrapper<InstallRecordDto> listInstallRecord(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询升级记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    ListWrapper<UpdateRecordDto> listUpdateRecord(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询维护PM记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    ListWrapper<MaintainPmDto> listMaintainPm(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看维护详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    MaintainDto viewMaintainDetail(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看PM详情
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    PmDto viewPmDetail(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询维护PM更换备件记录
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    ListWrapper<PartDto> listPartReplace(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查找机器最近CASE情况
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    ListWrapper<CaseDto> listRecentCase(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查找机器最近更换备件情况
     *
     * @param deviceFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-28
     */
    ListWrapper<PartReplaceDto> listRecentPartReplace(DeviceFilter deviceFilter, UserInfo userInfo, ReqParam reqParam);
}
