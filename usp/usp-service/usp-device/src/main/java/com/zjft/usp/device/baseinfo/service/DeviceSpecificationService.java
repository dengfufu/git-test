package com.zjft.usp.device.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.device.baseinfo.dto.DeviceSpecificationDto;
import com.zjft.usp.device.baseinfo.filter.DeviceSpecificationFilter;
import com.zjft.usp.device.baseinfo.model.DeviceSpecification;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备规格表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-01-20
 */
public interface DeviceSpecificationService extends IService<DeviceSpecification> {

    /**
     * 分页查询设备规格列表
     *
     * @param deviceSpecificationFilter
     * @return
     * @author zgpi
     * @date 2020/1/20 14:43
     **/
    ListWrapper<DeviceSpecification> query(DeviceSpecificationFilter deviceSpecificationFilter);

    /**
     * 查询设备规格列表
     *
     * @param deviceSpecificationFilter
     * @return
     * @author zgpi
     * @date 2020/1/20 14:43
     **/
    List<DeviceSpecificationDto> list(DeviceSpecificationFilter deviceSpecificationFilter);

    /**
     * 设备小类编号与规格列表映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    Map<Long, List<DeviceSpecification>> mapSmallClassIdAndSpecificationList(Long corpId);

    /**
     * 设备规格ID与名称映射
     * @date 2020/3/15
     * @param IdList
     * @return java.util.Map<java.lang.Long,java.lang.String>
     */
    Map<Long, String> mapIdAndName(List<Long> IdList);

    /**
     * 删除设备规格
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    void delDeviceSpecification(Long smallClassId);

    /**
     * 根据企业id获取设备规格map
     * @param corpId
     * @return
     */
    Map<Long, String> mapSpecificationByCorp(Long corpId);

    /**
     * 根据小类标号获取设备规格编号与名称映射
     *
     * @param smallClassId
     * @return
     */
    Map<Long, String> mapBySmallClassId(Long smallClassId);

    /**
     * 获取 规格编号->[小类名称]规格名称
     *
     * @param idList
     * @return
     */
    Map<Long, String> mapIdAndSmallClassSpecName(List<Long> idList);
}
