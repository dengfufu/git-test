package com.zjft.usp.wms.business.consign.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.consign.composite.ConsignCompoService;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.filter.ConsignFilter;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 发货基本信息共用表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-22
 */
@RestController
@RequestMapping("/consign")
public class ConsignController {

    @Autowired
    private ConsignCompoService consignCompoService;

    @ApiOperation(value = "发货申请提交")
    @PostMapping(value = "/add")
    public Result<Object> add(@RequestBody ConsignMainDto consignMainDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.consignCompoService.add(consignMainDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "确认收货")
    @PostMapping(value = "/receive")
    public Result<Object> receive(@RequestBody ConsignMainDto consignMainDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        this.consignCompoService.receive(consignMainDto,userInfo,reqParam);
        return Result.succeed();
    }

    @ApiOperation(value = "出库发货查看详情")
    @GetMapping(value = "/detail/{formDetailId}")
    public Result<List<ConsignDetailDto>> findByFormDetailId(@PathVariable Long formDetailId,@CommonReqParam ReqParam reqParam) {
        return Result.succeed(this.consignCompoService.findByFormDetailId(formDetailId,reqParam));
    }

    @ApiOperation(value = "分页查询发货列表")
    @PostMapping(value = "/list")
    public Result<ListWrapper<ConsignDetailDto>> listByPage(@RequestBody ConsignFilter consignFilter, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam) {
        consignFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(this.consignCompoService.listByPage(consignFilter,userInfo,reqParam));
    }
}
