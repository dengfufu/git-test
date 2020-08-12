package com.zjft.usp.uas.user.controller;

import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.user.dto.UserWxDto;
import com.zjft.usp.uas.user.model.UserWx;
import com.zjft.usp.uas.user.service.UserWxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * @description: wx前端控制器
 * @author chenxiaod
 * @date 2019/8/8 18:40
 */
@RestController
@RequestMapping("/wx")
public class UserWxController {

    @Autowired
    private UserWxService userWxService;

    @RequestMapping(value="/wxpayBind", method = RequestMethod.GET)
    public Result wxpayBind(@RequestBody UserWxDto userWxDto) {
        UserWx userWx = new UserWx();
        userWx.setOpenId(userWxDto.getOpenId());
        userWx.setUnionId(userWxDto.getUnionId());
        userWx.setUserId(Long.valueOf(userWxDto.getUserId()));
        userWx.setAddTime(new Date(System.currentTimeMillis()));
        userWxService.wxpayBind(userWx);
        return Result.succeed();
    }

    @RequestMapping(value="/openid", method = RequestMethod.GET)
    public Result getOpenId(@LoginUser UserInfo userInfo) {
        return Result.succeed(userWxService.getOpenidByUserid(userInfo.getUserId()));
    }


    @RequestMapping(value="/feign/openid", method = RequestMethod.GET)
    public String getFeignOpenIdByUserid(@RequestParam("userId") Long userId) {
        return userWxService.getOpenidByUserid(userId);
    }

}
