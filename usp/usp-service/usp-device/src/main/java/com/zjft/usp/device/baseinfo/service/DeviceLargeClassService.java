package com.zjft.usp.device.baseinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceLargeClassFilter;
import com.zjft.usp.device.baseinfo.model.DeviceLargeClass;

import java.util.List;
import java.util.Map;


/**
 * <p>
 * 设备大类表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceLargeClassService extends IService<DeviceLargeClass> {

    List<DeviceLargeClassDto> listDeviceLargeClass(DeviceLargeClassFilter deviceLargeClassFilter);

    /**
     * 根据客户企业编号获取设备大类映射
     * @param corpId
     * @return
     */
    Map<Long, String> mapClassIdAndNameByCorp(Long corpId);

    /**
     * 根据客户企业编号列表获取设备大类映射
     * @param corpIdList
     * @return
     */
    Map<Long, String> mapClassIdAndNameByCorpList(List<Long> corpIdList);

    /**
     * 分页查询设备大类
     * @param deviceLargeClassFilter
     * @return
     */
    ListWrapper<DeviceLargeClassDto> query(DeviceLargeClassFilter deviceLargeClassFilter);

    /**
     * 获得最大顺序号
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/11/15 16:47
     **/
    Integer findMaxSortNo(Long corpId);

    /**
     * 模糊查询设备大类
     *
     * @param deviceLargeClassFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:19
     **/
    List<DeviceLargeClass> matchDeviceLargeClass(DeviceLargeClassFilter deviceLargeClassFilter);


    /**
     * 保存设备大类
     * @param deviceLargeClass
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(DeviceLargeClass deviceLargeClass, UserInfo userInfo, ReqParam reqParam);

    /**
     * 更新设备大类
     * @param deviceLargeClass
     * @param userInfo
     * @return
     */
    void update(DeviceLargeClass deviceLargeClass, UserInfo userInfo);

    /**
     * 删除设备大类
     * @param id
     */
    void delete(Long id);
}
