package com.zjft.usp.anyfix.work.fee.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.anyfix.work.fee.dto.ServiceItemFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.ServiceItemFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.service.ServiceItemFeeRuleService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 工单服务项目结算规则 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Api(tags = "工单服务项目结算规则")
@RestController
@RequestMapping("/service-item-fee-rule")
public class ServiceItemFeeRuleController {

    @Autowired
    private ServiceItemFeeRuleService serviceItemFeeRuleService;

    @ApiOperation("分页查询")
    @PostMapping("/query")
    public Result<ListWrapper<ServiceItemFeeRuleDto>> query(@RequestBody ServiceItemFeeRuleFilter serviceItemFeeRuleFilter,
                                                            @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceItemFeeRuleFilter.getServiceCorp())) {
            serviceItemFeeRuleFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(serviceItemFeeRuleService.query(serviceItemFeeRuleFilter));
    }

    @ApiOperation("添加")
    @PostMapping("/add")
    public Result add(@RequestBody ServiceItemFeeRuleDto serviceItemFeeRuleDto, @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(serviceItemFeeRuleDto.getServiceCorp())) {
            serviceItemFeeRuleDto.setServiceCorp(reqParam.getCorpId());
        }
        serviceItemFeeRuleService.add(serviceItemFeeRuleDto);
        return Result.succeed();
    }

    @ApiOperation("更新")
    @PostMapping("/update")
    public Result update(@RequestBody ServiceItemFeeRuleDto serviceItemFeeRuleDto, @CommonReqParam ReqParam reqParam) {
        serviceItemFeeRuleService.update(serviceItemFeeRuleDto);
        return Result.succeed();
    }

    @ApiOperation("删除")
    @DeleteMapping("/{ruleId}")
    public Result delete(@PathVariable("ruleId") Long ruleId) {
        this.serviceItemFeeRuleService.deleteById(ruleId);
        return Result.succeed();
    }

    @ApiModelProperty("根据工单匹配服务项目费用")
    @PostMapping("/matchRule")
    public Result<ServiceItemFeeRuleDto> matchRule(@RequestBody WorkDto workDto) {
        List<ServiceItemFeeRuleDto> list = this.serviceItemFeeRuleService.matchServiceItemFeeRule(workDto);
        if (CollectionUtil.isNotEmpty(list)) {
            return Result.succeed(list.get(0));
        }
        return Result.succeed();
    }

}
