package com.zjft.usp.anyfix.work.request.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.anyfix.baseinfo.filter.FaultTypeFilter;
import com.zjft.usp.anyfix.baseinfo.filter.WorkTypeFilter;
import com.zjft.usp.anyfix.baseinfo.model.FaultType;
import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import com.zjft.usp.anyfix.baseinfo.service.FaultTypeService;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.listener.WorkMqTopic;
import com.zjft.usp.anyfix.work.request.composite.WorkRequestCompoService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestExcelDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestExportDto;
import com.zjft.usp.anyfix.work.request.enums.ExcelCodeMsg;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.service.WorkRequestExcelService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 工单excel服务
 * @author ljzhu
 * @since 2020-02-25
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkRequestExcelServiceImpl implements WorkRequestExcelService {

    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private DemanderService demanderService;
    @Autowired
    private WorkDealService workDealService;

    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private FaultTypeService faultTypeService;

    @Resource
    private DeviceFeignService deviceFeignService;

    @Resource
    private DeviceBranchService deviceBranchService;

    @Resource
    private UasFeignService uasFeignService;

    @Autowired
    private WorkRequestCompoService workRequestCompoService;

    @Autowired
    private MqSenderUtil mqSenderUtil;

    public static final String BOOK_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";
    public static final String FAULT_DATE_FORMAT_STR = "yyyy-MM-dd HH:mm";


    public static final DateFormat BOOK_DATE_FORMAT = new SimpleDateFormat(BOOK_DATE_FORMAT_STR);

    public static final DateFormat FAULT_DATE_FORMAT = new SimpleDateFormat(FAULT_DATE_FORMAT_STR);

    public static final String REGEX_MOBILEPHONE = "^[1][0-9]{10}$";

    /**
     * 验证带区号的
     */
    public static final String REGEX_PHONE_QH = "^[0][1-9]{2,3}-[0-9]{5,10}$";
    /**
     * 验证没有区号的
     */
    public static final String REGEX_PHONE = "^[1-9]{1}[0-9]{5,8}$";


    public static final String[] SOURCELIST = new String[]{"微信","网页","电话","邮件"};


    /**
     * 验证包含字母
     */
    public static final Pattern P_WORD = Pattern.compile(".*[a-zA-z].*");
    /**
     * 验证包含数字
     */
    public static final Pattern P_NUMBER = Pattern.compile(".*\\d+.*");

    private List<WorkDeal> workDealList = null;
    private Map<Long, String> nameMap = null;

    @Override
    public List<Long> checkImportData(List<WorkRequestExcelDto> workRequestExcelDtoList,UserInfo userInfo,ReqParam reqParam) {
        int rowNo = 1;
        // 遍历校验所有行和列
        StringBuilder sb = new StringBuilder();
        Long demanderCorp = this.getExcelDemanderDto(workRequestExcelDtoList,reqParam.getCorpId());

        // 委托商数据错误 直接返回不处理
        if(demanderCorp == 0){
            sb.append(ExcelCodeMsg.IMPORT_FIELD_DEMANDER.fillArgs());
            throw new AppException(sb.toString());
        }
        List<DemanderCustom> customDtos = this.demanderCustomService.listCustomByCorpId(demanderCorp);

        Map<String,String> areaCodeNameMap = uasFeignService.mapAreaCodeAndName().getData();

        List<WorkRequestDto> workRequestDtoList = new ArrayList<>();

        //用来校验Excel内的重复数据，key为 品牌名+序列号+工单类型，value为行号list
        Map<String, List<Integer>> repeatCheckMap = new HashMap<>();
        //获取该委托商对应的工单类型映射
        Map<Integer, WorkType> workTypeMap = workTypeService.mapWorkTypeObj(demanderCorp);

        for (WorkRequestExcelDto workRequestExcelDto : workRequestExcelDtoList) {
            // 行号从第2行开始
            rowNo++;

            WorkRequestDto workRequestDto = new WorkRequestDto();


            // 1.委托商
            workRequestDto.setDemanderCorp(demanderCorp);
            workRequestDto.setCustomCorp(0L);

            // 2.委托单号
            String checkWorkCode = StrUtil.trimToEmpty(workRequestExcelDto.getCheckWorkCode());
            if (StringUtils.isEmpty(checkWorkCode)) {
//                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "委托单号",""));
            } else if (checkWorkCode.trim().length() > 20) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "委托单号",20));
            }else{
                workRequestDto.setCheckWorkCode(checkWorkCode);
            }
            // 3.*客户名称
            String customCorpName = StrUtil.trimToEmpty(workRequestExcelDto.getCustomCorpName());
            if (StringUtils.isEmpty(customCorpName)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "客户名称",""));
            } else if (customCorpName.trim().length() > 50) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "客户名称",50));
            }else{
                workRequestDto.setCustomCorpName(customCorpName);
            }
            DemanderCustom demanderCustom = customDtos.stream().filter(o ->
                    o.getCustomCorpName().equals(customCorpName)).findAny().orElse(null);

            if (demanderCustom == null) {
//                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NOTEXIT.fillArgs(rowNo, "客户名称"));
                workRequestDto.setCustomId(0L);
            } else {
                workRequestDto.setCustomId(demanderCustom.getCustomId());
            }
            // 4.设备网点
            String deviceBranchName = StrUtil.trimToEmpty(workRequestExcelDto.getDeviceBranchName());
            if (!StringUtils.isEmpty(deviceBranchName)) {
                if (deviceBranchName.trim().length() > 50) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "设备网点",50));
                }else{
                    workRequestDto.setDeviceBranchName(deviceBranchName);
                    // 客户准确 验证设备网点准确性
                    if (workRequestDto.getCustomId() != null && workRequestDto.getCustomId() > 0) {
                        Long deviceBranchId = this.getDeviceBranchId(workRequestDto.getCustomId(),deviceBranchName);
                        if (deviceBranchId > 0) {
                            workRequestDto.setDeviceBranch(deviceBranchId);
                        }
                    }else{
                        workRequestDto.setDeviceBranch(0L);
                    }
                }
            }
            // 5.*联系人
            String contactName = StrUtil.trimToEmpty(workRequestExcelDto.getContactName());
            if (StringUtils.isEmpty(contactName)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "联系人",""));
            } else if (contactName.trim().length() > 20) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "联系人",20));
            }else{
                workRequestDto.setContactName(contactName);
            }

            // 6.*联系电话
            String contactPhone = StrUtil.trimToEmpty(workRequestExcelDto.getContactPhone());
            if (StringUtils.isEmpty(contactPhone)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "联系电话","可以是电话或者座机"));
            } else if (contactPhone.trim().length() > 20) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "联系电话",20));
            }else if(!Pattern.matches(REGEX_PHONE,contactPhone) && !Pattern.matches(REGEX_PHONE_QH,contactPhone) &&
                    !Pattern.matches(REGEX_MOBILEPHONE,contactPhone)){
                sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "联系电话"));
            }else{
                workRequestDto.setContactPhone(contactPhone);
            }

            // 7.*省
            // 8.*市
            // 9.区
            String provinceName = StrUtil.trimToEmpty(workRequestExcelDto.getProvinceName());
            String cityName = StrUtil.trimToEmpty(workRequestExcelDto.getCityName());
            String districtName = StrUtil.trimToEmpty(workRequestExcelDto.getDistrictName());

            String province = "";
            String city = "";
            String district = "";
            if (StringUtils.isEmpty(provinceName)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "省",""));
            } else {
                Map.Entry<String, String> provinceMap = areaCodeNameMap.entrySet().stream().
                        filter(e -> e.getValue().equals(provinceName)).findAny().orElse(null);
                if(provinceMap == null){
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "省"));
                }else{
                    province = provinceMap.getKey();

                    String provinceCode = province;
                    if (!StringUtils.isEmpty(cityName)) {
                        Map.Entry<String, String> cityMap = areaCodeNameMap.entrySet().stream().
                                filter(e -> (e.getValue().equals(cityName) && e.getKey().indexOf(provinceCode) > -1))
                                .findAny().orElse(null);
                        if(cityMap == null){
                            sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "市"));
                        }else{
                            city = cityMap.getKey();

                            String cityCode = (city != null && city.length() > 0) ? city : ((province != null && province.length() > 0) ? province : "");
                            if (!StringUtils.isEmpty(districtName)) {
                                Map.Entry<String, String> districtMap = areaCodeNameMap.entrySet().stream().
                                        filter(e -> e.getValue().equals(districtName) && e.getKey().indexOf(cityCode) > -1)
                                        .findAny().orElse(null);
                                if(districtMap == null){
                                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "区"));
                                }else{
                                    district = districtMap.getKey();
                                }
                            }
                        }
                    } else {
                        sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "市",""));
                    }
                }
            }

            if(!StringUtils.isEmpty(district)){
                workRequestDto.setDistrict(district);
            }else if(!StringUtils.isEmpty(city)){
                workRequestDto.setDistrict(city);
            }else if(!StringUtils.isEmpty(province)){
                workRequestDto.setDistrict(province);
            }

            // 10.*详细地址
            String address = StrUtil.trimToEmpty(workRequestExcelDto.getAddress());
            if (StringUtils.isEmpty(address)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "详细地址",""));
            } else if (address.trim().length() > 200) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "详细地址",200));
            }else{
                workRequestDto.setAddress(address);
            }

            // 10.分布
            String zoneName = StrUtil.trimToEmpty(workRequestExcelDto.getZoneName());
            if (!StringUtils.isEmpty(zoneName)) {
                if("市区".equals(zoneName) || "郊县".equals(zoneName)){
                    workRequestDto.setZone("市区".equals(zoneName) ? 1 : 2);
                }else{
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "分布"));
                }
            }
            // 11.设备大类
