package com.zjft.usp.anyfix.work.request.composite.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.operate.dto.WorkOperateDto;
import com.zjft.usp.anyfix.work.operate.enums.WorkOperateTypeEnum;
import com.zjft.usp.anyfix.work.request.composite.CaseRequestCompoService;
import com.zjft.usp.anyfix.work.request.dto.CaseDto;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WarrantyModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.AesUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.RestTemplateUtil;
import com.zjft.usp.common.utils.RsaUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * 金融工单请求聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-02-25 13:49
 **/
@Slf4j
@Service
@RefreshScope
public class CaseRequestCompoServiceImpl implements CaseRequestCompoService {
    @Resource
    private UasFeignService uasFeignService;
    @Value("${sc.queryAtmCaseUrl}")
    private String queryAtmCaseUrl;
    @Value("${sc.findAtmCaseDetailUrl}")
    private String findAtmCaseDetailUrl;
    @Value("${sc.findAtmCaseOptionUrl}")
    private String findAtmCaseOptionUrl;
    @Value("${sc.privateKey}")
    private String privateKey;
    @Value("${sc.offsetDays}")
    private String offsetDays;
    private static final String DEMANDER_CORP_NAME = "深圳市紫金支点技术股份有限公司";
    private static final String SERVICE_CORP_NAME = "深圳市紫金支点技术股份有限公司";


    /**
     * 根据条件分页查询金融设备类工单
     *
     * @author Qiugm
     * @date 2020-02-22
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public ListWrapper<CaseDto> queryAtmCase(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        Page<WorkDto> page = new Page(workFilter.getPageNum(), workFilter.getPageSize());
        String paramData = makeQueryAtmCaseParams(workFilter, userInfo, page);
        String url = queryAtmCaseUrl;
        List<CaseDto> caseDtoList;
        try {
            log.info("请求老平台获取金融设备工单开始：" + DateUtil.now());
            String result = RestTemplateUtil.post(url, paramData, null);
            log.info("请求老平台获取金融设备工单结束：" + DateUtil.now());
            caseDtoList = makeCaseDtoList(result, page);
        } catch (Exception e) {
            if (e.getMessage().contains("Read timed out")) {
                throw new AppException("系统服务忙，请重试");
            } else if (e.getMessage().contains("connect timed out")) {
                throw new AppException("连接服务器超时，请重试");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new AppException("服务器拒绝访问，请重试");
            } else if (e.getMessage().contains("I/O error")) {
                throw new AppException("出现例外异常，请重试");
            } else {
                throw new AppException(e.getMessage());
            }
        }
        return ListWrapper.<CaseDto>builder()
                .list(caseDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 获得单个金融设备类工单详情
     *
     * @author Qiugm
     * @date 2020-02-23
     * @param workFilter
     * @param userInfo
     *
     * @return
     */
    @Override
    public CaseDto findAtmCaseDetail(WorkFilter workFilter, UserInfo userInfo) {
        String url = findAtmCaseDetailUrl;
        String paramData = makeQueryCaseDetailParams(workFilter, userInfo);
        CaseDto workDto;
        try {
            log.info("请求老平台获取金融设备工单明细开始：" + DateUtil.now());
            String result = RestTemplateUtil.post(url, paramData, null);
            log.info("请求老平台获取金融设备工单明细结束：" + DateUtil.now());
            workDto = makeWorkDto(result);
        } catch (Exception e) {
            if (e.getMessage().contains("Read timed out")) {
                throw new AppException("系统服务忙，请重试");
            } else if (e.getMessage().contains("connect timed out")) {
                throw new AppException("连接服务器超时，请重试");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new AppException("服务器拒绝访问，请重试");
            } else if (e.getMessage().contains("I/O error")) {
                throw new AppException("出现例外异常，请重试");
            } else {
                throw new AppException(e.getMessage());
            }
        }
        return workDto;
    }

