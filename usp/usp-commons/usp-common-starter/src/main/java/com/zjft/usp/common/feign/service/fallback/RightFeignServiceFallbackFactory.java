package com.zjft.usp.common.feign.service.fallback;

import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.Result;
import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RightFeignServiceFallbackFactory implements FallbackFactory<RightFeignService> {
    @Override
    public RightFeignService create(Throwable throwable) {

        return new RightFeignService() {

            /**
             * 人员与角色列表映射
             *
             * @param corpId
             * @return
             * @author zgpi
             * @date 2019/12/16 09:08
             **/
            @Override
            public Result<Map<Long, Object>> mapUserIdAndRoleList(Long corpId) {
                return null;
            }

            /**
             * 人员与角色映射
             *
             * @param jsonFilter
             * @return
             * @author zgpi
             * @date 2019/12/16 09:08
             **/
            @Override
            public Result<Map<Long, String>> mapUserIdAndRoleNames(String jsonFilter) {
                return null;
            }


            /**
             * 添加系统角色
             *
             * @param corpId
             * @param userId
             * @return
             * @author zgpi
             * @date 2019/12/24 11:09
             **/
            @Override
            public Result addSysRole(Long corpId, Long userId) {
                return null;
            }

            /**
             * 获得具有某权限的人员列表
             *
             * @param corpId
             * @param rightId
             * @return
             **/
            @Override
            public Result<List<Long>> listUserByRightId(Long corpId, Long rightId) {
                return null;
            }

            /**
             * 获得具有某权限的人员列表，排除系统管理员用户
             *
             * @param corpId
             * @param rightId
             * @return
             **/
            @Override
            public Result<List<Long>> listUserByRightIdNoSysUser(Long corpId, Long rightId) {
                return null;
            }

            /**
             * 初始化用户角色的redis
             *
             * @param userId
             * @return
             * @author zgpi
             * @date 2020/5/28 21:00
             **/
            @Override
            public Result initUserRoleRedis(Long userId) {
                return null;
            }

        };
    }
}