//            String largeClassName = StrUtil.trimToEmpty(workRequestExcelDto.getLargeClassName());
//            if (!StringUtils.isEmpty(largeClassName)) {
//                Long largeClassId = this.getLargeClassId(demanderCorp,largeClassName);
//                if (largeClassId == 0) {
//                    sb.append(ExcelCodeMsg.IMPORT_FIELD_NOT_MATCH_DEMANDER.fillArgs(rowNo, "设备大类"));
//                } else {
//                    // workRequestDto(largeClassId);
//                }
//            }
            // 12.设备类型
            String smallClassName = StrUtil.trimToEmpty(workRequestExcelDto.getSmallClassName());
            if (!StringUtils.isEmpty(smallClassName)) {
                Long smallClassId = this.getSmallClassId(demanderCorp,smallClassName);
                if (smallClassId == 0) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_NOT_MATCH_DEMANDER.fillArgs(rowNo, "设备小类"));
                } else {
                    workRequestDto.setSmallClass(smallClassId);
                }

            }

            // 13.设备规格
            String specificationName = StrUtil.trimToEmpty(workRequestExcelDto.getSpecificationName());
            if (!StringUtils.isEmpty(specificationName)) {
                Long specification = this.getSpecification(demanderCorp,specificationName);
                if (specification == 0) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_NOT_MATCH_DEMANDER.fillArgs(rowNo, "设备规格"));
                } else {
                    workRequestDto.setSpecification(specification);
                }
            }


            // 14.设备品牌
            String brandName = StrUtil.trimToEmpty(workRequestExcelDto.getBrandName());
            if (!StringUtils.isEmpty(brandName)) {
                Long brandId = this.getBranchId(demanderCorp,brandName);
                if (brandId == 0) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_NOT_MATCH_DEMANDER.fillArgs(rowNo, "设备品牌"));
                } else {
                    workRequestDto.setBrand(brandId);
                }

            }
            // 15.设备型号
            String modelName = StrUtil.trimToEmpty(workRequestExcelDto.getModelName());
            if (!StringUtils.isEmpty(modelName)) {

                boolean exist = (workRequestDto.getBrand() == null || workRequestDto.getBrand() == 0)
                        && (workRequestDto.getSmallClass() == null || workRequestDto.getSmallClass() == 0);
                if(exist){
                    //型号没有基础数据时默认加入名称
                    if(!P_WORD.matcher(modelName).matches() && !P_NUMBER.matcher(modelName).matches()){
                        sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNo, "设备型号",":必须含有数字或字母"));
                    }else{
                        workRequestDto.setModelName(modelName);
                    }
                    // sb.append(ExcelCodeMsg.IMPORT_FIELD_OTHER_IS_NOTEXIT.fillArgs(rowNo, "设备型号"));

                }else{
                    Long modelId = this.getModelId(demanderCorp,workRequestDto.getBrand() == null ? 0L : workRequestDto.getBrand() ,
                            workRequestDto.getSmallClass() == null ? 0L : workRequestDto.getSmallClass(),modelName);
                    if (modelId == 0) {
                        //型号没有基础数据时默认加入名称
                        if(!P_WORD.matcher(modelName).matches() && !P_NUMBER.matcher(modelName).matches()){
                            sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNo, "设备型号",":必须含有数字或字母"));
                        }else{
                            workRequestDto.setModelName(modelName);
                        }
                        // sb.append(ExcelCodeMsg.IMPORT_FIELD_NOT_MATCH_DEMANDER.fillArgs(rowNo, "设备型号"));
                    } else {
                        workRequestDto.setModel(modelId);
                    }
                }
            }

            // 16.*设备数量

            String deviceNumStr = StrUtil.trimToEmpty(workRequestExcelDto.getDeviceNumStr());
            if (StringUtils.isEmpty(deviceNumStr)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "设备数量","必须为整数"));
            }else if(!NumberUtils.isDigits(deviceNumStr)){
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_NUMBER.fillArgs(rowNo, "设备数量"));
            }else{
                workRequestDto.setDeviceNum(Integer.parseInt(deviceNumStr));
            }

            // 17.出厂序列号
            String serials = StrUtil.trimToEmpty(workRequestExcelDto.getSerials());
            if (!StringUtils.isEmpty(serials)) {
                serials = serials.replaceAll("，",",").toUpperCase();
                String[] serialsArray = serials.split(",");
                if(!P_WORD.matcher(serials).matches() && !P_NUMBER.matcher(serials).matches()){
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNo, "出厂序列号",":必须含有数字或字母"));
                }else{
                    if(workRequestDto.getDeviceNum() != null &&  workRequestDto.getDeviceNum() > 0 &&
                            workRequestDto.getDeviceNum() < serialsArray.length){
                        sb.append(ExcelCodeMsg.IMPORT_FIELD_LOWER_THEN.fillArgs(rowNo, "出厂序列号"));
                    }else{
                        workRequestDto.setSerials(serials);
                    }
                }
            }

            // 18.维保方式
            String warrantyModeName = StrUtil.trimToEmpty(workRequestExcelDto.getWarrantyModeName());
            if (!StringUtils.isEmpty(warrantyModeName)) {
                if("整机保".equals(warrantyModeName) || "单次保".equals(warrantyModeName)){
                    workRequestDto.setWarrantyMode("整机保".equals(warrantyModeName) ? 10 : 20);
                }else{
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMAT_ERROR.fillArgs(rowNo, "维保方式"));
                }
            }
            // 19.*工单类型
            String workTypeName = StrUtil.trimToEmpty(workRequestExcelDto.getWorkTypeName());

            if (StringUtils.isEmpty(workTypeName)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "工单类型",""));

            }else{
                Integer workType = this.getWorkType(demanderCorp,workTypeName);
                if(workType > 0){
                    workRequestDto.setWorkType(workType);
                }else{
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_NOT_MATCH_DEMANDER.fillArgs(rowNo, "工单类型"));
                }
            }

            // 20.故障代码
            String faultCode = StrUtil.trimToEmpty(workRequestExcelDto.getFaultCode());
            if (!StringUtils.isEmpty(faultCode) && faultCode.trim().length() > 50) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "故障代码",50));
            }else{
                workRequestDto.setFaultCode(StrUtil.trimToEmpty(faultCode));
            }
            // 21.故障时间(yyyy-MM-dd HH:mm)
            String faultTimeStr = StrUtil.trimToEmpty(workRequestExcelDto.getFaultTimeStr());
            if (!StringUtils.isEmpty(faultTimeStr)) {

                Date faultTime = this.canParseDate(faultTimeStr,FAULT_DATE_FORMAT);
                if(faultTime == null){
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNo, "故障时间",FAULT_DATE_FORMAT_STR));
                }else{
                    workRequestDto.setFaultTime(faultTime);
                }
            }
            // 21.*故障现象
            String faultTypes = StrUtil.trimToEmpty(workRequestExcelDto.getFaultTypes());
            if (!StringUtils.isEmpty(workTypeName) && "维护".equals(workTypeName)) {
                //维修时需要必填故障现象
                if (StringUtils.isEmpty(faultTypes)){
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "故障现象",",工单类型为维护时故障现象必填," +
                            "没有可填【其他】,在服务请求中详细描述"));
                }else{
                    Map<String,Integer> faultTypeMap = this.getFaultTypeMap(demanderCorp);
                    faultTypes = faultTypes.replaceAll("，",",");
                    String[] faultTypesArray = faultTypes.split(",");
                    boolean isTrue = true;
                    List<Integer> faultTypeIds = new ArrayList<>();
                    for(String f : faultTypesArray){
                        if(faultTypeMap.get(f) == null){
                            isTrue = false;
                            break;
                        }else{
                            if(faultTypeMap.get(f) != 0){
                                faultTypeIds.add(faultTypeMap.get(f));
                            }
                            continue;
                        }
                    }
                    if(!isTrue){
                        sb.append(ExcelCodeMsg.IMPORT_FIELD_NOT_MATCH_DEMANDER.fillArgs(rowNo, "故障现象"));
                    }else {
                        workRequestDto.setFaultTypeList(this.getFaultTypeList(demanderCorp,faultTypeIds));
                    }
                }
            }
            // 22.*服务请求
            String serviceRequest = StrUtil.trimToEmpty(workRequestExcelDto.getServiceRequest());
            if(faultTypes != null && faultTypes.equals("其他")){
            } else {
                serviceRequest = faultTypes + "。" + serviceRequest;
            }
            workRequestDto.setServiceRequest(serviceRequest);

            if (!StringUtils.isEmpty(workTypeName) && "维护".equals(workTypeName)) {
                if (!StringUtils.isEmpty(serviceRequest)) {
                    if(serviceRequest.trim().length() > 500){
                        sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "服务请求",500));
                    }
                }else{//为空判断是否为其他
                    if(faultTypes != null && faultTypes.equals("其他")){
                        sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "服务请求",",【故障现象】为其他的时候服务请求必填"));
                    }
                }
            }else{
                if (StringUtils.isEmpty(serviceRequest)) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "服务请求",""));
                } else if (serviceRequest.trim().length() > 500) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNo, "服务请求",500));
                }
            }

            // 23.预约时间结束(yyyy-MM-dd HH:mm)
            String bookTimeEndStr = StrUtil.trimToEmpty(workRequestExcelDto.getBookTimeEndStr());
            if (!StringUtils.isEmpty(bookTimeEndStr)) {
                Date bookTimeEnd = this.canParseDate(bookTimeEndStr,BOOK_DATE_FORMAT);
                if(bookTimeEnd == null){
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_FORMATDATE_ERROR.fillArgs(rowNo, "预约时间",BOOK_DATE_FORMAT_STR));
                }else{
                    workRequestDto.setBookTimeEnd(bookTimeEnd);
                }
            }

            // 24*工单来源
            String sourceName = StrUtil.trimToEmpty(workRequestExcelDto.getSourceName());
            if (StringUtils.isEmpty(sourceName)) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_EAMPTY.fillArgs(rowNo, "工单来源",""));

            }else{
                List<String> sources = Arrays.asList(SOURCELIST);
                if(sources.contains(sourceName)){
                    workRequestDto.setSource(sources.indexOf(sourceName) + 2);
                }else{
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_CHOOSE.fillArgs(rowNo, "工单来源",String.join(",",SOURCELIST)));
                }

            }

            // 25 费用报价
            String basicServiceFeeStr = StrUtil.trimToEmpty(workRequestExcelDto.getBasicServiceFee());
            if (StringUtils.isEmpty(basicServiceFeeStr)) {
                workRequestDto.setBasicServiceFee(BigDecimal.ZERO);
            } else if (NumberUtils.isNumber(basicServiceFeeStr)) {
                BigDecimal basicServiceFee = new BigDecimal(basicServiceFeeStr);
                if (basicServiceFee == null || basicServiceFee.compareTo(BigDecimal.ZERO) < 0) {
                    sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_POSITIVE_NUMBER.fillArgs(rowNo, "费用报价"));
                }
                // 保留两位小数
                workRequestDto.setBasicServiceFee(basicServiceFee.setScale(2));
            } else {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_POSITIVE_NUMBER.fillArgs(rowNo, "费用报价"));
            }
            workRequestDtoList.add(workRequestDto);
            //校验Excel内数据是否重复
            if(StrUtil.isNotEmpty(workRequestDto.getSerials()) &&
                    LongUtil.isNotZero(workRequestDto.getBrand()) && IntUtil.isNotZero(workRequestDto.getWorkType())){
                List<String> serialList = Arrays.asList(StrUtil.trimToEmpty(workRequestDto.getSerials()).split(","));
                for (String serial : serialList) {
                    WorkType workType = workTypeMap.get(workRequestDto.getWorkType());
                    String key = StrUtil.trimToEmpty(serial) + "-" + workRequestDto.getBrand() + "-" +
                            (workType == null ? "" : workType.getSysType().toString());
                    if(repeatCheckMap.containsKey(key)){
                        repeatCheckMap.get(key).add(rowNo);
                    } else {
                        List<Integer> rowList = new ArrayList<>();
                        rowList.add(rowNo);
                        repeatCheckMap.put(key, rowList);
                    }
                }
            }
        }
        //根据map中的值判断Excel中是否存在重复数据，若是存在则将重复数据的行号拼接在sb中
        for (String key : repeatCheckMap.keySet()) {
            if(CollectionUtil.isNotEmpty(repeatCheckMap.get(key)) && repeatCheckMap.get(key).size() > 1){
                sb.append(ExcelCodeMsg.IMPORT_DUPLICATE_FIELD_ERROR.fillArgs(repeatCheckMap.get(key).toString()));
            }
        }
        if (StrUtil.isNotEmpty(sb.toString())) {
            throw new AppException(sb.toString());
        }
        return this.addWorkRequestDtoList(workRequestDtoList, userInfo, reqParam);
    }

    @Override
    public void sendImportWorkMessage(List<Long> workIdList, String autoHandel){
        if(workIdList != null && workIdList.size() > 0){
            // 如果选择自动提单则触发
            if("Y".equals(StrUtil.trimToEmpty(autoHandel))){
                Map<String, Object> msg;
                for (Long workId : workIdList) {
                    msg = new HashMap<>(1);
                    msg.put("workId", workId);
                    mqSenderUtil.sendMessage(WorkMqTopic.CREATE_WORK, JsonUtil.toJson(msg));
                }
            }
        }
    }


    /**
     *  调用新建工单服务批量插入工单数据
     * @param workRequestDtoList
     * @param userInfo
     * @param reqParam
     * @return 返回工单编号列表
     */
    private List<Long> addWorkRequestDtoList(List<WorkRequestDto> workRequestDtoList,UserInfo userInfo,ReqParam reqParam){
        // 5.入库操作
        List<Long> idList = new ArrayList<>();
        for(WorkRequestDto workRequestDto : workRequestDtoList){
            List<Long> workIdList = workRequestCompoService.addWorkRequest(workRequestDto, userInfo, reqParam);
            idList.addAll(workIdList);
        }
        return idList;
    }


    /**
     * 获取故障类型
     * @param demanderCorp
     * @return
     */
    private Map<String,Integer> getFaultTypeMap(Long demanderCorp){
        List<FaultType> faultTypes = this.faultTypeService.listEnableFaultTypeByCorp(demanderCorp);
        if(faultTypes == null || faultTypes.size() == 0){
            faultTypes = new ArrayList<>();
        }
        FaultType faultType = new FaultType();
        faultType.setId(0);
        faultType.setName("其他");
        faultTypes.add(faultType);
        Map<String,Integer> map = faultTypes.stream().collect(Collectors.
                toMap(item -> item.getName(), item ->item.getId(), (oldVal, currVal) -> oldVal));
        return map;
    }


    /**
     * 获取故障编号list
     * @param demanderCorp
     * @param faultTypeIds
     * @return
     */
    private List<FaultType> getFaultTypeList(Long demanderCorp,List<Integer> faultTypeIds){
        FaultTypeFilter faultTypeFilter = new FaultTypeFilter();
        faultTypeFilter.setDemanderCorp(demanderCorp);
        List<FaultType> faultTypes = this.faultTypeService.listEnableFaultTypeByCorp(demanderCorp);

        List<FaultType> faultTypesOwn = faultTypes.stream().filter(e-> faultTypeIds.contains(e.getId())).collect(Collectors.toList());
        return faultTypesOwn;
    }

    /**
     * 获取转换的日期
     * @param timeStr
     * @param format
     * @return
     */
    private Date canParseDate(String timeStr,DateFormat format){
        try {
            Date time = format.parse(timeStr);
            return time;
        } catch (ParseException e) {
            return null;
        }
    }
    /**
     * 获取本次excel的委托商
     * @param corpId
     * @return
     */
    private Long getExcelDemanderDto(List<WorkRequestExcelDto> workRequestExcelDtoList,Long corpId){
        //判断所有委托商名称是否一致
        boolean isAllSame = true;
        for(int i = 0;i < workRequestExcelDtoList.size() - 1;i++){
            if(!StringUtils.isEmpty(workRequestExcelDtoList.get(i).getDemanderCorpName()) &&
                    !StringUtils.isEmpty(workRequestExcelDtoList.get(i+1).getDemanderCorpName()) &&
                    workRequestExcelDtoList.get(i).getDemanderCorpName().equals(workRequestExcelDtoList.get(i+1).getDemanderCorpName())){
                continue;
            }else {
                isAllSame = false;
                break;
            }
        }
        if(isAllSame){
            List<DemanderDto> demanderDtos = demanderService.listDemander(corpId);
            DemanderDto demanderDto = demanderDtos.stream().filter(o ->
                    o.getDemanderCorpName().equals(workRequestExcelDtoList.get(0).getDemanderCorpName())).findAny().orElse(null);
            if (demanderDto != null) {
                return demanderDto.getDemanderCorp();
            }
        }
        return 0L;
    }


    /**
     * 获取设备网点数据
     * @param customcorp
     * @param deviceBranchName
     * @return
     */
    private Long getDeviceBranchId(Long customcorp,String deviceBranchName){
        List<Long> corpIds = new ArrayList<>();
        corpIds.add(customcorp);
        Map<Long,String> deviceBranchNames = this.deviceBranchService.mapCustomDeviceBranchByCustomIdList(corpIds);
       return this.getKeyFromMapValue(deviceBranchNames,deviceBranchName);
    }


    /**
     * 获取设备大类
     * @param demanderCorp
     * @param largeClassName
     * @return
     */
    private Long getLargeClassId(Long demanderCorp,String largeClassName){

        Map<Long, String> largeClassMap = deviceFeignService.mapLargeClassByCorp(demanderCorp).getData();

        return this.getKeyFromMapValue(largeClassMap,largeClassName);


    }

    /**
     * 获取设备小类
     * @param demanderCorp
     * @param smallClassName
     * @return
     */
    private Long getSmallClassId(Long demanderCorp,String smallClassName){

        Map<Long, String> samllClassMap = deviceFeignService.getDeviceClassMap(demanderCorp).getData();

        return this.getKeyFromMapValue(samllClassMap,smallClassName);


    }

    /**
     * 获取品牌
     * @param demanderCorp
     * @param branchName
     * @return
     */
    private Long getBranchId(Long demanderCorp,String branchName){

        Map<Long, String> branchMap = deviceFeignService.mapDeviceBrandByCorp(demanderCorp).getData();

        return this.getKeyFromMapValue(branchMap,branchName);


    }

    /**
     * 获取型号
     * @param demanderCorp
     * @param modelName
     * @return
     */
    private Long getModelId(Long demanderCorp,Long brandId,Long smallClassId,String modelName){
        Map<Long, String> modelMap = deviceFeignService.mapByCorpIdBrandIdSmallClassId(demanderCorp,brandId,smallClassId).getData();
        return this.getKeyFromMapValue(modelMap,modelName);
    }

    /**
     * 获取设备规格
     * @param demanderCorp
     * @param specificationName
     * @return
     */
    public Long getSpecification(Long demanderCorp,String specificationName){
        Map<Long, String> specificationMap = deviceFeignService.mapSpecificationByCorp(demanderCorp).getData();
        return this.getKeyFromMapValue(specificationMap,specificationName);

    }

    /**
     * 获取工单类型编号
     * @param demanderCorp
     * @param workTypeName
     * @return
     */
    private Integer getWorkType(Long demanderCorp, String workTypeName){

        WorkTypeFilter workTypeFilter = new WorkTypeFilter();
        workTypeFilter.setEnabled("Y");
        workTypeFilter.setDemanderCorp(demanderCorp);
        WorkType workType = this.workTypeService.query(workTypeFilter).getList()
                .stream().filter(e->e.getName().equals(workTypeName)).findAny().orElse(null);

        if(workType != null){
            return workType.getId();
        }
        return 0;
    }


    private Long getKeyFromMapValue(Map<Long, String> map,String value){
        if(map != null && map.size() > 0){
            List<Long> modelIds = map.entrySet().stream()
                    .filter(kvEntry -> Objects.equals(kvEntry.getValue(), value))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            if(modelIds != null && modelIds.size() >= 1){
                return modelIds.get(0);
            }
        }
        return 0L;
    }


    @Override
    public Map<Integer,String []> getMapDropDown(long demanderCorp){

          //客户可以只填名称来新建
//        List<DemanderCustom> demanderCustomDtos = this.demanderCustomService.listCustomByCorpId(demanderCorp);
//        List<String> customCorp = demanderCustomDtos.stream().map(e -> e.getCustomCorpName()).collect(Collectors.toList());

        String [] zone = new String[]{"市区","郊县"};

        Map<Long, String> largeClassMap = deviceFeignService.getDeviceClassMap(demanderCorp).getData();
        List<String> largeClass = largeClassMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());


        Map<Long, String> brandMap = deviceFeignService.mapDeviceBrandByCorp(demanderCorp).getData();
        List<String> brand = brandMap.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());


        String [] warrantyMode = new String[]{"单次保","整机保"};

        WorkTypeFilter workTypeFilter = new WorkTypeFilter();
        workTypeFilter.setEnabled("Y");
        workTypeFilter.setDemanderCorp(demanderCorp);
        List<String> workTypes = this.workTypeService.query(workTypeFilter).getList().stream().map(e -> e.getName()).collect(Collectors.toList());


