package com.zjft.usp.device.device.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.constant.ExcelCodeMsg;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.model.DeviceModel;
import com.zjft.usp.device.baseinfo.service.*;
import com.zjft.usp.device.device.dto.DeviceExcelDto;
import com.zjft.usp.device.device.dto.DeviceExportDto;
import com.zjft.usp.device.device.dto.DeviceInfoDto;
import com.zjft.usp.device.device.enums.DeviceStatusEnum;
import com.zjft.usp.device.device.enums.WarrantyEnum;
import com.zjft.usp.device.device.filter.DeviceInfoFilter;
import com.zjft.usp.device.device.model.CustomDeviceRelate;
import com.zjft.usp.device.device.service.DeviceExcelService;
import com.zjft.usp.device.device.service.DeviceInfoService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 设备Excel服务实现类
 *
 * @author canlei
 * @version 1.0
 * @date 2020-02-27 14:58
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceExcelServiceImpl implements DeviceExcelService {

    @Autowired
    private DeviceLargeClassService deviceLargeClassService;
    @Autowired
    private DeviceSmallClassService deviceSmallClassService;
    @Autowired
    private DeviceBrandService deviceBrandService;
    @Autowired
    private DeviceModelService deviceModelService;
    @Autowired
    private DeviceInfoService deviceInfoService;
    @Autowired
    private DeviceSpecificationService deviceSpecificationService;
    @Resource
    private AnyfixFeignService anyfixFeignService;
    @Resource
    private UasFeignService uasFeignService;

    public static final String FAULT_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";

    public static final DateFormat FAULT_DATE_FORMAT = new SimpleDateFormat(FAULT_DATE_FORMAT_STR);
    /**
     * 验证手机号
     */
    public static final String REGEX_MOBILEPHONE = "^(1[3,4,5,7,8]\\d{9})$";
    /**
     * 验证带区号的座机
     */
    public static final String REGEX_PHONE_QH = "^(0[1-9]{2,3}-\\d{5,10})$";
    /**
     * 验证没有区号的座机
     */
    public static final String REGEX_PHONE = "^([1-9]{1}\\[0-9]{5,8})$";
    /**
     * 日期格式
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 获取导入模板
     *
     * @return
     */
    @Override
    public List<DeviceExcelDto> getDeviceTemplate(Long demanderCorp) {
        List<DeviceExcelDto> deviceExcelDtoList = new ArrayList<>();
        Calendar today = Calendar.getInstance();
        DeviceExcelDto deviceExcelDto = DeviceExcelDto.builder()
                .customCorpName("客户名称XXX")
                .contactName("张三")
                .contactPhone("133XXXXXXXX")
                .deviceBranchName("设备网点XXX")
                .provinceName("北京")
                .cityName("北京市")
                .districtName("东城区")
                .address("详细地址")
                .zoneName("市区")
                .serviceCorpName("服务商名称XXX")
                .largeClassName("电脑")
                .smallClassName("笔记本")
                .specificationName("256G+16G")
                .brandName("苹果")
                .modelName("MacBook Pro")
                .serial("XXXXXXX")
                .deviceCode("XXXXXXXX")
                .statusName("运行")
                .factoryDate(DateUtil.format(today.getTime(), "yyyy-MM-dd"))
                .purchaseDate(DateUtil.format(today.getTime(), "yyyy-MM-dd"))
                .installDate(DateUtil.format(today.getTime(), "yyyy-MM-dd"))
                .description("设备描述")
                .warrantyModeName("单次保")
                .contNo("XXXXXXXX")
                .warrantyStartDate(DateUtil.format(today.getTime(), "yyyy-MM-dd"))
                .warrantyEndDate(DateUtil.format(today.getTime(), "yyyy-MM-dd"))
                .warrantyStatusName("保内")
                .warrantyNote("保修说明")
                .build();
        deviceExcelDtoList.add(deviceExcelDto);
        return deviceExcelDtoList;
    }

    /**
     * 获取下拉框数据
     *
     * @param demanderCorp
     * @return
     */
    @Override
    public Map<Integer, String[]> getMapDropDown(Long demanderCorp) {
        Map<Integer, String[]> dropDownMap = new HashMap<>();
        if (LongUtil.isZero(demanderCorp)) {
            return dropDownMap;
        }
        // 客户
//        Result<Map<Long, String>> customMapResult = this.anyfixFeignService.mapCustomIdAndNameByDemander(demanderCorp);
//        Map<Long, String> customCorpMap = customMapResult == null ? new HashMap<>() : customMapResult.getData();
//        dropDownMap.put(0, customCorpMap.values().toArray(new String[customCorpMap.size()]));

        // 分布
        dropDownMap.put(8, new String[]{"市区", "郊县"});

        // 服务商
        Result<List<Long>> serviceCorpListResult = this.anyfixFeignService.listServiceCorpIdsByDemander(demanderCorp);
        List<Long> serviceCorpList = serviceCorpListResult == null ? new ArrayList<>() : serviceCorpListResult.getData();
        Result<Map<Long, String>> serviceCorpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(serviceCorpList));
        Map<Long, String> serviceCorpMap = serviceCorpMapResult == null ? new HashMap<>() : serviceCorpMapResult.getData();
        dropDownMap.put(9, serviceCorpMap.values().toArray(new String[serviceCorpMap.size()]));

        // 设备大类
        Map<Long, String> largeClassMap = this.deviceLargeClassService.mapClassIdAndNameByCorp(demanderCorp);
        dropDownMap.put(10, largeClassMap.values().toArray(new String[largeClassMap.size()]));

        // 设备类型
        Map<Long, String> smallClassMap = this.deviceSmallClassService.mapIdAndNameByCorp(demanderCorp);
        dropDownMap.put(11, smallClassMap.values().toArray(new String[smallClassMap.size()]));

        // 设备规格
        Map<Long, String> specificationMap = this.deviceSpecificationService.mapSpecificationByCorp(demanderCorp);
        dropDownMap.put(12, specificationMap.values().toArray(new String[specificationMap.size()]));

        // 品牌
        Map<Long, String> brandMap = this.deviceBrandService.mapIdAndNameByCorpId(demanderCorp);
        dropDownMap.put(13, brandMap.values().toArray(new String[brandMap.size()]));

        // 型号
        Map<Long, String> modelMap = this.deviceModelService.mapIdAndNameByCorp(demanderCorp);
        dropDownMap.put(14, modelMap.values().toArray(new String[modelMap.size()]));

        // 设备状态
        dropDownMap.put(17, new String[]{"运行", "暂停", "死亡"});

        // 维保方式
        dropDownMap.put(22, new String[]{"整机保", "单次保"});

        // 保修状态
        List<String> warrantyStatuses = new ArrayList<>();
        for (WarrantyEnum warrantyEnum : WarrantyEnum.values()) {
            warrantyStatuses.add(warrantyEnum.getName());
        }
        dropDownMap.put(26, warrantyStatuses.toArray(new String[warrantyStatuses.size()]));

        return dropDownMap;
    }

    /**
     * 校验导入数据
     * @param deviceExcelDtoList
     * @param userInfo
     * @param demanderCorp
     * @return
     */
    @Override
    public String checkImportData(List<DeviceExcelDto> deviceExcelDtoList, UserInfo userInfo, Long demanderCorp) {

        if (CollectionUtil.isEmpty(deviceExcelDtoList)) {
            throw new AppException("未读取到excel数据，请检查");
        }

        StringBuilder sb = new StringBuilder(64);

        // 客户映射
        Result<Map<Long, String>> result = this.anyfixFeignService.mapCustomIdAndNameByDemander(demanderCorp);
        Map<Long, String> customMap = result == null ? new HashMap<>() : result.getData();
        // 设备网点映射
        List<Long> customIdList = new ArrayList<>(customMap.keySet());
        Result<Map<Long, String>> deviceBranchMapResult = this.anyfixFeignService.mapDeviceBranchByCustomIdList(customIdList);
        Map<Long, String> deviceBranchMap = deviceBranchMapResult == null ? new HashMap<>() : deviceBranchMapResult.getData();
        // 服务商映射
        Result<List<Long>> serviceCorpListResult = this.anyfixFeignService.listServiceCorpIdsByDemander(demanderCorp);
        List<Long> serviceCorpList = serviceCorpListResult == null ? new ArrayList<>() : serviceCorpListResult.getData();
        Result<Map<Long, String>> serviceCorpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(serviceCorpList));
        Map<Long, String> serviceCorpMap = serviceCorpMapResult == null ? new HashMap<>() : serviceCorpMapResult.getData();
        // 设备大类映射
        Map<Long, String> largeClassMap = this.deviceLargeClassService.mapClassIdAndNameByCorp(demanderCorp);
        // 设备小类映射
        Map<Long, String> smallClassMap = this.deviceSmallClassService.mapIdAndNameByCorp(demanderCorp);
        // 设备规格映射
        Map<Long, String> specificationMap = this.deviceSpecificationService.mapSpecificationByCorp(demanderCorp);
        // 设备品牌映射
        Map<Long, String> brandMap = this.deviceBrandService.mapIdAndNameByCorpId(demanderCorp);
        // 设备型号id和名称映射
        Map<Long, String> modelIdAndNameMap = this.deviceModelService.mapIdAndNameByCorp(demanderCorp);
        // 设备型号id和model映射
        Map<Long, DeviceModel> modelMap = this.deviceModelService.mapIdAndModelByCorp(demanderCorp);
        // 地区映射
        Result<Map<String, String>> areaMapResult = this.uasFeignService.mapAreaCodeAndName();
        Map<String, String> areaMap = areaMapResult == null ? new HashMap<>() : areaMapResult.getData();

        // 存储的数据
        List<DeviceInfoDto> deviceInfoList = new ArrayList<>();
        int rowNum = 1;
        // 用来校验excel内重复数据，key为序列号+设备型号，value为行号集合
        Map<String, List<Integer>> repeatCheckMap = new HashMap<>();

        // 用来校验数据库已存在的数据，key为序列号+设备型号，value为deviceId
        Map<String, Long> existCheckMap = this.deviceInfoService.checkExistMap(demanderCorp);
        Map<String,Object> needInsertMap = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<String> needInsertCustomCorp = new ArrayList<>();
        List<CustomDeviceRelate> needInsertDeviceBranch = new ArrayList<>();
        Map<DeviceInfoDto,DeviceModel> infoDtoDeviceModelMap = new HashMap<>();
        List<DeviceModel> needInsertDeviceMode = new ArrayList<>();
        for (DeviceExcelDto deviceExcelDto: deviceExcelDtoList) {
            DeviceInfoDto deviceInfoDto = new DeviceInfoDto();
            deviceInfoDto.setDemanderCorp(demanderCorp);
            rowNum ++;
            // 0，*校验客户
            String customCorpName = StrUtil.trimToEmpty(deviceExcelDto.getCustomCorpName());
            if (StringUtils.isEmpty(customCorpName)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EMPTY.fillArgs(rowNum, "客户名称").getMsg()).append("<br>");
            } else if (!customMap.containsValue(customCorpName)) {
                deviceInfoDto.setCustomCorpName(customCorpName);
                needInsertCustomCorp.add(customCorpName);
            } else {
                deviceInfoDto.setCustomId(customMap.entrySet().stream().filter(entry -> customCorpName.equals(entry.getValue()))
                .findAny().orElse(null).getKey());
            }

            // 1，校验联系人
            String contactName = StrUtil.trimToEmpty(deviceExcelDto.getContactName());
            if (!StringUtils.isEmpty(contactName) && contactName.trim().length() > 20) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "客户联系人", 20).getMsg()).append("<br>");
            } else {
                deviceInfoDto.setContactName(contactName);
            }

            // 2，校验联系人电话
            String contactPhone = StrUtil.trimToEmpty(deviceExcelDto.getContactPhone());
            if (!StringUtils.isEmpty(contactPhone)) {
                if (!Pattern.matches(REGEX_MOBILEPHONE, contactPhone)
                        && !Pattern.matches(REGEX_PHONE, contactPhone)
                        && !Pattern.matches(REGEX_PHONE_QH, contactPhone)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNum, "联系人电话").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setContactPhone(contactPhone);
                }
            }

            // 3，校验设备网点
            String deviceBranchName = StrUtil.trimToEmpty(deviceExcelDto.getDeviceBranchName());
            if (!StringUtils.isEmpty(deviceBranchName)) {
                if (!deviceBranchMap.containsValue(deviceBranchName)) {
                    CustomDeviceRelate customDeviceRelate = new CustomDeviceRelate();
                    customDeviceRelate.setCustomCorpName(customCorpName);
                    customDeviceRelate.setCustomId(deviceInfoDto.getCustomId());
                    customDeviceRelate.setCustomCorp(deviceInfoDto.getCustomCorp());
                    deviceInfoDto.setBranchName(deviceBranchName);
                    customDeviceRelate.setBranchName(deviceBranchName);
                    needInsertDeviceBranch.add(customDeviceRelate);
                } else {
                    deviceInfoDto.setBranchId(deviceBranchMap.entrySet().stream().filter(entry -> deviceBranchName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            }

            // 4，5，6 省市区
            String provinceName = StrUtil.trimToEmpty(deviceExcelDto.getProvinceName());
            String cityName = StrUtil.trimToEmpty(deviceExcelDto.getCityName());
            String districtName = StrUtil.trimToEmpty(deviceExcelDto.getDistrictName());
            if (!StringUtils.isEmpty(provinceName)) {
                if (!areaMap.containsValue(provinceName)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNum, "省").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setDistrict(areaMap.entrySet().stream().filter(entry -> provinceName.equals(entry.getValue()))
                        .findAny().orElse(null).getKey());
                }
            } else {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EMPTY.fillArgs(rowNum, "省").getMsg()).append("<br>");
            }
            if (!StringUtils.isEmpty(cityName)) {
                if (!areaMap.containsValue(cityName)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNum, "市").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setDistrict(areaMap.entrySet().stream().filter(entry -> cityName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            }
            if (!StringUtils.isEmpty(districtName)) {
                if (!areaMap.containsValue(districtName)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNum, "区").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setDistrict(areaMap.entrySet().stream().filter(entry -> districtName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            }

            // 7，详细地址
            String address = StrUtil.trimToEmpty(deviceExcelDto.getAddress());
            if (!StringUtils.isEmpty(address)) {
                if (address.trim().length() > 100) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "详细地址", 100).getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setAddress(address);
                }
            }

            // 8，校验分布
            String zoneName = StrUtil.trimToEmpty(deviceExcelDto.getZoneName());
            if (!StringUtils.isEmpty(zoneName)) {
                if ("市区".equals(zoneName)) {
                    deviceInfoDto.setZone(1);
                } else if ("郊县".equals(zoneName)) {
                    deviceInfoDto.setZone(2);
                } else {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_CHOOSE.fillArgs(rowNum, "分布", "【市区】或者【郊县】").getMsg()).append("<br>");
                }
            }

            // 9，校验服务商名称
            String serviceCorpName = StrUtil.trimToEmpty(deviceExcelDto.getServiceCorpName());
            if (!StringUtils.isEmpty(serviceCorpName)) {
                if (!serviceCorpMap.containsValue(serviceCorpName)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNum, "服务商名称").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setServiceCorp(serviceCorpMap.entrySet().stream().filter(entry -> serviceCorpName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            }

            // 10，校验设备大类
            String largeClassName = StrUtil.trimToEmpty(deviceExcelDto.getLargeClassName());
            if (!StringUtils.isEmpty(largeClassName)) {
                if (!largeClassMap.containsValue(largeClassName)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNum, "所属大类").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setLargeClassId(largeClassMap.entrySet().stream().filter(entry -> largeClassName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            }

            // 11，校验设备小类
            String smallClassName = StrUtil.trimToEmpty(deviceExcelDto.getSmallClassName());
            if (!StringUtils.isEmpty(smallClassName)) {
                if (!smallClassMap.containsValue(smallClassName)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNum, "设备类型").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setSmallClassId(smallClassMap.entrySet().stream().filter(entry -> smallClassName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            } else {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EMPTY.fillArgs(rowNum, "设备类型").getMsg()).append("<br>");
            }

            // 12，校验设备规格
            String specificationName = StrUtil.trimToEmpty(deviceExcelDto.getSpecificationName());
            if (!StringUtils.isEmpty(specificationName)) {
                if (!specificationMap.containsValue(specificationName)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNum, "设备规格").getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setSpecificationId(specificationMap.entrySet().stream()
                            .filter(entry -> specificationName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            }

            // 13， 校验设备品牌
            String brandName = StrUtil.trimToEmpty(deviceExcelDto.getBrandName());
            if (!StringUtils.isEmpty(brandName)) {
                if (brandMap.containsValue(brandName)) {
                    deviceInfoDto.setBrandId(brandMap.entrySet().stream().filter(entry -> brandName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                }
            } else {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EMPTY.fillArgs(rowNum, "品牌").getMsg()).append("<br>");
            }

            // 14， 校验设备型号
            String modelName = StrUtil.trimToEmpty(deviceExcelDto.getModelName());
            if (!StringUtils.isEmpty(modelName)) {
                if (!modelIdAndNameMap.containsValue(modelName)) {
                    DeviceModel deviceModel = new DeviceModel();
                    deviceModel.setCorp(demanderCorp);
                    deviceModel.setSmallClassId(deviceInfoDto.getSmallClassId());
                    deviceModel.setName(modelName);
                    deviceModel.setCorp(demanderCorp);
                    deviceModel.setBrandId(deviceInfoDto.getBrandId());
                    needInsertDeviceMode.add(deviceModel);
                    infoDtoDeviceModelMap.put(deviceInfoDto,deviceModel);
                } else {
                    deviceInfoDto.setModelId(modelIdAndNameMap.entrySet().stream().filter(entry -> modelName.equals(entry.getValue()))
                            .findAny().orElse(null).getKey());
                    DeviceModel deviceModel = modelMap.get(deviceInfoDto.getModelId());
                    if (deviceModel != null) {
                        // 根据型号设置小类和品牌
                        deviceInfoDto.setBrandId(deviceModel.getBrandId());
                        deviceInfoDto.setSmallClassId(deviceModel.getSmallClassId());
                    }
                }
            } else {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EMPTY.fillArgs(rowNum, "设备型号").getMsg()).append("<br>");
            }

            // 15，校验出厂序列号
            String serial = StrUtil.trimToEmpty(deviceExcelDto.getSerial());
            if (!StringUtils.isEmpty(serial)) {
                if (serial.trim().length() > 20) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "出厂序列号", 100).getMsg()).append("<br>");
                }
                deviceInfoDto.setSerial(serial.toUpperCase());
            }

            // 16，自定义唯一编号
            String deviceCode = StrUtil.trimToEmpty(deviceExcelDto.getDeviceCode());
            if (!StringUtils.isEmpty(deviceCode)) {
                if (deviceCode.trim().length() > 20) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "自定义唯一编号", 50).getMsg()).append("<br>");
                }
                deviceInfoDto.setDeviceCode(deviceCode);
            }

            // 17，设备状态
            String statusName = StrUtil.trimToEmpty(deviceExcelDto.getStatusName());
            if (!StringUtils.isEmpty(statusName)) {
                for (DeviceStatusEnum deviceStatusEnum : DeviceStatusEnum.values()) {
                    if (statusName.equals(deviceStatusEnum.getName())) {
                        deviceInfoDto.setStatus(deviceStatusEnum.getCode());
                        break;
                    }
                }
                if (IntUtil.isZero(deviceInfoDto.getStatus())) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_CHOOSE.fillArgs(rowNum, "设备状态", "【运行】、【暂停】或者【死亡】").getMsg()).append("<br>");
                }
            }

            // 18， 校验出厂日期
            String factoryDate = StrUtil.trimToEmpty(deviceExcelDto.getFactoryDate());
            if (!StringUtils.isEmpty(factoryDate)) {
                if (parseDate(factoryDate, dateFormat) == null) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNum, "出厂日期", DATE_FORMAT).getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setFactoryDate(parseDate(factoryDate, dateFormat));
                }
            }

            // 19， 校验购买日期
            String purchaseDate = StrUtil.trimToEmpty(deviceExcelDto.getPurchaseDate());
            if (!StringUtils.isEmpty(purchaseDate)) {
                if (parseDate(purchaseDate, dateFormat) == null) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNum, "购买日期", DATE_FORMAT).getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setPurchaseDate(parseDate(purchaseDate, dateFormat));
                }
            }

            // 20，校验安装日期
            String installDate = StrUtil.trimToEmpty(deviceExcelDto.getInstallDate());
            if (!StringUtils.isEmpty(installDate)) {
                if (parseDate(installDate, dateFormat) == null) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNum, "安装日期", DATE_FORMAT).getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setInstallDate(parseDate(installDate, dateFormat));
                }
            }

            // 21，校验设备描述
            String description = StrUtil.trimToEmpty(deviceExcelDto.getDescription());
            if (!StringUtils.isEmpty(description) && description.trim().length() > 200) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "设备描述", 200).getMsg()).append("<br>");
            } else {
                deviceInfoDto.setDescription(description);
            }

            // 22，校验维保方式
            String warrantyModeName = StrUtil.trimToEmpty(deviceExcelDto.getWarrantyModeName());
            if (!StringUtils.isEmpty(warrantyModeName)) {
                if ("整机保".equals(warrantyModeName)) {
                    deviceInfoDto.setWarrantyMode(10);
                } else if ("单次保".equals(warrantyModeName)) {
                    deviceInfoDto.setWarrantyMode(20);
                } else {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_CHOOSE.fillArgs(rowNum, "维保方式", "【单次保】或【整机保】").getMsg()).append("<br>");
                }
            }

            // 23，校验维保合同号
            String contNo = StrUtil.trimToEmpty(deviceExcelDto.getContNo());
            if (!StringUtils.isEmpty(contNo)) {
                if (contNo.length() > 30) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "维保合同号", 30).getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setContNo(contNo);
                }
            }

            // 24， 校验保修起始日期
            String warrantyStartDate = deviceExcelDto.getWarrantyStartDate();
            if (!StringUtils.isEmpty(warrantyStartDate)) {
                if (parseDate(warrantyStartDate, dateFormat) == null) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNum, "保修起始日期", DATE_FORMAT).getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setWarrantyStartDate(parseDate(warrantyStartDate, dateFormat));
                }
            }

            // 25， 校验保修截止日期
            String warrantyEndDate = StrUtil.trimToEmpty(deviceExcelDto.getWarrantyEndDate());
            if (!StringUtils.isEmpty(warrantyEndDate)) {
                if (parseDate(warrantyEndDate, dateFormat) == null) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNum, "保修截止日期", DATE_FORMAT).getMsg()).append("<br>");
                } else {
                    deviceInfoDto.setWarrantyEndDate(parseDate(warrantyEndDate, dateFormat));
                }
            }

            // 26，校验保修状态
            String warrantyStatusName = StrUtil.trimToEmpty(deviceExcelDto.getWarrantyStatusName());
            if (!StringUtils.isEmpty(warrantyStatusName)) {
                for (WarrantyEnum warrantyEnum : WarrantyEnum.values()) {
                    if (warrantyStatusName.equals(warrantyEnum.getName())) {
                        deviceInfoDto.setWarrantyStatus(warrantyEnum.getCode());
                        break;
                    }
                }
                if (IntUtil.isZero(deviceInfoDto.getWarrantyStatus())) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_CHOOSE.fillArgs(rowNum, "保修状态", "【保内】或者【保外】").getMsg()).append("<br>");
                }
            }

            // 27，校验保修说明
            String warrantyNote = StrUtil.trimToEmpty(deviceExcelDto.getWarrantyNote());
            if (!StringUtils.isEmpty(warrantyNote) && warrantyNote.trim().length() > 200) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "保修说明", 200).getMsg()).append("<br>");
            } else {
                deviceInfoDto.setWarrantyNote(warrantyNote);
            }

            // 校验序列号和型号是否有重复
            if (!StringUtils.isEmpty(deviceInfoDto.getSerial()) && LongUtil.isNotZero(deviceInfoDto.getModelId())) {
                String key = deviceInfoDto.getSerial() + deviceInfoDto.getModelId();
                List<Integer> rowNumList = repeatCheckMap.get(key);
                if (rowNumList == null) {
                    rowNumList = new ArrayList<>();
                }
                rowNumList.add(rowNum);
                repeatCheckMap.put(key, rowNumList);

                // 校验数据库中是否已存在同型号同序列号的设备
                if (existCheckMap.containsKey(key)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_ALREADY_EXISTS.fillArgs(rowNum, "【设备型号】和【序列号】").getMsg()).append("<br>");
                }
            }

            deviceInfoList.add(deviceInfoDto);

        }

        // 校验重复数据
        repeatCheckMap.entrySet().forEach(entry -> {
            // 有重复数据
            if (entry.getValue() != null && entry.getValue().size() > 1) {
                String rowNums = entry.getValue().toString();
                sb.append(ExcelCodeMsg.IMPORT_DUPLICATE_FIELD_ERROR.fillArgs(rowNums, "【设备型号】和【序列号】").getMsg()).append("<br>");
            }
        });


        if (StringUtils.isEmpty(sb.toString())) {
            needInsertMap.put("customCorpName",needInsertCustomCorp);
            needInsertMap.put("customDevice",needInsertDeviceBranch);
            needInsertMap.put("deviceModel",needInsertDeviceMode);
            needInsertMap.put("infoDeviceModel",infoDtoDeviceModelMap);
            this.deviceInfoService.batchSaveDeviceInfo(deviceInfoList, userInfo, demanderCorp, needInsertMap);
        }

        return sb.toString();

    }

    @Override
    public List<DeviceExportDto> getDeviceExcelList(DeviceInfoFilter deviceInfoFilter, UserInfo userInfo, ReqParam reqParam) {
        deviceInfoFilter.setPageSize(1000000);
        deviceInfoFilter.setPageNum(1);
        List<DeviceInfoDto> list = deviceInfoService.queryDeviceInfo(deviceInfoFilter,userInfo,reqParam).getList();
        List<DeviceExportDto> deviceExcelDtoList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(list)) {
             deviceExcelDtoList = new ArrayList<>();
            for(DeviceInfoDto deviceInfoDto : list) {
                DeviceExportDto deviceExportDto = new DeviceExportDto();
                BeanUtils.copyProperties(deviceInfoDto,deviceExportDto);
                if(deviceInfoDto.getInstallDate() != null) {
                    deviceExportDto.setInstallDate(FAULT_DATE_FORMAT.format(deviceInfoDto.getInstallDate()));
                }
                if(deviceInfoDto.getWarrantyEndDate() !=null ){
                    deviceExportDto.setWarrantyEndDate(FAULT_DATE_FORMAT.format(deviceInfoDto.getWarrantyEndDate()));
                }
                if(deviceInfoDto.getWarrantyStartDate() != null) {
                    deviceExportDto.setWarrantyStartDate(FAULT_DATE_FORMAT.format(deviceInfoDto.getWarrantyStartDate()));
                }
                if(deviceInfoDto.getPurchaseDate() != null) {
                    deviceExportDto.setPurchaseDate(FAULT_DATE_FORMAT.format(deviceInfoDto.getPurchaseDate()));
                }
                if(deviceInfoDto.getFactoryDate() != null) {
                    deviceExportDto.setFactoryDate(FAULT_DATE_FORMAT.format(deviceInfoDto.getFactoryDate()));
                }
//                deviceInfoDto.setStatusName(DeviceStatusEnum.getNameByCode(deviceInfoDto.getStatus()));
                deviceExcelDtoList.add(deviceExportDto);
            }
        }
        return deviceExcelDtoList;
    }

    /**
     * 字符串转为日期，不能转则返回null
     *
     * @param dateString
     * @param dateFormat
     * @return
     */
    public static Date parseDate(String dateString, SimpleDateFormat dateFormat) {
        try {
            dateFormat.setLenient(false);
            return dateFormat.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }

}
