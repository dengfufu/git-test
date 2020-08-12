package com.zjft.usp.common.feign.service;

import com.zjft.usp.common.feign.service.fallback.RightFeignServiceFallbackFactory;
import com.zjft.usp.common.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "usp-uas", fallbackFactory = RightFeignServiceFallbackFactory.class)
public interface RightFeignService {

    /**
     * 人员与角色列表映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/16 09:08
     **/
    @RequestMapping(value = "/sys-role-user/feign/map/{corpId}", method = RequestMethod.GET)
    Result<Map<Long, Object>> mapUserIdAndRoleList(@PathVariable("corpId") Long corpId);

    /**
     * 人员与角色映射
     *
     * @param jsonFilter
     * @return
     * @author zgpi
     * @date 2019/12/16 09:08
     **/
    @RequestMapping(value = "/sys-role-user/feign/map/users", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapUserIdAndRoleNames(@RequestBody String jsonFilter);

    /**
     * 添加系统角色
     *
     * @param corpId
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/12/24 11:09
     **/
    @RequestMapping(value = "/sys-role/feign/sys/add", method = RequestMethod.POST)
    Result addSysRole(@RequestParam("corpId") Long corpId,
                      @RequestParam("userId") Long userId);

    /**
     * 获得具有某权限的人员列表
     *
     * @param corpId
     * @param rightId
     * @return
     **/
    @RequestMapping(value = "/user-right/feign/listUserByRightId", method = RequestMethod.GET)
    Result<List<Long>> listUserByRightId(@RequestParam("corpId") Long corpId,
                                         @RequestParam("rightId") Long rightId);

    /**
     * 获得具有某权限的人员列表，排除系统管理员用户
     *
     * @param corpId
     * @param rightId
     * @return
     **/
    @RequestMapping(value = "/user-right/feign/listUserByRightIdNoSysUser", method = RequestMethod.GET)
    Result<List<Long>> listUserByRightIdNoSysUser(@RequestParam("corpId") Long corpId,
                                                  @RequestParam("rightId") Long rightId);

    /**
     * 初始化用户所有角色的redis
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/28 21:00
     **/
    @RequestMapping(value = "/sys-role-user/feign/redis-init/all", method = RequestMethod.POST)
    Result initUserRoleRedis(@RequestParam("userId") Long userId);

}
