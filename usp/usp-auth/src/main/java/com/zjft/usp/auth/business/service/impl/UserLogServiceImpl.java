package com.zjft.usp.auth.business.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.zjft.usp.auth.business.service.UserLogService;
import com.zjft.usp.common.feign.service.UserFeignService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户操作日志
 *
 * @author zgpi
 * @date 2020/5/25 14:36
 */
@Service
public class UserLogServiceImpl implements UserLogService {

    @Resource
    private UserFeignService userFeignService;

    /**
     * 保存登录成功日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/25 14:37
     **/
    @Override
    public void saveLogonSuccess(Long userId, String mobile, String extraParams) {
        String json = "";
        if (StrUtil.isNotBlank(extraParams)) {
            JSONObject jsonObject = new JSONObject(extraParams);
            jsonObject.put("userId", userId);
            jsonObject.put("mobile", mobile);
            json = JSON.toJSONString(jsonObject);
        }
        userFeignService.saveLogonSuccessLog(json);
    }

    /**
     * 保存自动登录成功日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/26 15:23
     **/
    @Override
    public void saveAutoLogonSuccess(Long userId, String mobile, String extraParams) {
        String json = "";
        if (StrUtil.isNotBlank(extraParams)) {
            JSONObject jsonObject = new JSONObject(extraParams);
            jsonObject.put("userId", userId);
            jsonObject.put("mobile", mobile);
            json = JSON.toJSONString(jsonObject);
        }
        userFeignService.saveAutoLogonSuccessLog(json);
    }

    /**
     * 保存登录密码错误日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/25 14:38
     **/
    @Override
    public void saveLogonPwdFail(Long userId, String mobile, String extraParams) {
        String json = "";
        if (StrUtil.isNotBlank(extraParams)) {
            JSONObject jsonObject = new JSONObject(extraParams);
            jsonObject.put("userId", userId);
            jsonObject.put("mobile", mobile);
            json = JSON.toJSONString(jsonObject);
        }
        userFeignService.saveLogonPwdFailLog(json);
    }

    /**
     * 保存登出日志
     *
     * @param userId
     * @param mobile
     * @param extraParams
     * @return
     * @author zgpi
     * @date 2020/5/26 15:22
     **/
    @Override
    public void saveLogoutLog(Long userId, String mobile, String extraParams) {
        String json = "";
        if (StrUtil.isNotBlank(extraParams)) {
            JSONObject jsonObject = new JSONObject(extraParams);
            jsonObject.put("userId", userId);
            jsonObject.put("mobile", mobile);
            json = JSON.toJSONString(jsonObject);
        }
        userFeignService.saveLogoutLog(json);
    }
}