//        FaultTypeFilter faultTypeFilter = new FaultTypeFilter();
//        faultTypeFilter.setDemanderCorp(demanderCorp);
//        List<String> faultTypes = this.faultTypeService.query(faultTypeFilter).getList().stream().map(e -> e.getName()).collect(Collectors.toList());
//        faultTypes.add("其他");


        Map<Integer,String []> mapDropDown = new HashMap<>();

        //mapDropDown.put(2,customCorp.toArray(new String[customCorp.size()]));
        mapDropDown.put(10,zone);
        if(largeClass != null && largeClass.size() > 0){
            mapDropDown.put(11,largeClass.toArray(new String[largeClass.size()]));
        }
        if(brand != null && brand.size() > 0){
            mapDropDown.put(13,brand.toArray(new String[brand.size()]));
        }
        mapDropDown.put(17,warrantyMode);
        if(workTypes != null && workTypes.size() > 0) {
            mapDropDown.put(18,workTypes.toArray(new String[workTypes.size()]));
        }
        //mapDropDown.put(22,faultTypes.toArray(new String[faultTypes.size()]));
        mapDropDown.put(24,SOURCELIST);

        return mapDropDown;

    }

    @Override
    public List<WorkRequestExcelDto> getTemplateWorkRequestExcelDtoList(Long demanderCorp, String demanderCorpName){
        List<WorkRequestExcelDto> workRequestExcelDtoList = new ArrayList<>();

        Calendar today = Calendar.getInstance();

        today.add(Calendar.DAY_OF_MONTH, 1);
        String bookTimeEnd = BOOK_DATE_FORMAT.format(today.getTime());

        Map<Long, String> largeClassMap = deviceFeignService.getDeviceClassMap(demanderCorp).getData();

        String largeClass = null;
        for (Map.Entry<Long, String> entry : largeClassMap.entrySet()) {
            largeClass = entry.getValue();
        }
        Map<Long, String> brandMap = deviceFeignService.mapDeviceBrandByCorp(demanderCorp).getData();
        String brand = null;
        for (Map.Entry<Long, String> entry : brandMap.entrySet()) {
            brand = entry.getValue();
        }
        WorkRequestExcelDto workRequestExcelDto1 = WorkRequestExcelDto.builder()
                .demanderCorpName(demanderCorpName)
                .checkWorkCode("委托单号1")
                .customCorpName("客户名称1")
                .deviceBranchName("网点名称1")
                .contactName("张三")
                .contactPhone("153XXXXXXXX")
                .provinceName("江苏省")
                .cityName("南京市")
                .districtName("建邺区")
                .address("XXX路XXXX号")
                .zoneName("市区")
                .smallClassName(largeClass)
                .specificationName("")
                .brandName(brand)
                .modelName("")
                .deviceNumStr("2")
                .serials("11111,22345")
                .warrantyModeName("单次保")
                .workTypeName("维护")
                .faultCode("xxxx")
                .faultTimeStr("2020-02-26 10:20")
                .faultTypes("蓝屏,自动反复重启")
                .serviceRequest("")
                .bookTimeEndStr(bookTimeEnd)
                .sourceName("电话").build();
        WorkRequestExcelDto workRequestExcelDto2 = WorkRequestExcelDto.builder()
                .demanderCorpName(demanderCorpName)
                .checkWorkCode("委托单号2")
                .customCorpName("客户名称2")
                .deviceBranchName("")
                .contactName("李四")
                .contactPhone("021-XXXXXXXX")
                .provinceName("广东省")
                .cityName("深圳市")
                .districtName("南山区")
                .address("XXX路XXXX号")
                .zoneName("郊县")
                .smallClassName("")
                .specificationName("")
                .brandName("")
                .modelName("")
                .deviceNumStr("1")
                .serials("")
                .warrantyModeName("单次保")
                .workTypeName("维护")
                .faultCode("xxxx")
                .faultTimeStr("2020-02-26 10:20")
                .faultTypes("其他")
                .serviceRequest("设备不知道怎么了")
                .bookTimeEndStr(bookTimeEnd)
                .sourceName("邮件").build();
        workRequestExcelDtoList.add(workRequestExcelDto1);
        workRequestExcelDtoList.add(workRequestExcelDto2);
        return workRequestExcelDtoList;
    }

    @Override
    public List<WorkRequestExportDto> getWorkRequestExportDtoList(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        List<WorkDto> list =  workRequestCompoService.exportWork(workFilter,userInfo,reqParam);
        List<WorkRequestExportDto> exportDtoList = new ArrayList<>();
        WorkRequestExportDto exportDto = null;
        // 远程调用获得企业映射

        if(CollectionUtil.isNotEmpty(list)) {
            for(WorkDto workDto : list){
                exportDto = new WorkRequestExportDto();
                BeanUtils.copyProperties(workDto,exportDto);
                if(workDto.getEndTime() != null) {
                    exportDto.setEndTime(FAULT_DATE_FORMAT.format(workDto.getEndTime()));
                }
                if(workDto.getCreateTime() != null){
                    exportDto.setCreateTime(FAULT_DATE_FORMAT.format(workDto.getCreateTime()));
                }
                if(workDto.getDispatchTime() != null) {
                    exportDto.setDispatchTime(FAULT_DATE_FORMAT.format(workDto.getDispatchTime()));
                }
                if(workDto.getGoTime() != null) {
                    exportDto.setGoTime(FAULT_DATE_FORMAT.format(workDto.getGoTime()));
                }
                if(workDto.getSignTime() != null) {
                    exportDto.setSignTime(FAULT_DATE_FORMAT.format(workDto.getSignTime()));
                }
                if(workDto.getAcceptTime() != null) {
                    exportDto.setAcceptTime(FAULT_DATE_FORMAT.format(workDto.getAcceptTime()));
                }

                exportDtoList.add(exportDto);
            }
        }
        return exportDtoList;
    }
}
