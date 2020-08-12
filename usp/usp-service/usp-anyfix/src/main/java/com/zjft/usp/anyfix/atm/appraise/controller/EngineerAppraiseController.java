package com.zjft.usp.anyfix.atm.appraise.controller;

import com.zjft.usp.anyfix.atm.appraise.composite.EngineerAppraiseCompoService;
import com.zjft.usp.anyfix.atm.appraise.dto.EngineerAppraiseDto;
import com.zjft.usp.anyfix.atm.appraise.filter.EngineerAppraiseFilter;
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
 * ATM工程师绩效考核系统请求工单数据
 * 后续如果工单数量大幅度增加，需要换一种方案，建议在ATM数据库中保留工单副本，减少网络传输
 * @Author: JFZOU
 * @Date: 2020-03-09 9:48
 */
@Api(tags = "ATM工程师绩效考核系统请求工单数据")
@RestController
@RequestMapping(value = "/atm-appraise-engineer")
@Slf4j
public class EngineerAppraiseController {
    @Autowired
    private EngineerAppraiseCompoService engineerAppraiseCompoService;

    @ApiOperation(value = "openApi: 根据条件查询工程师的符合条件的绩效考核工单信息")
    @PostMapping(value = "/queryByMonth")
    public Result<List<EngineerAppraiseDto>> queryByMonth(@RequestBody Map<String, Object> reqParam,
                                                          @LoginClient(isFull = true) OauthClient oauthClient) {
        log.info("open-api: 查询工程师的绩效考核工单,{} ", reqParam);
        boolean testBoolean = false;
        if (reqParam.get("test") != null) {
            testBoolean = (Boolean) reqParam.get("test");
        }
        String finishDate = (String) reqParam.get("finishDate");
        Assert.notNull(finishDate, "finishDate 不能为空");

        Long corpId = oauthClient.getCorpId();
        String additionalInformation = oauthClient.getAdditionalInformation();
        log.info("open-api: 查询工程师报销工单:clientId:{},corpId:{},additionalInformation:{}, finishDate:{}",
                oauthClient.getClientId(), corpId, additionalInformation, finishDate);

        EngineerAppraiseFilter engineerAppraiseFilter = new EngineerAppraiseFilter();
        engineerAppraiseFilter.setCorpId(corpId);
        engineerAppraiseFilter.setDate(finishDate);

        /**测试是否可以访问网关*/
        if (testBoolean) {
            log.info("open-api: 查询工程师绩效考核工单,测试成功");
            return Result.succeed("测试模式: 查询工程师绩效考核工单,测试成功,所属月份日期：" + finishDate);
        } else {
            return Result.succeed(engineerAppraiseCompoService.queryByMonth(engineerAppraiseFilter));
        }

    }
}

