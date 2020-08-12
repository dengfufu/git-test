package com.zjft.usp.device.baseinfo.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceClassCompoDto;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceSmallClassFilter;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备小类表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceSmallClassService extends IService<DeviceSmallClass> {

    /**
     * 分页查询设备类型
     *
     * @param page
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2020/6/10 15:05
     **/
    List<DeviceSmallClassDto> queryDeviceSmallClass(Page page, DeviceSmallClassFilter deviceSmallClassFilter);

    /**
     * 设备分类列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/9/25 1:45 下午
     **/
    List<DeviceLargeClassDto> listDeviceClass(Long corpId);

    List<DeviceSmallClassDto> listDeviceSmallClass(DeviceSmallClassDto smallClassDto);

    /**
     * 获得设备小类详情
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/16 16:43
     **/
    DeviceSmallClassDto findDtoById(Long smallClassId);

    /**
     * 根据客户编号获取设备小类的编号和名称映射
     *
     * @param corpId
     * @return
     */
    Map<Long, String> mapIdAndNameByCorp(Long corpId);

    /**
     * 根据客户编号列表获取设备小类的编号和名称映射
     *
     * @param corpIdList
     * @return
     */
    Map<Long, String> mapIdAndNameByCorpIdList(List<Long> corpIdList);


    /**
     * 根据企业编号list获取设备小类ID与设备大类、设备小类组合对象的映射
     *
     * @param corpIdList
     * @return
     */
    Map<Long, DeviceClassCompoDto> mapDeviceClassCompoByCorpIds(List<Long> corpIdList);


    /**
     * 根据企业编号获取设备小类映射
     *
     * @param corpId
     * @return
     */
    Map<Long, DeviceSmallClass> mapSmallClassByCorpId(Long corpId);

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
     * 模糊查询设备小类
     *
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:20
     **/
    List<DeviceSmallClass> matchDeviceSmallClass(DeviceSmallClassFilter deviceSmallClassFilter);


    /**
     * 保存设备小类
     *
     * @param deviceSmallClassDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    Long save(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 更新设备小类
     *
     * @param deviceSmallClassDto
     * @param userInfo
     * @return
     */
    void update(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo);


    /**
     * 删除设备小类
     *
     * @param id
     */
    void delete(Long id);

    Map<Long, String> getDeviceClassMap(Long customCorp);

    /**
     * 根据设备大类编号获取设备小类编号列表
     *
     * @param largeClassId
     * @return
     */
    List<Long> listSmallClassIdByLargeClassId(Long largeClassId);
}
