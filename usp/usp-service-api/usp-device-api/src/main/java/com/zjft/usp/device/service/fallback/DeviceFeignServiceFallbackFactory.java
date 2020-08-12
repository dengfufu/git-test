package com.zjft.usp.device.service.fallback;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.device.dto.DeviceClassCompoDto;
import com.zjft.usp.device.dto.DeviceInfoDto;
import com.zjft.usp.device.service.DeviceFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * @author zgpi
 * @date 2019/10/14 19:53
 * @version 1.0
 **/
@Slf4j
@Component
public class DeviceFeignServiceFallbackFactory implements FallbackFactory<DeviceFeignService> {
    @Override
    public DeviceFeignService create(Throwable cause) {
        return new DeviceFeignService() {

            @Override
            public Result findDeviceSmallClass(Long smallClassId) {
                return null;
            }

            /**
             * 获得设备规格
             *
             * @param id
             * @return
             * @author zgpi
             * @date 2020/1/20 15:32
             **/
            @Override
            public Result findDeviceSpecification(Long id) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapSpecificationAndNameByIdList(String jsonFilter) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapDeviceBrandByCorpIdList(List<Long> corpIdList) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapDeviceBrandByJsonCorpIds(String jsonFilter) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapLargeClassByCorpIdList(List<Long> corpIdList) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapSmallClassByCorpIdList(List<Long> corpIdList) {
                return null;
            }

            @Override
            public Result<Map<Long, DeviceClassCompoDto>> mapDeviceClassCompoByCorpIds(String jsonFilter) {
                return null;
            }

            @Override
            public Result findDeviceBrand(Long brandId) {
                return null;
            }

            @Override
            public Result findDeviceModel(Long modelId) {
                return null;
            }


            /**
             * 添加/修改设备档案
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2020/2/6 11:21
             **/
            @Override
            public Result<DeviceInfoDto> editDeviceInfo(String json) {
                return null;
            }

            @Override
            public Result findDeviceInfoBy(String json) {
                return null;
            }

            @Override
            public Result findDeviceInfoListBy(String json) {
                return null;
            }

            @Override
            public Result findDeviceInfo(Long deviceId) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapDeviceBrandByCorp(Long corpId) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapDeviceModel() {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapDeviceModelByCorpIdList(List<Long> corpIdList) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapDeviceModelByJsonCorpIds(String jsonFilter) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapLargeClassByCorp(Long customCorp) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapSmallClassByCorp(Long customCorp) {
                return null;
            }

            @Override
            public Result<List<Long>> findDeviceEngineers(String deviceCode, Long smallClassId, Long brandId, Long modelId, String serial, Long demanderCorp, Long serviceCorp) {
                return null;
            }
            @Override
            public Result<Map<Long, String>> mapSpecificationByCorp( Long corpId){
                return null;
            }

            @Override
            public Result<Map<Long, String>> getDeviceClassMap(Long customCorp) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapSpecificationBymallClassId(Long smallClassId) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapByCorpIdBrandIdSmallClassId(Long corpId, Long brandId, Long smallClassId) {
                return null;
            }

            @Override
            public Result<List<Long>> listSmallClassIdByLargeClassId(Long largeClassId) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapSpecIdAndSmallClassSpecName(String idListJson) {
                return null;
            }

            @Override
            public Result delDeviceService(Long userId, Long corpId, Long currentUserId, String clientId) {
                log.error("DeviceFeign:delDeviceService,调用失败");
                return null;
            }


        };
    }
}
