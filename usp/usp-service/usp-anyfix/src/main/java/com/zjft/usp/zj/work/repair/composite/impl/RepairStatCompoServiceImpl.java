package com.zjft.usp.zj.work.repair.composite.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.work.repair.composite.RepairStatCompoService;
import com.zjft.usp.zj.work.repair.filter.RepairFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.List;

/**
 * 老平台报修统计聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-04-01 16:12
 **/
@Slf4j
@Service
public class RepairStatCompoServiceImpl implements RepairStatCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.repair.countRepairStatusUrl}")
    private String countRepairStatusUrl;

    /**
     * 统计各个状态的工单数量
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-01
     */
    @Override
    public List<WorkStatDto> countRepairStatus(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(repairFilter);
        String handleResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, countRepairStatusUrl);
        List<WorkStatDto> workStatDtoList = null;
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            workStatDtoList = JsonUtil.parseArray(resultObject.getString("workStatRemoteList"), WorkStatDto.class);
        }
        return workStatDtoList;
    }
}
