package com.zjft.usp.anyfix.corp.fileconfig.controller;


import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.model.FileConfig;
import com.zjft.usp.anyfix.corp.fileconfig.service.FileConfigService;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 文件配置定义表 前端控制器
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/file-config")
public class FileConfigController {

    @Resource
    private FileConfigService fileConfigService;
    @PostMapping("add")
    public Result addFileConfig(@RequestBody FileConfig fileConfig, @LoginUser UserInfo userInfo) {
        this.fileConfigService.addFileConfig(fileConfig, userInfo);
        return Result.succeed();
    }

    @PostMapping("query")
    public Result getFileConfigList(@RequestBody FileConfigFilter filter,
                                    @LoginUser UserInfo userInfo) {
        return Result.succeed(this.fileConfigService.getFileConfigList(filter));
    }

    @PostMapping("demander/list")
    public Result getFileConfigListByDemander(@RequestBody FileConfigFilter filter,
                                    @LoginUser UserInfo userInfo) {
        return Result.succeed(this.fileConfigService.getFileConfigListByDemander(filter));
    }

    @PostMapping("update")
    public Result updateFileConfig(@RequestBody FileConfig fileConfig,
                                              @LoginUser UserInfo userInfo) {
        this.fileConfigService.updateFileConfig(fileConfig, userInfo);
        return Result.succeed();
    }

    @DeleteMapping("{id}")
    public Result deleteFileConfig(@PathVariable("id") Long id) {
        this.fileConfigService.deleteFileConfig(id);
        return Result.succeed();
    }


    @GetMapping("{id}")
    public Result getFileConfig(@PathVariable("id") Long id) {
        return Result.succeed(this.fileConfigService.getFileConfigById(id));
    }
}
