package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.user.dto.UserAlipayDto;
import com.zjft.usp.uas.user.model.UserAlipay;
import com.zjft.usp.uas.user.service.UserAlipayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @description: alipay前端控制器
 * @author chenxiaod
 * @date 2019/8/8 11:39
 */
@RestController
public class UserAlipayController {

    @Autowired
    private UserAlipayService userAlipayService;

    @RequestMapping(value="/alipayBind", method = RequestMethod.POST)
    public Result alipayBind(@RequestBody UserAlipayDto userAlipayDto) {
        UserAlipay UserAlipay = new UserAlipay();
        UserAlipay.setAlipayUserId(userAlipayDto.getAlipayUserId());
        UserAlipay.setAlipayLogonId(userAlipayDto.getAlipayLogonId());
        UserAlipay.setUserId(userAlipayDto.getUserId());
        UserAlipay.setAddTime(new Date(System.currentTimeMillis()));
        userAlipayService.alipayBind(UserAlipay);
        return Result.succeed();
    }
}
