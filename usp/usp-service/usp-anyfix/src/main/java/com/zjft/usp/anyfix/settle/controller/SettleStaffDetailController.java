package com.zjft.usp.anyfix.settle.controller;


import com.zjft.usp.anyfix.settle.dto.SettleStaffDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffDetailFilter;
import com.zjft.usp.anyfix.settle.model.SettleStaffDetail;
import com.zjft.usp.anyfix.settle.service.SettleStaffDetailService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 员工结算单明细 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
@Api(tags = "员工结算单明细")
@RestController
@RequestMapping("/settle-staff-detail")
public class SettleStaffDetailController {

    @Autowired
    private SettleStaffDetailService settleStaffDetailService;

    @ApiOperation(value = "查询员工结算单明细")
    @PostMapping(value = "/query")
    public Result<ListWrapper<SettleStaffDetailDto>> query(SettleStaffDetailFilter settleStaffDetailFilter, @CommonReqParam ReqParam reqParam){
        ListWrapper<SettleStaffDetailDto> listWrapper = this.settleStaffDetailService.query(settleStaffDetailFilter, reqParam);
        return Result.succeed(listWrapper);
    }

}
