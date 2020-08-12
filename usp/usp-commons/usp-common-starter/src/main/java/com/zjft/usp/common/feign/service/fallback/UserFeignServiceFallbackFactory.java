package com.zjft.usp.common.feign.service.fallback;

import com.zjft.usp.common.feign.service.UserFeignService;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * userService降级工场
 *
 * @author CK
 * @date 2019-08-20 08:45
 */
@Slf4j
@Component
public class UserFeignServiceFallbackFactory implements FallbackFactory<UserFeignService> {
    @Override
    public UserFeignService create(Throwable throwable) {
        return new UserFeignService() {

            /**
             * 通过手机号登录
             *
             * @param mobile
             * @return
             * @author zgpi
             * @date 2019/11/28 11:00
             **/
            @Override
            public LoginAppUser logonByMobile(String mobile) {
                return null;
            }

            /**
             * 通过logonId查询用户信息
             *
             * @param logonId
             * @return
             */
            @Override
            public LoginAppUser logonByLogonId(String logonId) {
                return null;
            }

            @Override
            public LoginAppUser logonByWxId(String openId) {
                log.error("通过openId查询用户异常:{}");
                return null;
            }

            @Override
            public UserInfo selectByUserId(String userId) {
                log.error("通过username查询用户异常:{}");
                return null;
            }

            /**
             * 根据idList获得企业ID与名称映射
             *
             * @param corpIdList
             * @return
             */
            @Override
            public Result<Map<Long, String>> mapCorpIdAndNameByCorpIdList(List<Long> corpIdList) {
                return null;
            }


            @Override
            public Result<Map<Long, String>> mapCorpIdAndNameByJsonCorpIds(List<Long> corpIdList) {
                return null;
            }

            @Override
            public String getOpenidByUserid(Long userId) {
                return null;
            }

            /**
             * 保存登录成功日志
             *
             * @param userLog
             * @return
             * @author zgpi
             * @date 2020/5/25 14:43
             **/
            @Override
            public Result saveLogonSuccessLog(String userLog) {
                return null;
            }

            /**
             * 保存登录成功日志
             *
             * @param userLog
             * @return
             * @author zgpi
             * @date 2020/5/25 14:43
             **/
            @Override
            public Result saveAutoLogonSuccessLog(String userLog) {
                return null;
            }

            /**
             * 保存登录密码错误日志
             *
             * @param userLog
             * @return
             * @author zgpi
             * @date 2020/5/25 14:43
             **/
            @Override
            public Result saveLogonPwdFailLog(String userLog) {
                return null;
            }

            /**
             * 保存登出日志
             *
             * @param userLog
             * @return
             * @author zgpi
             * @date 2020/5/25 19:59
             **/
            @Override
            public Result saveLogoutLog(String userLog) {
                return null;
            }


        };
    }
}
