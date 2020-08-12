package com.zjft.usp.anyfix.work.fee.controller;


import com.zjft.usp.anyfix.work.fee.dto.WorkFeeAssortDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeAssortFilter;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeAssortDefineService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 分类费用定义 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@RestController
@RequestMapping("/work-fee-assort-define")
public class WorkFeeAssortDefineController {

    @Autowired
    private WorkFeeAssortDefineService workFeeAssortDefineService;

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/query")
    public Result<ListWrapper<WorkFeeAssortDto>> query(@RequestBody WorkFeeAssortFilter workFeeAssortFilter,
                                                      @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workFeeAssortFilter.getServiceCorp())) {
            workFeeAssortFilter.setServiceCorp(reqParam.getCorpId());
        }
        return Result.succeed(workFeeAssortDefineService.query(workFeeAssortFilter));
    }

    @ApiOperation(value = "添加分类费用定义")
    @PostMapping(value = "/add")
    public Result add(@RequestBody WorkFeeAssortDto workFeeAssortDto,
                      @LoginUser UserInfo userInfo,
                      @CommonReqParam ReqParam reqParam) {
        this.workFeeAssortDefineService.add(workFeeAssortDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiOperation(value = "更新")
    @PostMapping(value = "/update")
    public Result update(@RequestBody WorkFeeAssortDto workFeeAssortDto,
                         @LoginUser UserInfo userInfo,
                         @CommonReqParam ReqParam reqParam) {
        this.workFeeAssortDefineService.update(workFeeAssortDto, userInfo.getUserId(), reqParam.getCorpId());
        return Result.succeed();
    }

    @ApiModelProperty(value = "查看")
    @GetMapping(value = "/{assortId}")
    public Result<WorkFeeAssortDto> findDetail(@PathVariable("assortId") Long assortId) {
        return Result.succeed(this.workFeeAssortDefineService.getDtoById(assortId));
    }

}