    /**
     * 获得下拉数据源
     *
     * @author Qiugm
     * @date 2020-03-02
     * @param workFilter
     * @param userInfo
     * @return
     */
    @Override
    public Map<String, Object> findAtmCaseOption(WorkFilter workFilter, UserInfo userInfo) {
        String url = findAtmCaseOptionUrl;
        Map<String, Object> optionMap;
        String paramData = makeQueryCaseDetailParams(workFilter, userInfo);
        try {
            log.info("请求老平台获取金融设备工单映射Map开始：" + DateUtil.now());
            String result = RestTemplateUtil.post(url, paramData, null);
            log.info("请求老平台获取金融设备工单映射Map结束：" + DateUtil.now());
            optionMap = makeAtmCaseOption(result);
        } catch (Exception e) {
            if (e.getMessage().contains("Read timed out")) {
                throw new AppException("系统服务忙，请重试");
            } else if (e.getMessage().contains("connect timed out")) {
                throw new AppException("连接服务器超时，请重试");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new AppException("服务器拒绝访问，请重试");
            } else if (e.getMessage().contains("I/O error")) {
                throw new AppException("出现例外异常，请重试");
            } else {
                throw new AppException(e.getMessage());
            }
        }
        return optionMap;
    }

    /**
     * 制造映射Map
     *
     * @author Qiugm
     * @date 2020-03-02
     * @param result
     * @return
     */
    private Map<String, Object> makeAtmCaseOption(String result) {
        Map<String, Object> optionMap = new HashMap<>();
        if (StrUtil.isNotEmpty(result)) {
            if (result.contains("data")) {
                JSONObject resultObj = JsonUtil.parseObject(result, JSONObject.class);
                String dataStr = resultObj.getString("data");
                JSONObject dataObj = JsonUtil.parseObject(dataStr, JSONObject.class);
                if (dataObj.containsKey("workTypeMap")) {
                    Map<String, String> workTypeMap = JsonUtil.parseMap(dataObj.getString("workTypeMap"));
                    optionMap.put("workTypeMap", workTypeMap);
                }
                if (dataObj.containsKey("provinceMap")) {
                    Map<String, String> provinceMap = JsonUtil.parseMap(dataObj.getString("provinceMap"));
                    optionMap.put("provinceMap", provinceMap);
                }
                if (dataObj.containsKey("customMap")) {
                    Map<String, String> customMap = JsonUtil.parseMap(dataObj.getString("customMap"));
                    optionMap.put("customMap", customMap);
                }
                if (dataObj.containsKey("branchMap")) {
                    Map<String, String> branchMap = JsonUtil.parseMap(dataObj.getString("branchMap"));
                    optionMap.put("branchMap", branchMap);
                }
                if (dataObj.containsKey("brandMap")) {
                    Map<String, String> brandMap = JsonUtil.parseMap(dataObj.getString("brandMap"));
                    optionMap.put("brandMap", brandMap);
                }
                if (dataObj.containsKey("deviceModelMap")) {
                    Map<String, String> deviceModelMap = JsonUtil.parseMap(dataObj.getString("deviceModelMap"));
                    optionMap.put("deviceModelMap", deviceModelMap);
                }
            }
        }
        return optionMap;
    }

