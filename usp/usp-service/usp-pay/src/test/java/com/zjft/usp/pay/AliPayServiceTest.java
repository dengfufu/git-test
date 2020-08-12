package com.zjft.usp.pay;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.response.AlipayTradeCloseResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zjft.usp.pay.business.compo.PayCompoService;
import com.zjft.usp.pay.business.service.AccountInfoService;
import com.zjft.usp.pay.business.service.AliPayService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

/**
 * @author: CK
 * @create: 2020-05-20 14:27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AliPayServiceTest {

    @Autowired
    AliPayService aliPayService;

    @Autowired
    AccountInfoService accountInfoService;

    @Autowired
    PayCompoService payCompoService;

    @Test
    public void aliPayPcTest() throws AlipayApiException {
        String form = this.aliPayService.aliPayPc(
                "20170730444",
                "标题",
                "内容",
                "10",
                new Date());
        System.out.println(form);
    }

    /**
     * {"alipay_trade_query_response":{"code":"10000","msg":"Success","buyer_logon_id":"ljf***@sandbox.com","buyer_pay_amount":"0.00","buyer_user_id":"2088102178965924","buyer_user_type":"PRIVATE","invoice_amount":"0.00","out_trade_no":"2222223","point_amount":"0.00","receipt_amount":"0.00","send_pay_date":"2020-05-26 17:09:35","total_amount":"20.00","trade_no":"2020052622001465920500929641","trade_status":"TRADE_SUCCESS"},"sign":"W/GLXKGKZBd6I4PTqd4dXauwpEywk1RuflHZPt7tgoZOSZgFuLE474wAilDCi2EJDbcfnNGpKpaMM59uhhsbDffjfitTcBz93jxVV1mevAvNUloNfQAc2szRbYeFz/88NQR9BruZlqb3aFl84MvMgAZTWJ3mUCzS/QEaoEoE4hqOyMu+rDIr/WPkWLrsKqr6zj2+EW+ZUOnHF6AMBs4i10XLkZPLGstvHiWsEQ7pFgKbDgXxwgPUHISrZxQBzbaKj9hvCVgPdzHpj4ZkhIcockuEijqniFti9La3/nX7BWiGyU06rf47sE7W1wTMAf8570DPqAPMIQJY8ztRNnnu0Q=="}
     * {"alipay_trade_query_response":{"code":"40004","msg":"Business Failed","sub_code":"ACQ.TRADE_NOT_EXIST","sub_msg":"交易不存在","buyer_pay_amount":"0.00","invoice_amount":"0.00","out_trade_no":"22222233","point_amount":"0.00","receipt_amount":"0.00"},"sign":"sCBFDj73jaSktDhPL0vqu7J/wEtcUzyzVJ/1wAoey3Bhg2fs1/S74z5QqRQJ3BoPNI6Q54RJqGlBtiZaskH+/4Xl3ndKOhK//EIwdnCABXwmiLzoJncv8Eirrzq4pNaYXr22OYkNIU1PM483YefZ1t3nIOkphWQ93OZ3JPfTUAmiPQEHL9gO8WxU2tS82ne4N80+zKwH/Y0Vy5CN2CjgXKSzsY5A7dUpStk7/DdWnTOPUgNjTJRZjnNa5KQHpcRZJzYX9dZflR6VKUtr0vKJBeZQLJM1AOZIHoFRypzm5fkfIFzyx3TytMGPyqIGPOxQ87hy5kr45xmGa0YnXYO1vg=="}
     *
     * @throws AlipayApiException
     */
    @Test
    public void aliPayQueryTest() throws AlipayApiException {
        AlipayTradeQueryResponse result = this.aliPayService.aliPayQuery(
                "1275770857578303489",
                "");
        System.out.println(JSON.toJSONString(result));
    }

    /**
     * {"alipay_trade_refund_response":{"code":"10000","msg":"Success","buyer_logon_id":"ljf***@sandbox.com","buyer_user_id":"2088102178965924","fund_change":"N","gmt_refund_pay":"2020-05-28 09:32:52","out_trade_no":"2222223","refund_fee":"2.00","send_back_fee":"0.00","trade_no":"2020052622001465920500929641"},"sign":"i8aHhufp4Dkwm+MDpEA7D52NTFHXtfb+XHIfFwQnLU0+UeKHVrgvc0PG+7SBP9G3kiupjScsQN7/c8TxRo53sEEhOntSV33q5U0wqhcNreOlHO0g0hKl81iGG3KQ03An0OSn18ZnGvXx2bxi1eZ66Hx5YT31BulpwS/jXGD24wNMaMeyn64XJMJQNt378FpeTHNvjnJ9PaB9s4KLN2fWjE1XBT1RsHJHMeNd6/QUfsl1A18OgU7mu8orCvAQ78R/KidksniPcMnd/SGMoKi6+GpjCd7r2xyWRM54sPqHwmsyieyurZhi87l95pIYYBIGwyDLEj7+jpTyMnYruoieYQ=="}
     * {"alipay_trade_refund_response":{"code":"40004","msg":"Business Failed","sub_code":"ACQ.TRADE_NOT_EXIST","sub_msg":"交易不存在","refund_fee":"0.00","send_back_fee":"0.00"},"sign":"hTWQkViRT+5HAOfU/xjX07sIsZt7DSA+aEiKdszb4KKdZn0M3wnwVTqrLj8OcqgMHlj+rwRcEiTM4nKEmjdCCxobbTltykTX/SbRodZ5jcH/DEZ4b5j+q1NVaZKDHz8YX+GzlFse76kXa46uQ8sEuvwC2angoyA+ng+YGqr92YAXv+cIYU2TjNBC5+PUcVykh6+DBeVsWZpGFl+GxF+w27TK8aKGZtfK+LvWetSuAG3pIhg+B7ltfecbxnz6RqxOZt4XTftb2Tv42k1bTchICrSNWiWzm7VZsRAbrTFNgbwXxZCiaHrnbbFquBOOOoRkNqhpM8UJfAKuOYxRB0Kygw=="}
     *
     * @throws AlipayApiException
     */
    @Test
    public void aliPayRefundTest() throws AlipayApiException {
        String form = this.aliPayService.aliPayRefund(
                "2222223",
                "",
                "20200730444",
                "2",
                "测试");
        System.out.println(form);
    }

    /**
     * {"alipay_trade_fastpay_refund_query_response":{"code":"10000","msg":"Success","out_request_no":"20200730444","out_trade_no":"2222223","refund_amount":"2.00","total_amount":"20.00","trade_no":"2020052622001465920500929641"},"sign":"YI/aSLDsdT4CmBM3IpkYJTBPe9fvMmIuWYV682axblzOWZVGJwp7jzv/NGuQD2Zz0Ag5el7V2bro69/D1qa6FhnKWU/WtQNXDUx/LYPDN6djcaJrxvkc1befULYVtn3DIgBnPlW5gwbpe7udABIlF92g+OIKhcyj7kgjvDnvL0yI7kexBNWcNMzNN/FUvfA4lW89USIJUskcblYPO1XgRmlspLuAwb/IrAhfGc7GRn6MiT+EdFpDVsr1NEOhJLBSR15vaC+G/86zCwLnnfDuP1NC1oUNCacY9yenjqeOvbkC5VgP/Cxn7+JgSzvXKqhGOUBoKMGA5DbW+5i5h3rO2Q=="}
     *
     * @throws AlipayApiException
     */
    @Test
    public void aliPayRefundQueryTest() throws AlipayApiException {
        String form = this.aliPayService.aliPayRefundQuery(
                "2222223",
                "",
                "20200730444");
        System.out.println(form);
    }

    /**
     * {"alipay_trade_close_response":{"code":"40004","msg":"Business Failed","sub_code":"ACQ.TRADE_STATUS_ERROR","sub_msg":"交易状态不合法","out_trade_no":"2222223"},"sign":"gsGJUY7YoT+/TIV+c+jqMfHo7EkdL8/5iRSqFxwo3py1siFK6At7hBr70yfqglf2fuGgSAiMxz3wh3OtQAiS1tBLRvpGzzxmYViQPoWVxTFYOr3XPb7C5FNQkbojDeaTHS8VzPn2D6JDpKDHD8X6HxnnCxOpxdlObm0009rOys3x9UGeAUN6CouoxHNYb9We9qGD0qKHViAQTJnShLensio/N99AZcZSf+4BufKr1xBBUmkguUaFfHrYyDa1Xaoev14FzT8gf6OBBJHOoAbmjBKqGPxd4hrb5XlDnuYsAS+sJ9V5zE7q334jWY+LuCxbOjTQleutp0xGZOVBKOv03Q=="}
     * {"alipay_trade_close_response":{"code":"10000","msg":"Success","out_trade_no":"222222","trade_no":"2020052622001465920500929640"},"sign":"oNmGXi/iJ0w7tQ0ZtFJX3Fn9R0YI67r0gFkNWLiCD82McArf5d3ER75v0w+A2MKGrKj5RMSlSH4SbVXYzST5afP4IPfoSfraijlctlL7fhdB2A9UttINpp2/LijEVKyBMSPyxQ6wzPvf9vSOsN3tVHt58Uo6y6CiB3mI1237myR0qofr5jiZbs4gsBnIKo9P6ZB94cAp2j6ARJmyAOYjdJlIogpe20opcwyiMpvK8rBGgI63o1YJn2ihaNuklrsx75QPNMiz6gL2iratClCtdvfcXdbueNXskNbQT4bdyH9Q3KwvSgM+7gAv/ULwvHnJpglAm0rOXzJNng+YsHKAgQ=="}
     *
     * @throws AlipayApiException
     */
    @Test
    public void aliPayCloseTest() throws AlipayApiException {
        AlipayTradeCloseResponse response = this.aliPayService.aliPayClose(
                "212121211",
                "");
        if (response.isSuccess()) {
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
            if (response.getSubCode().equals("ACQ.SYSTEM_ERROR")) {
                System.out.println("系统异常,需要重新发起请求");
            }
            if (response.getSubCode().equals("ACQ.TRADE_NOT_EXIST")) {
                System.out.println("交易不存在,检查传入的交易号和外部订单号是否正确，修改后再重新发起");
            }
            if (response.getSubCode().equals("交易状态不合法")) {
                System.out.println("交易状态不合法,检查当前交易的状态是不是等待买家付款，只有等待买家付款状态下才能发起交易关闭");
            }
            if (response.getSubCode().equals("ACQ.INVALID_PARAMETER")) {
                System.out.println("参数无效,检查请求参数，修改后重新发起请求");
            }
        }
        System.out.println(response);
    }

    @Test
    public void corpInitWallet() {
        Long corpId = 1L;
        this.accountInfoService.corpInitWallet(corpId);
    }

    @Test
    public void PayByAliPayPcTest() throws AlipayApiException {
//        PaymentApplyDTO paymentApplyDTO = new PaymentApplyDTO();
//        paymentApplyDTO.setPayerAccountId(11111L); // 付款方
//        paymentApplyDTO.setPayeeAccountId(22222L);// 收款方
//        paymentApplyDTO.setOrderId(1261525556245688321L); // 结算单号
//        paymentApplyDTO.setOrderName("五月结算"); //结算单名
//        paymentApplyDTO.setOrderAmount(new BigDecimal("10.15")); // 结算费用
//        String form = payCompoService.payByAliPayPc(paymentApplyDTO, 11111L);
//        System.out.println(form);
    }

}
