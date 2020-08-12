package com.zjft.usp.zj.work.repair.composite.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.device.atm.dto.DeviceTypeDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.repair.composite.RepairCompoService;
import com.zjft.usp.zj.work.repair.dto.*;
import com.zjft.usp.zj.work.repair.filter.RepairFilter;
import com.zjft.usp.zj.work.repair.mapping.AtmCaseMapping;
import com.zjft.usp.zj.work.repair.mapping.BxSendCreateFailMapping;
import com.zjft.usp.zj.work.repair.mapping.RepairMapping;
import com.zjft.usp.zj.work.repair.strategy.factory.RepairStrategyFactory;
import com.zjft.usp.zj.work.workorder.dto.WorkOrderDto;
import com.zjft.usp.zj.work.workorder.mapping.WorkOrderMapping;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 老平台报修聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:47
 **/
@Slf4j
@Service
public class RepairCompoServiceImpl implements RepairCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Resource
    RepairStrategyFactory repairStrategyFactory;
    @Value("${wo.repair.listRepairUrl}")
    private String listRepairUrl;
    @Value("${wo.repair.listUnCloseRepairUrl}")
    private String listUnCloseRepairUrl;
    @Value("${wo.repair.queryRepairUrl}")
    private String queryRepairUrl;
    @Value("${wo.repair.queryUnCloseRepairUrl}")
    private String queryUnCloseRepairUrl;
    @Value("${wo.repair.pickRelevanceCaseIdUrl}")
    private String pickRelevanceCaseIdUrl;
    @Value("${wo.repair.listRepairByUspUrl}")
    private String listRepairByUspUrl;

    /**
     * 根据条件分页查询报修单
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public ListWrapper<RepairDto> listRepair(RepairFilter repairFilter, UserInfo userInfo,
                                             ReqParam reqParam) {
        Page<RepairDto> page = new Page(repairFilter.getPageNum(), repairFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairFilter,
                RepairMapping.getNewAndOldPropertyMap());
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listRepairUrl);
        List<RepairDto> repairDtoList = makeRepairDtoList(handResult, page);
        return ListWrapper.<RepairDto>builder()
                .list(repairDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 根据条件分页查询未关闭的报修单
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public ListWrapper<RepairDto> listUnCloseRepair(RepairFilter repairFilter, UserInfo userInfo,
                                                    ReqParam reqParam) {
        Page<RepairDto> page = new Page(repairFilter.getPageNum(), repairFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairFilter,
                RepairMapping.getNewAndOldPropertyMap());
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listUnCloseRepairUrl);
        List<RepairDto> repairDtoList = makeRepairDtoList(handResult, page);
        return ListWrapper.<RepairDto>builder()
                .list(repairDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 查看报修单详情
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public Map findRepairDetail(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id", repairFilter.getRepairId());
        paramMap.add("repairId", repairFilter.getRepairId());
        String handResult = repairStrategyFactory.getStrategy(
                RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairFilter.getServiceId()).findRepairDetail(paramMap, userInfo, reqParam);
        Map repairDetailMap = makeRepairDetailMap(handResult);
        return repairDetailMap;
    }

    /**
     * 进入转处理页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public Map turnHandle(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id", repairFilter.getRepairId());
        paramMap.add("repairId", repairFilter.getRepairId());
        String handResult = repairStrategyFactory.getStrategy(
                RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairFilter.getServiceId()).turnHandle(paramMap, userInfo, reqParam);

        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handResult) && !handResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (jsonObject.containsKey("listAtmType")) {
                List<DeviceTypeDto> deviceTypeDtoList = JsonUtil.parseArray(jsonObject.getString("listAtmType"),
                        DeviceTypeDto.class);
                returnMap.put("deviceTypeDtoList", deviceTypeDtoList);
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = makeRepairDto(jsonObject.getJSONObject("faultRepair"));
                returnMap.put("repairDto", repairDto);
            }
            if (jsonObject.containsKey("bureauMap")) {
                Map<String, String> bureauMap = JsonUtil.parseMap(jsonObject.getString("bureauMap"));
                returnMap.put("bureauMap", bureauMap);
            }
            if (jsonObject.containsKey("userMap")) {
                Map<String, String> userMap = JsonUtil.parseMap(jsonObject.getString("userMap"));
                returnMap.put("userMap", userMap);
            }
            returnMap.put("modTime", jsonObject.getString("modTime"));
        }
        return returnMap;
    }

    /**
     * 转处理提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public void turnHandleSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairDto,
                RepairMapping.getNewAndOldPropertyMap());
        String handleResult =
                repairStrategyFactory.getStrategy(RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairDto.getServiceId()).turnHandleSubmit(paramMap, userInfo, reqParam);

        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("returnCode")) {
                String returnCode = jsonObject.getString("returnCode");
                if (!StatusCodeConstants.SUCCESS_ONE.equals(returnCode)) {
                    String returnMsg = jsonObject.getString("returnMsg");
                    throw new AppException(returnMsg);
                }
            }
        }
    }

    /**
     * 进入电话处理页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public Map phoneHandle(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id", repairFilter.getRepairId());
        paramMap.add("repairId", repairFilter.getRepairId());
        String handResult = repairStrategyFactory.getStrategy(
                RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairFilter.getServiceId()).phoneHandle(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handResult) && !handResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (jsonObject.containsKey("listAtmType")) {
                List<DeviceTypeDto> deviceTypeDtoList = JsonUtil.parseArray(jsonObject.getString("listAtmType"),
                        DeviceTypeDto.class);
                returnMap.put("deviceTypeDtoList", deviceTypeDtoList);
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = makeRepairDto(jsonObject.getJSONObject("faultRepair"));
                returnMap.put("repairDto", repairDto);
            }
            returnMap.put("modTime", jsonObject.getString("modTime"));
        }
        return returnMap;
    }

    /**
     * 电话处理提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public void phoneHandleSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairDto,
                RepairMapping.getNewAndOldPropertyMap());
        String handleResult =
                repairStrategyFactory.getStrategy(RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairDto.getServiceId()).phoneHandleSubmit(paramMap, userInfo, reqParam);
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("returnCode")) {
                String returnCode = jsonObject.getString("returnCode");
                if (!StatusCodeConstants.SUCCESS_ONE.equals(returnCode)) {
                    String returnMsg = jsonObject.getString("returnMsg");
                    throw new AppException(returnMsg);
                }
            }
        }
    }

    /**
     * 进入退单页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public Map returnHandle(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id", repairFilter.getRepairId());
        paramMap.add("repairId", repairFilter.getRepairId());

        String handResult = repairStrategyFactory.getStrategy(
                RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairFilter.getServiceId()).returnHandle(paramMap, userInfo, reqParam);

        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handResult) && !handResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (jsonObject.containsKey("returnReasonMap")) {
                Map returnReasonMap = JsonUtil.parseMap(jsonObject.getString("returnReasonMap"));
                returnMap.put("returnReasonMap", returnReasonMap);
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = makeRepairDto(jsonObject.getJSONObject("faultRepair"));
                returnMap.put("repairDto", repairDto);
            }
            returnMap.put("modTime", jsonObject.getString("modTime"));
        }
        return returnMap;
    }

    /**
     * 退单提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public void returnHandleSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairDto,
                RepairMapping.getNewAndOldPropertyMap());
        String handleResult =
                repairStrategyFactory.getStrategy(RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairDto.getServiceId()).returnHandleSubmit(paramMap, userInfo, reqParam);
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("returnCode")) {
                String returnCode = jsonObject.getString("returnCode");
                if (!StatusCodeConstants.SUCCESS_ONE.equals(returnCode)) {
                    String returnMsg = jsonObject.getString("returnMsg");
                    throw new AppException(returnMsg);
                }
            }
        }
    }

    /**
     * 进入关联CASE页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public Map associateCase(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id", repairFilter.getRepairId());
        paramMap.add("repairId", repairFilter.getRepairId());

        String handResult = repairStrategyFactory.getStrategy(
                RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                        repairFilter.getServiceId()).associateCase(paramMap, userInfo, reqParam);

        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handResult) && !handResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = makeRepairDto(jsonObject.getJSONObject("faultRepair"));
                returnMap.put("repairDto", repairDto);
            }
            returnMap.put("modTime", jsonObject.getString("modTime"));
        }
        return returnMap;
    }

    /**
     * 关联CASE提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    @Override
    public void associateCaseSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairDto,
                RepairMapping.getNewAndOldPropertyMap());
        String handleResult =
                repairStrategyFactory.getStrategy(
                        RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                repairDto.getServiceId()).associateCaseSubmit(paramMap, userInfo, reqParam);
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("returnCode")) {
                String returnCode = jsonObject.getString("returnCode");
                if (!StatusCodeConstants.SUCCESS_ONE.equals(returnCode)) {
                    String returnMsg = jsonObject.getString("returnMsg");
                    throw new AppException(returnMsg);
                }
            }
        }
    }

    /**
     * 查询补录失败的记录
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public ListWrapper<BxSendCreateFailDto> listBxSendCreateFail(RepairFilter repairFilter, UserInfo userInfo,
                                                                 ReqParam reqParam) {
        Page<BxSendCreateFailDto> page = new Page(repairFilter.getPageNum(), repairFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairFilter,
                RepairMapping.getNewAndOldPropertyMap());
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());
        String handResult =
                repairStrategyFactory.getStrategy(
                        RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                repairFilter.getServiceId()).listBxSendCreateFail(paramMap, userInfo, reqParam);
        List<BxSendCreateFailDto> bxSendCreateFailDtoList = makeBxSendCreateFailDtoList(handResult, page);
        return ListWrapper.<BxSendCreateFailDto>builder()
                .list(bxSendCreateFailDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 进入补录失败修改页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map modBxSendCreateFail(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("tranId", repairFilter.getTranId());
        String handResult =
                repairStrategyFactory.getStrategy(RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + repairFilter.getServiceId()).modBxSendCreateFail(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handResult) && !handResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (jsonObject.containsKey("bxSendCreateFailRemote")) {
                BxSendCreateFailDto bxSendCreateFailDto =
                        VariableConvertUtil.convertToNewEntity(jsonObject.getString("bxSendCreateFailRemote"),
                                BxSendCreateFailMapping.getOldAndNewPropertyMap(), BxSendCreateFailDto.class);
                returnMap.put("bxSendCreateFailDto", bxSendCreateFailDto);
            }
            if (jsonObject.containsKey("mapDealWay")) {
                Map mapDealWay = JsonUtil.parseMap(jsonObject.getString("mapDealWay"));
                returnMap.put("mapDealWay", mapDealWay);
            }
            if (jsonObject.containsKey("mapBxCfgSmall")) {
                Map mapBxCfgSmall = JsonUtil.parseMap(jsonObject.getString("mapBxCfgSmall"));
                returnMap.put("mapBxCfgSmall", mapBxCfgSmall);
            }
        }
        return returnMap;
    }

    /**
     * 补录失败修改提交
     *
     * @param bxSendCreateFailDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void modBxSendCreateFailSubmit(BxSendCreateFailDto bxSendCreateFailDto, UserInfo userInfo,
                                             ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(bxSendCreateFailDto,
                BxSendCreateFailMapping.getNewAndOldPropertyMap());
        paramMap.add("tranId", bxSendCreateFailDto.getTranID());
        String handleResult =
                repairStrategyFactory.getStrategy(
                        RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                bxSendCreateFailDto.getServiceID()).modBxSendCreateFailSubmit(paramMap, userInfo,
                        reqParam);
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("returnCode")) {
                String returnCode = jsonObject.getString("returnCode");
                if (!StatusCodeConstants.SUCCESS_ONE.equals(returnCode)) {
                    String returnMsg = jsonObject.getString("returnMsg");
                    throw new AppException(returnMsg);
                }
            }
        }
    }

    /**
     * 获取银行编号与名称映射Map
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-23
     */
    @Override
    public Map findBankMap(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bankName", StrUtil.trimToEmpty(repairFilter.getBankName()));

        String handleResult = repairStrategyFactory.getStrategy(RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                repairFilter.getServiceId()).findBankMap(paramMap, userInfo, reqParam);

        Map<String, String> bankMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handleResult, JSONObject.class);
            bankMap = JsonUtil.parseMapSort(resultObject.getString("bankMap"));
        }
        return bankMap;
    }

    /**
     * 获取服务站和网点编号与名称映射Map
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    @Override
    public Map findBureauAndBranchMap(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("bankCode", StrUtil.trimToEmpty(repairFilter.getBankCode()));
        paramMap.add("bankMode", StrUtil.trimToEmpty(repairFilter.getBankMode()));

        String handleResult = repairStrategyFactory.getStrategy(
                RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + repairFilter.getServiceId()).
                findBureauAndBranchMap(paramMap, userInfo, reqParam);

        Map<String, String> bureauAndBranchMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handleResult, JSONObject.class);
            bureauAndBranchMap = JsonUtil.parseMapSort(resultObject.getString("bureauAndBranchMap"));
        }
        return bureauAndBranchMap;
    }

    /**
     * 根据交易号获取工行对接报修图片
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    @Override
    public List<BxImgDto> listFaultRepairPic(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("tranId", StrUtil.trimToEmpty(repairFilter.getTranId()));
        paramMap.add("businessId", StrUtil.trimToEmpty(repairFilter.getBusinessId()));

        String handleResult = repairStrategyFactory.getStrategy(
                RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX + repairFilter.getServiceId()).
                listFaultRepairPic(paramMap, userInfo, reqParam);

        List<BxImgDto> bxImgDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (resultObject.containsKey("listBxImgRemote")) {
                bxImgDtoList = JsonUtil.parseArray(resultObject.getString("listBxImgRemote"), BxImgDto.class);
            }
            if (resultObject.containsKey("bankRepairPicRemoteList")) {
                bxImgDtoList = JsonUtil.parseArray(resultObject.getString("bankRepairPicRemoteList"), BxImgDto.class);
            }
        }
        return bxImgDtoList;
    }

    /**
     * 查看工行对接报修图片
     *
     * @param fileId
     * @param serviceId
     * @param userInfo
     * @param reqParam
     * @param response
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    @Override
    public void viewFaultRepairPic(String fileId, String serviceId, UserInfo userInfo, ReqParam reqParam,
                                   HttpServletResponse response) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("fileId", fileId);
        repairStrategyFactory.getStrategy(RepairStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                serviceId).viewFaultRepairPic(paramMap, userInfo, reqParam, response);
    }

    /**
     * 进入报修单查询页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-26
     */
    @Override
    public Map queryRepair(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairFilter,
                RepairMapping.getNewAndOldPropertyMap());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, queryRepairUrl);
        Map resultMap = new HashMap();
        if (StrUtil.isNotEmpty(handResult) && handResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (resultObject.containsKey("reportTimeFrom")) {
                String reportTimeFrom = resultObject.getString("reportTimeFrom");
                resultMap.put("reportTimeFrom", reportTimeFrom);
            }
            if (resultObject.containsKey("reportTimeTo")) {
                String reportTimeTo = resultObject.getString("reportTimeTo");
                resultMap.put("reportTimeTo", reportTimeTo);
            }
            if (resultObject.containsKey("mapBxAppStatus")) {
                String mapBxAppStatusJson = resultObject.getString("mapBxAppStatus");
                resultMap.put("mapBxAppStatus", JsonUtil.parseMap(mapBxAppStatusJson));
            }
            if (resultObject.containsKey("mapFacadded")) {
                String mapFacaddedJson = resultObject.getString("mapFacadded");
                resultMap.put("mapFacadded", JsonUtil.parseMap(mapFacaddedJson));
            }
            if (resultObject.containsKey("mapAppType")) {
                String mapAppTypeJson = resultObject.getString("mapAppType");
                resultMap.put("mapAppType", JsonUtil.parseMap(mapAppTypeJson));
            }
            if (resultObject.containsKey("mapSupervised")) {
                String mapSupervisedJson = resultObject.getString("mapSupervised");
                resultMap.put("mapSupervised", JsonUtil.parseMap(mapSupervisedJson));
            }
            if (resultObject.containsKey("mapDispatchWorkStatus")) {
                String mapDispatchWorkStatusJson = resultObject.getString("mapDispatchWorkStatus");
                resultMap.put("mapDispatchWorkStatus", JsonUtil.parseMap(mapDispatchWorkStatusJson));
            }
        }
        return resultMap;
    }

    /**
     * 进入未关闭的报修单查询页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-26
     */
    @Override
    public Map queryUnCloseRepair(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairFilter,
                RepairMapping.getNewAndOldPropertyMap());
        String handleResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, queryUnCloseRepairUrl);
        Map resultMap = new HashMap();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (resultObject.containsKey("mapBxAppStatus")) {
                String mapBxAppStatusJson = resultObject.getString("mapBxAppStatus");
                resultMap.put("mapBxAppStatus", JsonUtil.parseMap(mapBxAppStatusJson));
            }
            if (resultObject.containsKey("mapAppType")) {
                String mapAppTypeJson = resultObject.getString("mapAppType");
                resultMap.put("mapAppType", JsonUtil.parseMap(mapAppTypeJson));
            }
            if (resultObject.containsKey("mapSupervised")) {
                String mapSupervisedJson = resultObject.getString("mapSupervised");
                resultMap.put("mapSupervised", JsonUtil.parseMap(mapSupervisedJson));
            }
            if (resultObject.containsKey("mapDispatchWorkStatus")) {
                String mapDispatchWorkStatusJson = resultObject.getString("mapDispatchWorkStatus");
                resultMap.put("mapDispatchWorkStatus", JsonUtil.parseMap(mapDispatchWorkStatusJson));
            }
            if (resultObject.containsKey("userMap")) {
                String userMapJson = resultObject.getString("userMap");
                resultMap.put("userMap", JsonUtil.parseMap(userMapJson));
            }
        }
        return resultMap;
    }

    /**
     * 根据报修单的ID找到与之对应的最新执行人处理的CASE
     *
     * @author Qiugm
     * @date 2020-03-31
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    @Override
    public List<CaseDto> pickRelevanceCaseId(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("id", StrUtil.trimToEmpty(repairFilter.getRepairId()));

        String handleResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, pickRelevanceCaseIdUrl);

        List<CaseDto> caseDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            caseDtoList = VariableConvertUtil.convertToNewEntityList(resultObject.getString("yjcaseList"),
                    AtmCaseMapping.getOldAndNewPropertyMap(), CaseDto.class);
        }
        return caseDtoList;
    }

    /**
     * 根据新平台工单状态查询报修信息
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-01
     */
    @Override
    public ListWrapper<RepairDto> listRepairByWorkStatus(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        Page page = new Page(repairFilter.getPageNum(), repairFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairFilter);
        if (repairFilter.getStatus() != null && repairFilter.getStatus().length > 0) {
            String status = StringUtils.join(repairFilter.getStatus(), ",");
            paramMap.set("status", status);
        }
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());

        String handleResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, listRepairByUspUrl);

        Long totalCount = 0L;
        List<RepairDto> repairDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObjectSort(handleResult, JSONObject.class);
            if (resultObject.containsKey("returnMsg")) {
                String returnMsg = resultObject.getString("returnMsg");
                if (StrUtil.isNotEmpty(returnMsg)) {
                    throw new AppException(returnMsg);
                }
            }
            JSONObject dtoObject = JsonUtil.parseObjectSort(resultObject.getString("dto"), JSONObject.class);
            totalCount = dtoObject.getLong("totalCount");
            repairDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                    RepairMapping.getOldAndNewPropertyMap(), RepairDto.class);
        }
        return ListWrapper.<RepairDto>builder()
                .list(repairDtoList)
                .total(totalCount)
                .build();
    }

    /**
     * 创建报修详情Map
     * 包含报修详情、预警信息、派工单、报修跟踪记录等信息
     *
     * @param handleResult
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    private Map makeRepairDetailMap(String handleResult) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = makeRepairDto(jsonObject.getJSONObject("faultRepair"));
                resultMap.put("repairDto", repairDto);
            }
            if (jsonObject.containsKey("pmEarlyWarningMap")) {
                Map<String, String> pmEarlyWarningMap = JsonUtil.parseMap(jsonObject.getString("pmEarlyWarningMap"));
                resultMap.put("pmEarlyWarningMap", pmEarlyWarningMap);
            }
            if (jsonObject.containsKey("workStatusMap")) {
                Map<Integer, String> workStatusMap = JsonUtil.parseMap(jsonObject.getString("workStatusMap"));
                resultMap.put("workStatusMap", workStatusMap);
            }
            if (jsonObject.containsKey("listFaultRepairTraces")) {
                List<RepairTraceDto> repairTraceDtoList = JsonUtil.parseArray(jsonObject.getString(
                        "listFaultRepairTraces"), RepairTraceDto.class);
                /**反转成倒序排序*/
                CollUtil.reverse(repairTraceDtoList);
                resultMap.put("repairTraceDtoList", repairTraceDtoList);
            }
            if (jsonObject.containsKey("listFaultRepairTrace")) {
                List<RepairTraceDto> repairTraceDtoList = JsonUtil.parseArray(jsonObject.getString(
                        "listFaultRepairTrace"), RepairTraceDto.class);
                /**反转成倒序排序*/
                CollUtil.reverse(repairTraceDtoList);
                resultMap.put("repairTraceDtoList", repairTraceDtoList);
            }
            if (jsonObject.containsKey("listBxReceiveBug")) {
                List<BxReceiveBugDto> bxReceiveBugDtoList = JsonUtil.parseArray(jsonObject.getString(
                        "listBxReceiveBug"), BxReceiveBugDto.class);
                resultMap.put("bxReceiveBugDtoList", bxReceiveBugDtoList);
            }
            if (jsonObject.containsKey("listWorkOrders")) {
                List<WorkOrderDto> workOrderDtoList = makeWorkOrderDtoList(jsonObject.getString("listWorkOrders"));
                resultMap.put("workOrderDtoList", workOrderDtoList);
            }
            // 新建派工单权限：Y-有，N-否
            resultMap.put("addWorkOrderFlag", jsonObject.getString("addWorkOrderFlag"));
            // 转处理权限：Y-有，N-否
            resultMap.put("turnHandleFlag", jsonObject.getString("turnHandleFlag"));
            // 服务主管【电话处理】权限：Y-有，N-否
            resultMap.put("managerPhoneFlag", jsonObject.getString("managerPhoneFlag"));
            // 退单权限：Y-有，N-否
            resultMap.put("returnFlag", jsonObject.getString("returnFlag"));
            // 关联CASE权限：Y-有，N-否
            resultMap.put("associateCaseFlag", jsonObject.getString("associateCaseFlag"));
            // 远程处理权限：Y-有，N-否
            resultMap.put("remoteFlag", jsonObject.getString("remoteFlag"));
            // 主动结束派工权限：Y-有，N-否
            resultMap.put("endWorkOrderFlag", jsonObject.getString("endWorkOrderFlag"));
            // 400权限：true-有，false-否
            resultMap.put("has400Right", jsonObject.getString("has400Right"));
            // 管理员权限：true-有，false-否
            resultMap.put("hasSysManagerRight", jsonObject.getString("hasSysManagerRight"));
            if (jsonObject.containsKey("caseId")) {
                resultMap.put("caseId", jsonObject.getString("caseId"));
            }
            if (jsonObject.containsKey("caseId")) {
                resultMap.put("caseId", jsonObject.getString("caseId"));
            }
            if (jsonObject.containsKey("listWorkOrder")) {
                List<WorkOrderDto> workOrderDtoList = makeWorkOrderDtoList(jsonObject.getString("listWorkOrder"));
                resultMap.put("workOrderDtoList", workOrderDtoList);
            }
            if (jsonObject.containsKey("caseId")) {
                resultMap.put("caseId", jsonObject.getString("caseId"));
            }
            if (jsonObject.containsKey("remoteFlag")) {
                resultMap.put("remoteFlag", jsonObject.getString("remoteFlag"));
            }
            if (jsonObject.containsKey("faultCode")) {
                resultMap.put("faultCode", jsonObject.getString("faultCode"));
            }
            if (jsonObject.containsKey("has400Right")) {
                resultMap.put("has400Right", jsonObject.getString("has400Right"));
            }
            if (jsonObject.containsKey("hasSysManagerRight")) {
                resultMap.put("hasSysManagerRight", jsonObject.getString("hasSysManagerRight"));
            }
            if (jsonObject.containsKey("endWorkOrderFlag")) {
                resultMap.put("endWorkOrderFlag", jsonObject.getString("endWorkOrderFlag"));
            }
            if (jsonObject.containsKey("bankUserLoginFlag")) {
                resultMap.put("bankUserLoginFlag", jsonObject.getString("bankUserLoginFlag"));
            }
            if (jsonObject.containsKey("cancelOrderFlag")) {
                resultMap.put("cancelOrderFlag", jsonObject.getString("cancelOrderFlag"));
            }
            // 退回主管按钮权限：Y-有，N-否
            if (jsonObject.containsKey("canReturnManagerFlag")) {
                resultMap.put("canReturnManagerFlag", jsonObject.getString("canReturnManagerFlag"));
            }
            // 预约权限：Y-有，N-否
            if (jsonObject.containsKey("preTimeFlag")) {
                resultMap.put("preTimeFlag", jsonObject.getString("preTimeFlag"));
            }
            // 重新预约权限：Y-有，N-否
            if (jsonObject.containsKey("rePreTimeFlag")) {
                resultMap.put("rePreTimeFlag", jsonObject.getString("rePreTimeFlag"));
            }
            // 工程师电话处理权限：Y-有，N-否
            if (jsonObject.containsKey("engineerPhoneFlag")) {
                resultMap.put("engineerPhoneFlag", jsonObject.getString("engineerPhoneFlag"));
            }
            // 接受派工权限：Y-有，N-否
            if (jsonObject.containsKey("acceptFlag")) {
                resultMap.put("acceptFlag", jsonObject.getString("acceptFlag"));
            }
            // 添加CASE权限：Y-有，N-否
            if (jsonObject.containsKey("addCaseFlag")) {
                resultMap.put("addCaseFlag", jsonObject.getString("addCaseFlag"));
            }
        }
        return resultMap;
    }

    /**
     * 创建RepairDto列表信息
     *
     * @param handleResult
     * @param page
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    private List<RepairDto> makeRepairDtoList(String handleResult, Page page) {
        List<RepairDto> repairDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.contains("dto")) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            String dtoString = jsonObject.getString("dto");
            if (StrUtil.isNotEmpty(dtoString) && dtoString.contains("data")) {
                JSONObject dtoObject = JsonUtil.parseObjectSort(dtoString, JSONObject.class);
                String totalCount = dtoObject.getString("totalCount");
                page.setTotal(Long.parseLong(totalCount));
                String repairInfosJson = dtoObject.getString("data");
                JSONArray jsonArray = JsonUtil.parseObject(repairInfosJson, JSONArray.class);
                for (int i = 0; i < jsonArray.size(); i++) {
                    JSONObject repairObj = jsonArray.getJSONObject(i);
                    RepairDto repairDto = makeRepairDto(repairObj);
                    if (repairDto != null) {
                        repairDtoList.add(repairDto);
                    }
                }
            }
        }
        return repairDtoList;
    }

    /**
     * 创建RepairDto列表信息
     *
     * @param jsonObject
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    private RepairDto makeRepairDto(JSONObject jsonObject) {
        RepairDto repairDto = null;
        if (jsonObject != null) {
            repairDto = VariableConvertUtil.convertToNewEntity(JsonUtil.toJson(jsonObject),
                    RepairMapping.getOldAndNewPropertyMap(), RepairDto.class);
            if (jsonObject.containsKey("bxReceiveCreate")) {
                String bxReceiveCreateJson = jsonObject.getString("bxReceiveCreate");
                BxReceiveCreateDto bxReceiveCreateDto = makeBxReceiveCreate(bxReceiveCreateJson);
                if (bxReceiveCreateDto != null) {
                    repairDto.setBxReceiveCreate(bxReceiveCreateDto);
                }
            }
            if (jsonObject.containsKey("faultRepairExtra")) {
                RepairExtraDto repairExtraDto = makeRepairExtra(jsonObject.getString("faultRepairExtra"));
                if (repairExtraDto != null) {
                    repairDto.setRepairExtra(repairExtraDto);
                }
            }
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString(
                        "workOrderRemote"), WorkOrderMapping.getOldAndNewPropertyMap(), WorkOrderDto.class);
                if (workOrderDto != null) {
                    repairDto.setWorkOrderDto(workOrderDto);
                }
            }
        }
        return repairDto;
    }

    /**
     * 创建报修附加表信息
     *
     * @param repairExtraJson
     * @return
     * @author Qiugm
     * @date 2020-03-22
     */
    private RepairExtraDto makeRepairExtra(String repairExtraJson) {
        RepairExtraDto repairExtraDto = null;
        if (StrUtil.isNotEmpty(repairExtraJson)) {
            repairExtraDto = JsonUtil.parseObject(repairExtraJson, RepairExtraDto.class);
        }
        return repairExtraDto;
    }

    /**
     * 创建派工单列表
     *
     * @param workOrderJsonArray
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    private List<WorkOrderDto> makeWorkOrderDtoList(String workOrderJsonArray) {
        List<WorkOrderDto> workOrderDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(workOrderJsonArray)) {
            workOrderDtoList = VariableConvertUtil.convertToNewEntityList(workOrderJsonArray,
                    WorkOrderMapping.getOldAndNewPropertyMap(), WorkOrderDto.class);
        }
        return workOrderDtoList;
    }

    /**
     * 创建工行对接报修接收信息
     *
     * @param bxReceiveCreateJson
     * @return
     * @author Qiugm
     * @date 2020-03-22
     */
    private BxReceiveCreateDto makeBxReceiveCreate(String bxReceiveCreateJson) {
        BxReceiveCreateDto bxReceiveCreateDto = JsonUtil.parseObject(bxReceiveCreateJson, BxReceiveCreateDto.class);
        return bxReceiveCreateDto;
    }

    /**
     * 创建补录失败列表
     *
     * @param handleResult
     * @param page
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    private List<BxSendCreateFailDto> makeBxSendCreateFailDtoList(String handleResult, Page<BxSendCreateFailDto> page) {
        List<BxSendCreateFailDto> bxSendCreateFailDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handleResult) && handleResult.contains("dto")) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            String dtoString = jsonObject.getString("dto");
            if (StrUtil.isNotEmpty(dtoString) && dtoString.contains("data")) {
                JSONObject dtoObject = JsonUtil.parseObject(dtoString, JSONObject.class);
                String totalCount = dtoObject.getString("totalCount");
                page.setTotal(Long.parseLong(totalCount));
                String data = dtoObject.getString("data");
                bxSendCreateFailDtoList = JsonUtil.parseArray(data, BxSendCreateFailDto.class);
            }
        }
        return bxSendCreateFailDtoList;
    }

}