    /**
     * 制造WorkDto对象列表
     *
     * @author Qiugm
     * @date 2020-02-24
     * @param result
     * @param page
     * @return
     */
    private List<CaseDto> makeCaseDtoList(String result, Page<WorkDto> page) {
        List<CaseDto> caseDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(result) && result.contains("retMsg")) {
            JSONObject jsonObject = JsonUtil.parseObject(result, JSONObject.class);
            JSONObject retMsgObject = jsonObject.getJSONObject("retMsg");
            String code = retMsgObject.getString("code");
            if (StrUtil.isNotEmpty(code) && Integer.parseInt(code) == 1) {
                String atmCaseArray = jsonObject.getString("atmCaseList");
                if (StrUtil.isNotEmpty(atmCaseArray)) {
                    caseDtoList = JsonUtil.parseArray(atmCaseArray, CaseDto.class);
                    String pageStr = jsonObject.getString("pager");
                    JSONObject pageObject = JsonUtil.parseObject(pageStr, JSONObject.class);
                    page.setTotal(Long.parseLong(pageObject.getString("rows")));
                }
            } else {
                String msg = retMsgObject.getString("msg");
                if (StrUtil.isNotEmpty(msg) && !msg.contains("未找到金融设备类工单信息")) {
                    throw new AppException(msg);
                }
            }
        } else {
            throw new AppException(result);
        }
        return caseDtoList;
    }

    /**
     * 制造WorkDto对象
     *
     * @author Qiugm
     * @date 2020-02-24
     * @param result
     * @return
     */
    private CaseDto makeWorkDto(String result) {
        CaseDto caseDto = new CaseDto();
        if (StrUtil.isNotEmpty(result) && result.contains("atmCaseRemote")) {
            JSONObject jsonObject = JsonUtil.parseObject(result, JSONObject.class);
            String atmCase = jsonObject.getString("atmCaseRemote");
            if (StrUtil.isNotEmpty(atmCase)) {
                caseDto = JsonUtil.parseObject(atmCase, CaseDto.class);
                if (caseDto != null) {
                    caseDto.setCreatorName(caseDto.getCreatorName());
                    caseDto.setDemanderCorpName(DEMANDER_CORP_NAME);
                    caseDto.setServiceCorpName(SERVICE_CORP_NAME);
                    caseDto.setWarrantyModeName(WarrantyModeEnum.WHOLE_MACHINE_WARRANTY.getName());
                    caseDto.setServiceModeName(ServiceModeEnum.LOCALE_SERVICE.getName());

                    // 工单费用
                    WorkFeeDto workFeeDto = makeWorkFeeDto();
                    caseDto.setWorkFeeDto(workFeeDto);

                    // 使用部件
                    List<WareDto> usedPartList = JsonUtil.parseArray(jsonObject.getString("upPartListRemote"),
                            WareDto.class);
                    caseDto.setUsedPartList(usedPartList);

                    // 故障部件
                    List<WareDto> faultPartList = JsonUtil.parseArray(jsonObject.getString("downPartListRemote"),
                            WareDto.class);
                    caseDto.setFaultPartList(faultPartList);

                    // 处理过程
                    List<WorkOperateDto> workOperateList = makeWorkOperateList(caseDto);
                    caseDto.setWorkOperateList(workOperateList);
                }
            }
        }
        return caseDto;
    }

    /**
     * 创建工单处理过程
     *
     * @author Qiugm
     * @date 2020-02-26
     * @param caseDto
     * @return
     */
    private List<WorkOperateDto> makeWorkOperateList(CaseDto caseDto) {
        List<WorkOperateDto> workOperateList = new ArrayList<>();
        WorkOperateDto operateDto = new WorkOperateDto();
        if (caseDto.getWorkStatus() == WorkStatusEnum.CLOSED.getCode()) {
            operateDto.setOperateTypeName(WorkOperateTypeEnum.LOCATE_SERVICE.getName());
            operateDto.setOperateTime(caseDto.getFinishTime());
            operateDto.setSummary("现场服务 顺利完成");
            workOperateList.add(operateDto);
        }

        if (caseDto.getWorkStatus() == WorkStatusEnum.CANCELED.getCode()) {
            operateDto = new WorkOperateDto();
            operateDto.setOperateTypeName("取消");
            operateDto.setOperateTime(caseDto.getCancelTime());
            operateDto.setSummary(caseDto.getEngineerName() + "取消工单");
            workOperateList.add(operateDto);
        }

        if (caseDto.getSignTime() != null) {
            operateDto = new WorkOperateDto();
            operateDto.setOperateTypeName(WorkOperateTypeEnum.SIGN.getName());
            operateDto.setOperateTime(caseDto.getSignTime());
            operateDto.setSummary(caseDto.getEngineerName() + "已签到");
            workOperateList.add(operateDto);
        }

        operateDto = new WorkOperateDto();
        operateDto.setOperateTypeName(WorkOperateTypeEnum.CLAIM.getName());
        operateDto.setOperateTime(new Date(caseDto.getCreateTime().getTime() + 1000 * 60 * 1));
        operateDto.setSummary(caseDto.getEngineerName());
        workOperateList.add(operateDto);

        operateDto = new WorkOperateDto();
        operateDto.setOperateTypeName(WorkOperateTypeEnum.ASSIGN.getName());
        operateDto.setOperateTime(new Date(caseDto.getCreateTime().getTime() + 1000 * 30));
        operateDto.setSummary("自动派工给 工程师" + caseDto.getEngineerName());
        workOperateList.add(operateDto);

        operateDto = new WorkOperateDto();
        operateDto.setOperateTypeName(WorkOperateTypeEnum.AUTO_DISPATCH_BRANCH.getName());
        operateDto.setOperateTime(caseDto.getCreateTime());
        operateDto.setSummary("自动分配服务商网点 " + caseDto.getServiceBranchName() + " 并自动受理");
        workOperateList.add(operateDto);

        operateDto = new WorkOperateDto();
        operateDto.setOperateTypeName(WorkOperateTypeEnum.AUTO_DISPATCH.getName());
        operateDto.setOperateTime(caseDto.getCreateTime());
        operateDto.setSummary("自动提交服务商 " + SERVICE_CORP_NAME);
        workOperateList.add(operateDto);

        operateDto = new WorkOperateDto();
        operateDto.setOperateTypeName(WorkOperateTypeEnum.CREATE.getName());
        operateDto.setOperateTime(caseDto.getCreateTime());
        operateDto.setSummary(caseDto.getCreatorName() + "创建了" + caseDto.getWorkTypeName() + "工单");
        workOperateList.add(operateDto);

        return workOperateList;
    }

    /**
     * 创建工单费用
     * @return
     */
    private WorkFeeDto makeWorkFeeDto() {
        WorkFeeDto workFeeDto = new WorkFeeDto();
        workFeeDto.setBasicServiceFee(new BigDecimal(0));
        workFeeDto.setAssortFee(new BigDecimal(0));
        workFeeDto.setImplementFee(new BigDecimal(0));
        workFeeDto.setTotalFee(new BigDecimal(0));
        workFeeDto.setOtherFee(new BigDecimal(0));
        return workFeeDto;
    }

    /**
     * 查询老平台工单列表请求参数
     *
     * @author Qiugm
     * @date 2020-02-24
     * @param workFilter
     * @param userInfo
     * @param page
     * @return
     */
    private String makeQueryAtmCaseParams(WorkFilter workFilter, UserInfo userInfo, Page<WorkDto> page) {
        Map<String, Object> paramMap = new HashMap<>();
        if (workFilter != null) {
            if (StrUtil.isNotEmpty(workFilter.getCaseId())) {
                paramMap.put("caseId", StrUtil.trimToEmpty(workFilter.getCaseId()));
            }
            if (StrUtil.isNotEmpty(workFilter.getCustomCorpName())) {
                paramMap.put("customCorpName", StrUtil.trimToEmpty(workFilter.getCustomCorpName()));
            }
            if (StrUtil.isNotEmpty(workFilter.getDeviceBrandName())) {
                paramMap.put("brandName", StrUtil.trimToEmpty(workFilter.getDeviceBrandName()));
            }
            if (StrUtil.isNotEmpty(workFilter.getServiceBranches())) {
                paramMap.put("serviceBranchName", StrUtil.trimToEmpty(workFilter.getServiceBranches()));
            }
            if (StrUtil.isNotEmpty(workFilter.getDeviceModelName())) {
                paramMap.put("modelName", StrUtil.trimToEmpty(workFilter.getDeviceModelName()));
            }
            if (StrUtil.isNotEmpty(workFilter.getMachineCode())) {
                paramMap.put("serial", StrUtil.trimToEmpty(workFilter.getMachineCode()));
            }
            if (StrUtil.isNotEmpty(workFilter.getWorkTypes())) {
                paramMap.put("workType", StrUtil.trimToEmpty(workFilter.getWorkTypes()));
            }
            if (StrUtil.isNotEmpty(workFilter.getProvinceName())) {
                paramMap.put("provinceName", StrUtil.trimToEmpty(workFilter.getProvinceName()));
            }
            if (StrUtil.isNotEmpty(workFilter.getWorkStatuses())) {
                paramMap.put("workStatusName", StrUtil.trimToEmpty(workFilter.getWorkStatuses()));
            }

            if (workFilter.getStartDate() != null) {
                paramMap.put("startCreateDate", DateUtil.format(workFilter.getStartDate(), "yyyy-MM-dd"));
            }
            if (workFilter.getEndDate() != null) {
                paramMap.put("endCreateDate", DateUtil.format(workFilter.getEndDate(), "yyyy-MM-dd"));
            }

            if (workFilter.getQueryCount() == 1) {
                int days = 0;
                if (StrUtil.isNotEmpty(offsetDays)) {
                    days = Integer.parseInt(offsetDays);
                }

                if (workFilter.getYcStartDate() == null && workFilter.getStartDate() == null &&
                        workFilter.getFinishStartDate() == null) {
                    paramMap.put("startYcDate", DateUtil.format(DateUtil.offsetDay(new Date(), days), "yyyy-MM-dd"
                    ));
                    paramMap.put("endYcDate", DateUtil.format(new Date(), "yyyy-MM-dd"));
                }
            }

            if (workFilter.getFinishStartDate() != null) {
                paramMap.put("startCloseDate", DateUtil.format(workFilter.getFinishStartDate(), "yyyy-MM-dd"));
            }
            if (workFilter.getFinishEndDate() != null) {
                paramMap.put("endCloseDate", DateUtil.format(workFilter.getFinishEndDate(), "yyyy-MM-dd"));
            }

            if (workFilter.getYcStartDate() != null) {
                paramMap.put("startYcDate", DateUtil.format(workFilter.getYcStartDate(), "yyyy-MM-dd"));
            }
            if (workFilter.getYcEndDate() != null) {
                paramMap.put("endYcDate", DateUtil.format(workFilter.getYcEndDate(), "yyyy-MM-dd"));
            }
        }

        if (userInfo != null) {
            //查找手机号
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(userInfo.getUserId());
            Result<Map<Long, String>> userMobileMapResult =
                    this.uasFeignService.mapUserIdAndMobileByUserIdList(JsonUtil.toJson(userIdList));
            if (userMobileMapResult.getCode() == Result.SUCCESS) {
                Map<Long, String> userIdAndMobileMap = userMobileMapResult.getData() != null ?
                        userMobileMapResult.getData() : new HashMap<>();
                String mobile = userIdAndMobileMap.get(userInfo.getUserId());
                paramMap.put("mobile", mobile);
            }
            paramMap.put("sendTimestamp", System.currentTimeMillis());
        }
        if (page != null) {
            paramMap.put("num", StrUtil.toString(page.getCurrent()));
            paramMap.put("size", StrUtil.toString(page.getSize()));
        }
        String encryptData = encrypt(JsonUtil.toJson(paramMap));
        return encryptData;
    }

    /**
     * 查询老平台工单详情请求参数
     *
     * @author Qiugm
     * @date 2020-02-24
     * @param workFilter
     * @param userInfo
     * @return
     */
    private String makeQueryCaseDetailParams(WorkFilter workFilter, UserInfo userInfo) {
        Map<String, Object> paramMap = new HashMap<>();
        if (workFilter != null) {
            if (StrUtil.isNotEmpty(workFilter.getCaseId())) {
                paramMap.put("caseId", workFilter.getCaseId());
            }
            if (StrUtil.isNotEmpty(workFilter.getDeviceModelName())) {
                paramMap.put("machineTypeId", StrUtil.trimToEmpty(workFilter.getDeviceModelName()));
            }
            if (StrUtil.isNotEmpty(workFilter.getMachineCode())) {
                paramMap.put("serial", StrUtil.trimToEmpty(workFilter.getMachineCode()));
            }
        }

        if (userInfo != null) {
            //查找手机号
            List<Long> userIdList = new ArrayList<>();
            userIdList.add(userInfo.getUserId());
            Result<Map<Long, String>> userMobileMapResult =
                    this.uasFeignService.mapUserIdAndMobileByUserIdList(JsonUtil.toJson(userIdList));
            if (userMobileMapResult.getCode() == Result.SUCCESS) {
                Map<Long, String> userIdAndMobileMap = userMobileMapResult.getData() != null ?
                        userMobileMapResult.getData() : new HashMap<>();
                String mobile = userIdAndMobileMap.get(userInfo.getUserId());
                paramMap.put("mobile", mobile);
            }
            paramMap.put("sendTimestamp", System.currentTimeMillis());
        }
        String encryptData = encrypt(JsonUtil.toJson(paramMap));
        return encryptData;
    }

    /**
     * 加密数据
     *
     * @author Qiugm
     * @date 2020-02-26
     * @param originData
     * @return
     */
    private String encrypt(String originData) {
        String aesEncryptContent = "";
        String rsaEncryptDataSign = "";
        try {
            //1.使用对称加密工具类
            aesEncryptContent = AesUtil.encrypt(originData);
            //2、对称加密的数据再用非对称加密
            rsaEncryptDataSign = RsaUtil.sign(aesEncryptContent, RsaUtil.getPrivateKey(privateKey));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("cipherParam", aesEncryptContent);
        paramMap.put("signParam", rsaEncryptDataSign);
        return JsonUtil.toJson(paramMap);
    }

}