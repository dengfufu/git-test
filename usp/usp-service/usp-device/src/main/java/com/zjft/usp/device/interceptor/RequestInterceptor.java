package com.zjft.usp.device.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import com.zjft.usp.device.config.MyContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求拦截器
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/17 9:42 上午
 **/
@Component
public class RequestInterceptor implements HandlerInterceptor {

    public static final String CORP_ID = "corpId";

    @Autowired
    private MyContext myContext;

    /**
     * 目标方法执行之前执行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String commonParamJson = request.getHeader("Common-Params");
        JSONObject jsonObject = new JSONObject(commonParamJson);
        String corpId = StrUtil.trimToEmpty(jsonObject.getStr(CORP_ID));
        if(StrUtil.isNotBlank(corpId)){
            myContext.setCurrentTenantId(Long.parseLong(corpId));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

    }
}
