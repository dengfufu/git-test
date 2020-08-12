package com.zjft.usp.uas.redis.controller;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.redis.service.RedisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Redis控制器
 *
 * @author zgpi
 * @date 2020/6/8 16:51
 */
@Api(tags = "Redis")
@RestController
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @ApiOperation(value = "获得Redis的值")
    @GetMapping(value = "/get")
    public Result<Object> findRedisValue(@RequestParam("key") String key) {
        return Result.succeed(redisService.findRedisValue(key));
    }

    @ApiOperation(value = "删除Redis的值")
    @DeleteMapping(value = "/delete")
    public Result deleteRedisValue(@RequestParam("key") String key) {
        redisService.deleteRedisValue(key);
        return Result.succeed();
    }
}
