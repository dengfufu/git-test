package com.zjft.usp.zj.common.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.anyfix.common.feign.dto.CorpUserDto;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.RestTemplateUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.service.UasFeignService;
import com.zjft.usp.zj.common.constant.RedisConstants;
import com.zjft.usp.zj.common.constant.StatusCodeConstants;
import com.zjft.usp.zj.common.dto.WoResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 向WO应用发送请求工具类
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-14 11:04
 **/
@Slf4j
@Component
@RefreshScope
public class SendWoUtil {
    @Resource
    private RedisRepository redisRepository;
    @Resource
    private UasFeignService uasFeignService;
    @Value("${wo.loginMaxNum}")
    private int loginMaxNum;
    @Value("${wo.loginUrl}")
    private String loginUrl;
    @Value("${wo.token}")
    private String token;
    private static final String NO_LOGIN = "NO_LOGIN";
    public static final String LOGIN_FAIL = "login fail";
    public static final String HANDLE_FAIL = "handle fail";
    private static final String USP = "USP";


    /**
     * 登录WO应用
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-14
     */
    public String loginWo(UserInfo userInfo, ReqParam reqParam) {
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<>();
        // 老平台用户Id
        String account = findAccount(userInfo, reqParam);
        if (StrUtil.isEmpty(account)) {
            log.info("无法处理，第三方系统用户id为空");
            return LOGIN_FAIL;
        }
        paramMap.add("loginInfo.userId", account);
        paramMap.add("loginInfo.loginType", USP);

        try {
            log.info(account + "登录WO应用" + loginUrl + "开始：" + DateUtil.now());
            ResponseEntity response = RestTemplateUtil.postForm(loginUrl, paramMap, null);
            log.info(account + "登录WO应用" + loginUrl + "结束：" + DateUtil.now());
            if (response.getHeaders().containsKey("Set-Cookie")) {
                String sessionId = response.getHeaders().get("Set-Cookie").get(0);
                redisRepository.set(RedisConstants.getWoUserSessionId(reqParam.getCorpId(), userInfo.getUserId()),
                        sessionId);
                return sessionId;
            } else {
                return LOGIN_FAIL;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Read timed out")) {
                throw new AppException("系统服务忙，请重试");
            } else if (e.getMessage().contains("connect timed out")) {
                throw new AppException("连接服务器超时，请重试");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new AppException("服务器拒绝访问，请重试");
            } else if (e.getMessage().contains("I/O error")) {
                throw new AppException("出现例外异常，请重试");
            } else {
                throw new AppException(e.getMessage());
            }
        }
    }

    /**
     * 向WO应用发送请求
     *
     * @param userInfo
     * @param reqParam
     * @param paramMap
     * @param url
     * @return
     * @author Qiugm
     * @date 2020-03-14
     */
    public String postToWo(UserInfo userInfo, ReqParam reqParam, MultiValueMap<String, Object> paramMap, String url) {
        return postToWo(userInfo, reqParam, paramMap, url, 0);
    }

    /**
     * 向WO应用发送请求
     *
     * @param userInfo
     * @param reqParam
     * @param url
     * @return
     * @author Qiugm
     * @date 2020-03-14
     */
    public String postToWo(UserInfo userInfo, ReqParam reqParam, String url) {
        return postToWo(userInfo, reqParam, null, url, 0);
    }

