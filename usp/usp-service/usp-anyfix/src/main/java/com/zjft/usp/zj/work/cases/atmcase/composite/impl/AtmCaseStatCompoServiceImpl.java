package com.zjft.usp.zj.work.cases.atmcase.composite.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.utils.SendWoUtil;
import com.zjft.usp.zj.common.utils.VariableConvertUtil;
import com.zjft.usp.zj.work.cases.atmcase.composite.AtmCaseStatCompoService;
import com.zjft.usp.zj.work.cases.atmcase.filter.AtmCaseFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import javax.annotation.Resource;
import java.util.List;

/**
 * ATM机CASE统计聚合实现类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-31 14:46
 **/
@Slf4j
@Service
public class AtmCaseStatCompoServiceImpl implements AtmCaseStatCompoService {
    @Resource
    private SendWoUtil sendWoUtil;
    @Value("${wo.atmcase.countCaseStatusUrl}")
    private String countCaseStatusUrl;

    /**
     * 统计各个状态的工单数量
     *
     * @param atmCaseFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-31
     */
    @Override
    public List<WorkStatDto> countCaseStatus(AtmCaseFilter atmCaseFilter, UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = VariableConvertUtil.convertToOldEntity(atmCaseFilter);
        String handleResult = sendWoUtil.postToWo(userInfo, reqParam, paramMap, countCaseStatusUrl);
        List<WorkStatDto> workStatDtoList = null;
        if (StrUtil.isNotEmpty(handleResult) && handleResult.matches(StatusCodeConstants.JSON_REGEX)) {
            JSONObject resultObject = JsonUtil.parseObject(handleResult, JSONObject.class);
            workStatDtoList = JsonUtil.parseArray(resultObject.getString("workStatRemoteList"), WorkStatDto.class);
        }
        return workStatDtoList;
    }

}
