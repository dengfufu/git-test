package com.zjft.usp.anyfix.settle.controller.feign;

import com.zjft.usp.anyfix.settle.composite.SettleDemanderPaymentCompoService;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderOnlinePaymentDto;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/feign/settle-demander-payment")
public class SettleDemanderPaymentFeignController {

    @Autowired
    private SettleDemanderPaymentCompoService settleDemanderPaymentCompoService;

    @ApiOperation(value = "结算单付款-在线支付")
    @PostMapping(value = "/pay-online")
    public Result payOnline(@RequestBody SettleDemanderOnlinePaymentDto settleDemanderOnlinePaymentDto) {
        log.info("feign:payOnline:{}", settleDemanderOnlinePaymentDto);
        settleDemanderPaymentCompoService.payOnline(settleDemanderOnlinePaymentDto);
        return Result.succeed();
    }

}