    /**
     * 向WO应用发送请求
     *
     * @param userInfo
     * @param reqParam
     * @param paramMap
     * @param url
     * @param loginCount
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    public String postToWo(UserInfo userInfo, ReqParam reqParam, MultiValueMap<String, Object> paramMap,
                           String url, int loginCount) {
        String sessionId =
                StrUtil.trimToEmpty((String) redisRepository.get(RedisConstants.getWoUserSessionId(reqParam.getCorpId(),
                        userInfo.getUserId())));
        if (StrUtil.isEmpty(sessionId)) {
            sessionId = this.loginWo(userInfo, reqParam);
        }

        if (paramMap == null) {
            paramMap = new LinkedMultiValueMap<>();
        }
        paramMap.add("requestType", USP);
        paramMap.add("token", token);

        String handleResult = "";
        try {
            log.info("请求WO应用" + url + "开始：" + DateUtil.now());
            ResponseEntity response = RestTemplateUtil.postForm(url, paramMap, sessionId);
            log.info("请求WO应用" + url + "结束：" + DateUtil.now());
            handleResult = (String) response.getBody();
            if (handleResult.contains(NO_LOGIN)) {
                loginCount++;
                if (loginCount <= loginMaxNum) {
                    this.loginWo(userInfo, reqParam);
                    return this.postToWo(userInfo, reqParam, paramMap, url, loginCount);
                } else {
                    return HANDLE_FAIL;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Read timed out")) {
                throw new AppException("系统服务忙，请重试");
            } else if (e.getMessage().contains("connect timed out")) {
                throw new AppException("连接服务器超时，请重试");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new AppException("服务器拒绝访问，请重试");
            } else if (e.getMessage().contains("I/O error")) {
                throw new AppException("出现例外异常，请重试");
            } else {
                throw new AppException(e.getMessage());
            }
        }

        return handleResult;
    }

    /**
     * 获取老平台用户Id
     *
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-20
     */
    private String findAccount(UserInfo userInfo, ReqParam reqParam) {
        String account = (String) redisRepository.get(RedisConstants.getOldPlatformUserId(reqParam.getCorpId(),
                userInfo.getUserId()));
        if (StrUtil.isEmpty(account)) {
            // 根据人员编号和企业编号获得企业人员信息
            Result result = uasFeignService.findCorpUserByUserIdAndCorpId(userInfo.getUserId(), reqParam.getCorpId());
            if (result != null && Result.SUCCESS == result.getCode().intValue()) {
                CorpUserDto corpUserDto = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), CorpUserDto.class);
                if (corpUserDto != null) {
                    account = corpUserDto.getAccount();
                    redisRepository.set(RedisConstants.getOldPlatformUserId(reqParam.getCorpId(), userInfo.getUserId()),
                            account);
                }
            }
        }
        return account;
    }

    /**
     * 从WO下载文件
     *
     * @param userInfo
     * @param reqParam
     * @param paramMap
     * @param url
     * @param response
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    public void downloadFile(UserInfo userInfo, ReqParam reqParam, MultiValueMap<String, Object> paramMap,
                             String url, HttpServletResponse response) {
        String sessionId =
                StrUtil.trimToEmpty((String) redisRepository.get(RedisConstants.getWoUserSessionId(reqParam.getCorpId(),
                        userInfo.getUserId())));
        if (StrUtil.isEmpty(sessionId)) {
            sessionId = this.loginWo(userInfo, reqParam);
        }

        if (paramMap == null) {
            paramMap = new LinkedMultiValueMap<>();
        }
        paramMap.add("requestType", USP);
        paramMap.add("token", token);

        try {
            log.info("请求WO应用" + url + "下载文件开始：" + DateUtil.now());
            InputStream inputStream = RestTemplateUtil.downloadFile(url, paramMap, sessionId);
            log.info("请求WO应用" + url + "下载文件结束：" + DateUtil.now());

            OutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new AppException("下载文件失败，请重试");
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getMessage().contains("Read timed out")) {
                throw new AppException("系统服务忙，请重试");
            } else if (e.getMessage().contains("connect timed out")) {
                throw new AppException("连接服务器超时，请重试");
            } else if (e.getMessage().contains("Connection refused")) {
                throw new AppException("服务器拒绝访问，请重试");
            } else if (e.getMessage().contains("I/O error")) {
                throw new AppException("出现例外异常，请重试");
            } else {
                throw new AppException(e.getMessage());
            }
        }
    }

    /**
     * 简单封装
     * 请求并返回解释后的状态码
     * @param userInfo
     * @param reqParam
     * @param paramMap
     * @param url
     * @author JFZOU
     * @return
     */
    public int postToWoAndReturnCode(UserInfo userInfo, ReqParam reqParam,MultiValueMap<String, Object> paramMap,String url){
        int returnCode = 0;
        String handResult = this.postToWo(userInfo, reqParam, paramMap, url);
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCodeStr = resultObject.getString("returnCode");
            String message = resultObject.getString("message");
            if (!StatusCodeConstants.SUCCESS_ZERO.equals(returnCodeStr)
                    && !StatusCodeConstants.SUCCESS_ONE.equals(returnCodeStr)) {
                throw new AppException(message);
            } else {
                returnCode = Integer.parseInt(returnCodeStr);
            }
        }
        return returnCode;
    }

    /**
     * 简单封装
     * 请求并返回解释后的状态码
     * @param userInfo
     * @param reqParam
     * @param paramMap
     * @param url
     * @author JFZOU
     * @return
     */
    public String postToWoAndReturnString(UserInfo userInfo, ReqParam reqParam,MultiValueMap<String, Object> paramMap,String url){
        String handResult = this.postToWo(userInfo, reqParam, paramMap, url);
        if (!StringUtils.isEmpty(handResult) && handResult.contains("message")) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String message = StrUtil.trimToEmpty(resultObject.getString("message"));
            if (!message.contains("OK")) {
                throw new AppException(message);
            }
        }
        return "OK";
    }

    public WoResult postToWoAndWoResult(UserInfo userInfo, ReqParam reqParam, MultiValueMap<String, Object> paramMap, String url) {
        WoResult woResult = new WoResult();
        String handResult = this.postToWo(userInfo, reqParam, paramMap, url);
        if (handResult.contains(StatusCodeConstants.RETURN_CODE_STRING)) {
            JSONObject resultObject = JsonUtil.parseObject(handResult, JSONObject.class);
            String returnCodeStr = resultObject.getString("returnCode");
            String message = resultObject.getString("message");
            woResult.setReturnCode(StrUtil.isNotEmpty(returnCodeStr) ? Integer.parseInt(returnCodeStr, 10) : 0);
            woResult.setMessage(message);
        }
        return woResult;
    }
}
