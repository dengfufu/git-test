package com.zjft.usp.device.service;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.device.dto.DeviceClassCompoDto;
import com.zjft.usp.device.dto.DeviceInfoDto;
import com.zjft.usp.device.service.fallback.DeviceFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 设备feign接口服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/14 7:51 下午
 **/
@FeignClient(name = "usp-device", fallbackFactory = DeviceFeignServiceFallbackFactory.class)
public interface DeviceFeignService {

    /**
     * 添加/修改设备档案
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/2/6 11:21
     **/
    @RequestMapping(value = "/device-info/feign/edit", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<DeviceInfoDto> editDeviceInfo(@RequestBody String json);

    /**
     * 获得设备档案
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/1/17 18:15
     **/
    @RequestMapping(value = "/device-info/feign/find", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result findDeviceInfoBy(@RequestBody String json);

    /**
     * 获得设备档案
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/1/17 18:15
     **/
    @RequestMapping(value = "/device-info/feign/findList", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result findDeviceInfoListBy(@RequestBody String json);

    /**
     * 获得设备档案
     *
     * @param deviceId
     * @returnInfo
     * @author zgpi
     * @date 2019/10/15 9:06 上午
     **/
    @RequestMapping(value = "/device-info/feign/{deviceId}", method = RequestMethod.GET)
    Result<DeviceInfoDto> findDeviceInfo(@PathVariable("deviceId") Long deviceId);

    /**
     * 获得设备品牌
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/10/15 8:39 上午
     **/
    @RequestMapping(value = "/device-brand/feign/{id}", method = RequestMethod.GET)
    Result findDeviceBrand(@PathVariable("id") Long id);

    /**
     * 获得设备型号
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/10/15 8:39 上午
     **/
    @RequestMapping(value = "/device-model/feign/{id}", method = RequestMethod.GET)
    Result findDeviceModel(@PathVariable("id") Long id);

    /**
     * 根据企业编号获取设备品牌编号和名称的映射
     *
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/device-brand/feign/map/{corpId}", method = RequestMethod.GET)
    Result<Map<Long, String>> mapDeviceBrandByCorp(@PathVariable("corpId") Long corpId);

    /**
     * 根据企业编号获取设备大类编号和名称的映射
     *
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/device-large-class/feign/map/{corpId}", method = RequestMethod.GET)
    Result<Map<Long, String>> mapLargeClassByCorp(@PathVariable("corpId") Long corpId);

    /**
     * 获取设备型号编号和名称的映射
     *
     * @return
     */
    @RequestMapping(value = "/device-model/feign/map", method = RequestMethod.GET)
    Result<Map<Long, String>> mapDeviceModel();

    /**
     * 根据企业编号列表获取设备型号编号和名称的映射
     *
     * @return
     */
    @RequestMapping(value = "/device-model/feign/mapByCorpIdList", method = RequestMethod.POST)
    Result<Map<Long, String>> mapDeviceModelByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList);

    /**
     * 根据企业编号列表获取设备型号编号和名称的映射(参数数据量过大时推荐使用，防止参数长度超出content-length)
     * @param jsonFilter
     * @return
     */
    @RequestMapping(value = "/device-model/feign/mapByJsonCorpIds", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapDeviceModelByJsonCorpIds(@RequestBody String jsonFilter);

    /**
     * 根据企业编号获取设备小类编号和名称的映射
     *
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/device-small-class/feign/map/{corpId}", method = RequestMethod.GET)
    Result<Map<Long, String>> mapSmallClassByCorp(@PathVariable("corpId") Long corpId);

    /**
     * 获得设备小类
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/10/15 8:39 上午
     **/
    @RequestMapping(value = "/device-small-class/feign/{id}", method = RequestMethod.GET)
    Result findDeviceSmallClass(@PathVariable("id") Long id);

    /**
     * 获得设备规格
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2020/1/20 15:32
     **/
    @RequestMapping(value = "/device-specification/feign/{id}", method = RequestMethod.GET)
    Result findDeviceSpecification(@PathVariable("id") Long id);

    /***
     * 设备规格ID与名称映射
     * @date 2020/3/15
     * @param jsonFilter
     * @return com.zjft.usp.common.model.Result<java.util.Map<java.lang.Long,java.lang.String>>
     */
    @RequestMapping(value = "/device-specification/feign/mapByIdList", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapSpecificationAndNameByIdList(@RequestBody String jsonFilter);

    /**
     * 根据企业编号list获取设备品牌的映射
     *
     * @param corpIdList
     * @return
     */
    @RequestMapping(value = "/device-brand/feign/mapByCorpIdList", method = RequestMethod.POST)
    Result<Map<Long, String>> mapDeviceBrandByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList);

    /**
     * 根据企业编号json格式获取设备品牌的映射(推荐使用，防止参数超出content-length长度)
     * @param jsonFilter
     * @return
     */
    @RequestMapping(value = "/device-brand/feign/mapByJsonCorpIds", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapDeviceBrandByJsonCorpIds(@RequestBody String jsonFilter);

    /**
     * 根据企业编号list获取设备大类的映射
     *
     * @param corpIdList
     * @return
     */
    @RequestMapping(value = "/device-large-class/feign/mapByCorpIdList", method = RequestMethod.POST)
    Result<Map<Long, String>> mapLargeClassByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList);

    /**
     * 根据企业编号list获取设备小类的映射
     *
     * @param corpIdList
     * @return
     */
    @RequestMapping(value = "/device-small-class/feign/mapByCorpIdList", method = RequestMethod.POST)
    Result<Map<Long, String>> mapSmallClassByCorpIdList(@RequestParam("corpIdList") List<Long> corpIdList);

    /**
     * 根据企业编号list获取设备小类ID与设备大类、设备小类组合对象的映射
     * @param jsonFilter
     * @return
     */
    @RequestMapping(value = "/device-class/feign/mapDeviceClassCompoByCorpIds", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, DeviceClassCompoDto>> mapDeviceClassCompoByCorpIds(@RequestBody String jsonFilter);


    /**
     * 根据设备信息查找设备负责工程师
     *
     * @param deviceCode
     * @param smallClassId
     * @param brandId
     * @param modelId
     * @param serial
     * @param demanderCorp
     * @param serviceCorp
     * @return
     */
    @RequestMapping(value = "/device-info/feign/findDeviceEngineers", method = RequestMethod.POST)
    Result<List<Long>> findDeviceEngineers(@RequestParam("deviceCode") String deviceCode,
                                           @RequestParam("smallClassId") Long smallClassId,
                                           @RequestParam("brandId") Long brandId,
                                           @RequestParam("modelId") Long modelId,
                                           @RequestParam("serial") String serial,
                                           @RequestParam("demanderCorp") Long demanderCorp,
                                           @RequestParam("serviceCorp") Long serviceCorp);

    @RequestMapping(value = "/device-service/feign/deleteDeviceService", method = RequestMethod.POST)
    Result delDeviceService(@RequestParam("userId") Long userId,
                            @RequestParam("corpId") Long corpId,
                            @RequestParam("currentUserId") Long currentUserId,
                            @RequestParam("clientId") String clientId);

    /**
     * 根据企业编号列表获取设备规格编号和名称的映射
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/device-specification/feign/map/{corpId}", method = RequestMethod.GET)
    Result<Map<Long, String>> mapSpecificationByCorp(@PathVariable("corpId") Long corpId);


    /**
     * 获得设备分类小类id和大类名称-小类名称map
     * @param customCorp
     * @return
     */
    @RequestMapping(value = "/device-class/feign/map/{customCorp}", method = RequestMethod.GET)
    Result<Map<Long, String>> getDeviceClassMap(@PathVariable("customCorp") Long customCorp);

    /**
     * 获得某个小类设备规格map
     * @param smallClassId
     * @return
     */
    @RequestMapping(value = "/device-specification/feign/mapBySmallClassId/{smallClassId}", method = RequestMethod.GET)
    Result<Map<Long, String>> mapSpecificationBymallClassId(@PathVariable("smallClassId") Long smallClassId);

    /**
     * 远程调用：根据企业ID型号编号小类编号获取型号编号和名称的映射
     * @param corpId
     * @param brandId
     * @param smallClassId
     * @return
     */
    @RequestMapping(value = "/device-model/feign/mapByCorpIdBrandIdSmallClassId", method = RequestMethod.POST)
    Result<Map<Long, String>> mapByCorpIdBrandIdSmallClassId(@RequestParam("corpId") Long corpId,
                                                             @RequestParam("brandId") Long brandId,
                                                             @RequestParam("smallClassId") Long smallClassId);

    /**
     * 根据设备大类编号查询设备小类编号列表
     *
     * @param largeClassId
     * @return
     */
    @RequestMapping(value = "/device-small-class/listSmallClassIdByLargeClassId", method = RequestMethod.POST)
    Result<List<Long>> listSmallClassIdByLargeClassId(@RequestParam("largeClassId") Long largeClassId);

    /**
     * 根据规格编号列表获取  规格编号->[小类名称]规格名称  映射
     *
     * @param idListJson
     * @return
     */
    @RequestMapping(value = "/device-specification/feign/mapSpecIdAndSmallClassSpecName", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapSpecIdAndSmallClassSpecName(@RequestBody String idListJson);

}
