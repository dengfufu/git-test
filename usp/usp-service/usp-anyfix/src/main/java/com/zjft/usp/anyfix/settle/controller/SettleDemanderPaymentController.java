package com.zjft.usp.anyfix.settle.controller;


import com.zjft.usp.anyfix.settle.composite.SettleDemanderPaymentCompoService;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderPaymentDto;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 委托商结算单付款表 前端控制器
 * </p>
 *
 * @author canlei
 * @since 2020-04-28
 */
@RestController
@RequestMapping("/settle-demander-payment")
public class SettleDemanderPaymentController {

    @Autowired
    private SettleDemanderPaymentCompoService settleDemanderPaymentCompoService;

    @ApiOperation(value = "结算单详情")
    @GetMapping(value = "/detail/{settleId}")
    public Result<SettleDemanderPaymentDto> viewSettleDemanderPayment(@PathVariable("settleId") Long settleId) {
        return Result.succeed(settleDemanderPaymentCompoService.viewSettleDemanderPayment(settleId));
    }

    @ApiOperation(value = "结算单开票")
    @PostMapping(value = "/invoice")
    public Result invoice(@RequestBody SettleDemanderPaymentDto settleDemanderPaymentDto,
                          @LoginUser UserInfo userInfo) {
        settleDemanderPaymentCompoService.invoice(settleDemanderPaymentDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "结算单付款")
    @PostMapping(value = "/pay")
    public Result pay(@RequestBody SettleDemanderPaymentDto settleDemanderPaymentDto,
                      @LoginUser UserInfo userInfo) {
        settleDemanderPaymentCompoService.payOffline(settleDemanderPaymentDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation(value = "结算单收款")
    @PostMapping(value = "/receipt")
    public Result receipt(@RequestBody SettleDemanderPaymentDto settleDemanderPaymentDto,
                          @LoginUser UserInfo userInfo) {
        settleDemanderPaymentCompoService.receipt(settleDemanderPaymentDto, userInfo.getUserId());
        return Result.succeed();
    }
}
