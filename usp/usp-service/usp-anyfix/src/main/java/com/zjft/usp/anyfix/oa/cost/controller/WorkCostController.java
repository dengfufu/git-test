package com.zjft.usp.anyfix.oa.cost.controller;

import com.zjft.usp.anyfix.oa.cost.composite.WorkCostCompoService;
import com.zjft.usp.anyfix.oa.cost.dto.WorkCostDto;
import com.zjft.usp.anyfix.oa.cost.filter.WorkCostFilter;
import com.zjft.usp.common.annotation.LoginClient;
import com.zjft.usp.common.model.OauthClient;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * OA报销系统请求工单数据
 *
 * @Author: JFZOU
 * @Date: 2020-02-29 9:48
 */
@Api(tags = "OA报销系统请求工单报销数据")
@RestController
@RequestMapping(value = "/work-cost")
@Slf4j
public class WorkCostController {
    @Autowired
    private WorkCostCompoService workCostCompoService;

    @ApiOperation(value = "openApi: 根据条件查询工程师的工单信息")
    @PostMapping(value = "/queryByMobile")
    public Result<List<WorkCostDto>> queryByMobile(@RequestBody Map<String, Object> reqParam,
                                                   @LoginClient(isFull = true) OauthClient oauthClient) {

        log.info("open-api: 查询工程师的报销工单,{} ", reqParam);
        boolean testBoolean = false;
        if(reqParam.get("test") != null){
            testBoolean = (Boolean) reqParam.get("test");
        }
        String userName = (String) reqParam.get("username");
        String mobile = (String) reqParam.get("mobile");
        String month = (String) reqParam.get("month");
        Assert.notNull(userName, "username 不能为空");
        Assert.notNull(mobile, "mobile 不能为空");
        Assert.notNull(month, "month 不能为空");

        Long corpId = oauthClient.getCorpId();
        String additionalInformation = oauthClient.getAdditionalInformation();
        log.info("open-api: 查询工程师的报销工单:clientId:{},corpId:{},additionalInformation:{}, username:{},mobile:{}",
                oauthClient.getClientId(), corpId, additionalInformation, userName, mobile);

        WorkCostFilter workCostFilter = new WorkCostFilter();
        workCostFilter.setMobile(mobile);
        workCostFilter.setUserName(userName);
        workCostFilter.setMonth(month);

        /**测试是否可以访问网关*/
        if (testBoolean) {
            log.info("open-api: 查询工程师工单,测试成功");
            return Result.succeed("测试模式: 查询工程师工单,测试成功,系统中存在该用户信息，姓名:" + userName + ",手机号:" + mobile+",报销所属月份："+month);
        }else{
            return Result.succeed(workCostCompoService.queryByMobile(workCostFilter));
        }

    }
}
