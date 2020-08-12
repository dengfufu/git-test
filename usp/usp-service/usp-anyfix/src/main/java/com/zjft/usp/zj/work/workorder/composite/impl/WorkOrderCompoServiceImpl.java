package com.zjft.usp.zj.work.workorder.composite.impl;

import cn.hutool.core.util.StrUtil;
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
import com.zjft.usp.zj.work.repair.dto.*;
import com.zjft.usp.zj.work.repair.mapping.RepairMapping;
import com.zjft.usp.zj.work.workorder.composite.WorkOrderCompoService;
import com.zjft.usp.zj.work.workorder.dto.WorkOrderDto;
import com.zjft.usp.zj.work.workorder.filter.WorkOrderFilter;
import com.zjft.usp.zj.work.workorder.mapping.WorkOrderMapping;
import com.zjft.usp.zj.work.workorder.strategy.factory.WorkOrderStrategyFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 派工单聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 17:31
 **/
@Slf4j
@Service
public class WorkOrderCompoServiceImpl implements WorkOrderCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Resource
    private WorkOrderStrategyFactory workOrderStrategyFactory;
    @Value("${wo.workorder.listMyWorkUrl}")
    private String listMyWorkUrl;
    @Value("${wo.workorder.viewWorkOrderUrl}")
    private String viewWorkOrderUrl;

    /**
     * 查询我的工单列表
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public ListWrapper<WorkOrderDto> listMyWork(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        Page<WorkOrderDto> page = new Page(workOrderFilter.getPageNum(), workOrderFilter.getPageSize());
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderFilter,
                WorkOrderMapping.getNewAndOldPropertyMap());
        paramMap.add("dto.currentPage", page.getCurrent());
        paramMap.add("dto.pageSize", page.getSize());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, listMyWorkUrl);
        List<WorkOrderDto> repairDtoList = makeWorkOrderDtoList(handResult, page);
        return ListWrapper.<WorkOrderDto>builder()
                .list(repairDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 创建WorkOrderDto列表信息
     *
     * @param jsonObject
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    private WorkOrderDto makeWorkOrderDto(JSONObject jsonObject) {
        WorkOrderDto workOrderDto = null;
        if (jsonObject != null && jsonObject.containsKey("workOrderRemote")) {
            String workOrderStr = jsonObject.getString("workOrderRemote");
            workOrderDto = VariableConvertUtil.convertToNewEntity(workOrderStr, WorkOrderMapping.getOldAndNewPropertyMap(), WorkOrderDto.class);
        }
        return workOrderDto;
    }

    /**
     * 创建派工单列表
     *
     * @param handResult
     * @param page
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    private List<WorkOrderDto> makeWorkOrderDtoList(String handResult, Page<WorkOrderDto> page) {
        List<WorkOrderDto> workOrderDtoList = new ArrayList<>();
        if (StrUtil.isNotEmpty(handResult) && handResult.contains("dto")) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String dtoString = jsonObject.getString("dto");
            if (StrUtil.isNotEmpty(dtoString) && dtoString.contains("data")) {
                JSONObject dtoObject = JsonUtil.parseObject(dtoString, JSONObject.class);
                String totalCount = dtoObject.getString("totalCount");
                page.setTotal(Long.parseLong(totalCount));
                workOrderDtoList = VariableConvertUtil.convertToNewEntityList(dtoObject.getString("data"),
                        WorkOrderMapping.getOldAndNewPropertyMap(), WorkOrderDto.class);
            }
        }
        return workOrderDtoList;
    }

    /**
     * 查看派工单详情
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map findWorkOrderDetail(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", workOrderFilter.getWorkOrderId());
        String handResult = this.sendWoUtil.postToWo(userInfo, reqParam, paramMap, viewWorkOrderUrl);
        Map workOrderDetailMap = makeWorkOrderDetailMap(handResult);
        return workOrderDetailMap;
    }

    /**
     * 创建派工单详情Map
     *
     * @param handResult
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    private Map makeWorkOrderDetailMap(String handResult) {
        Map<String, Object> resultMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handResult) && !handResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handResult, JSONObject.class);
            if (jsonObject.containsKey("faultRepair")) {
                String faultRepairStr = jsonObject.getString("faultRepair");
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(faultRepairStr,
                        RepairMapping.getOldAndNewPropertyMap(), RepairDto.class);
                resultMap.put("repairDto", repairDto);
            }
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    resultMap.put("workOrderDto", workOrderDto);
                }
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
                resultMap.put("repairTraceDtoList", repairTraceDtoList);
            }
            if (jsonObject.containsKey("listBxReceiveBug")) {
                List<BxReceiveBugDto> bxReceiveBugDtoList = JsonUtil.parseArray(jsonObject.getString(
                        "listBxReceiveBug"), BxReceiveBugDto.class);
                resultMap.put("bxReceiveBugDtoList", bxReceiveBugDtoList);
            }
            // 接收派工单权限：Y-有，N-否
            resultMap.put("acceptFlag", jsonObject.getString("acceptFlag"));
            // 添加CASE权限：Y-有，N-否
            resultMap.put("addCaseFlag", jsonObject.getString("addCaseFlag"));
            // 电话处理权限：Y-有，N-否
            resultMap.put("remoteFlag", jsonObject.getString("remoteFlag"));
            // 查看case权限：Y-有，N-否
            resultMap.put("viewCaseFlag", jsonObject.getString("viewCaseFlag"));
            // 退回主管权限：Y-有，N-否
            resultMap.put("canReturnManagerFlag", jsonObject.getString("canReturnManagerFlag"));
            // 电话处理权限：Y-有，N-否
            resultMap.put("engineerPhoneFlag", jsonObject.getString("engineerPhoneFlag"));
            // 预约权限：Y-有，N-否
            resultMap.put("preTimeFlag", jsonObject.getString("preTimeFlag"));
            // 重新预约权限：Y-有，N-否
            resultMap.put("rePreTimeFlag", jsonObject.getString("rePreTimeFlag"));
            // 关联CASE权限：Y-有，N-否
            resultMap.put("associateCaseFlag", jsonObject.getString("associateCaseFlag"));
        }
        return resultMap;
    }

    /**
     * 进入新建派工页面
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map<String, Object> addWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("faultRepairId", workOrderFilter.getRepairId());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderFilter.getServiceId()).addWorkOrder(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    returnMap.put("workOrderDto", workOrderDto);
                }
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString("faultRepair"),
                        RepairMapping.getOldAndNewPropertyMap(), RepairDto.class);
                returnMap.put("repairDto", repairDto);
            }
            if (jsonObject.containsKey("userMap")) {
                Map<String, String> userMap = JsonUtil.parseMap(jsonObject.getString("userMap"));
                returnMap.put("userMap", userMap);
            }
            if (jsonObject.containsKey("otherUserMap")) {
                Map<String, String> otherUserMap = JsonUtil.parseMap(jsonObject.getString("otherUserMap"));
                returnMap.put("otherUserMap", otherUserMap);
            }
            returnMap.put("modTime", jsonObject.getString("modTime"));
        }
        return returnMap;
    }

    /**
     * 新建派工提交
     *
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void addWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderDto, new HashMap<>());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderDto.getServiceId()).addWorkOrderSubmit(paramMap, userInfo, reqParam);
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
     * 进入接受派工页面
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map<String, Object> acceptWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", workOrderFilter.getWorkOrderId());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderFilter.getServiceId()).acceptWorkOrder(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    returnMap.put("workOrderDto", workOrderDto);
                }
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString("faultRepair"),
                        RepairMapping.getNewAndOldPropertyMap(), RepairDto.class);
                returnMap.put("repairDto", repairDto);
            }
        }
        return returnMap;
    }

    /**
     * 接受派工提交
     *
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void acceptWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderDto, new HashMap<>());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderDto.getServiceId()).acceptWorkOrderSubmit(paramMap, userInfo, reqParam);
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
     * 进入拒绝派工页面
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map<String, Object> refuseWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", workOrderFilter.getWorkOrderId());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderFilter.getServiceId()).refuseWorkOrder(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    returnMap.put("workOrderDto", workOrderDto);
                }
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString("faultRepair"),
                        RepairMapping.getNewAndOldPropertyMap(), RepairDto.class);
                returnMap.put("repairDto", repairDto);
            }
        }
        return returnMap;
    }

    /**
     * 拒绝派工提交
     *
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void refuseWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderDto, new HashMap<>());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderDto.getServiceId()).refuseWorkOrderSubmit(paramMap, userInfo, reqParam);
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
     * 进入工单预约页面
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map<String, Object> preBook(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", workOrderFilter.getWorkOrderId());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderFilter.getServiceId()).preBook(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    returnMap.put("workOrderDto", workOrderDto);
                }
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString("faultRepair"),
                        RepairMapping.getNewAndOldPropertyMap(), RepairDto.class);
                returnMap.put("repairDto", repairDto);
            }
        }
        return returnMap;
    }

    /**
     * 工单预约提交
     *
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void preBookSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderDto, new HashMap<>());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderDto.getServiceId()).preBookSubmit(paramMap, userInfo, reqParam);
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
     * 重进入重新预约页面
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map<String, Object> renewPreBook(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", workOrderFilter.getWorkOrderId());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderFilter.getServiceId()).renewPreBook(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    returnMap.put("workOrderDto", workOrderDto);
                }
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString("faultRepair"),
                        RepairMapping.getNewAndOldPropertyMap(), RepairDto.class);
                returnMap.put("repairDto", repairDto);
            }
        }
        return returnMap;
    }

    /**
     * 重新预约提交
     *
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void renewPreBookSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderDto, new HashMap<>());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderDto.getServiceId()).renewPreBookSubmit(paramMap, userInfo, reqParam);
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
     * 进入退回主管页面
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @author Qiugm
     * @date 2020-03-19
     * @returno
     */
    @Override
    public Map<String, Object> returnManager(WorkOrderFilter workOrderFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", workOrderFilter.getWorkOrderId());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderFilter.getServiceId()).returnManager(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    returnMap.put("workOrderDto", workOrderDto);
                }
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString("faultRepair"),
                        RepairMapping.getNewAndOldPropertyMap(), RepairDto.class);
                returnMap.put("repairDto", repairDto);
            }
        }
        return returnMap;
    }

    /**
     * 退回主管提交
     *
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void returnManagerSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderDto, new HashMap<>());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderDto.getServiceId()).returnManagerSubmit(paramMap, userInfo, reqParam);
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
     * 进入主动结束派工页面
     *
     * @param workOrderFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public Map<String, Object> activeEndWorkOrder(WorkOrderFilter workOrderFilter, UserInfo userInfo,
                                                  ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        paramMap.add("workOrderId", workOrderFilter.getWorkOrderId());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderFilter.getServiceId()).activeEndWorkOrder(paramMap, userInfo, reqParam);
        Map<String, Object> returnMap = new HashMap<>();
        if (StrUtil.isNotEmpty(handleResult) && !handleResult.contains(SendWoUtil.HANDLE_FAIL)) {
            JSONObject jsonObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            if (jsonObject.containsKey("workOrderRemote")) {
                WorkOrderDto workOrderDto = makeWorkOrderDto(jsonObject);
                if (workOrderDto != null) {
                    returnMap.put("workOrderDto", workOrderDto);
                }
            }
            if (jsonObject.containsKey("faultRepair")) {
                RepairDto repairDto = VariableConvertUtil.convertToNewEntity(jsonObject.getString("faultRepair"),
                        RepairMapping.getNewAndOldPropertyMap(), RepairDto.class);
                returnMap.put("repairDto", repairDto);
            }
        }
        return returnMap;
    }

    /**
     * 主动结束派工提交
     *
     * @param workOrderDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    @Override
    public void activeEndWorkOrderSubmit(WorkOrderDto workOrderDto, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(workOrderDto, new HashMap<>());
        String handleResult =
                workOrderStrategyFactory.getStrategy(
                        WorkOrderStrategyFactory.STRATEGY_CLASS_NAME_PREFIX +
                                workOrderDto.getServiceId()).activeEndWorkOrderSubmit(paramMap, userInfo, reqParam);
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
}
