package com.zjft.usp.anyfix.corp.fileconfig.controller;


import com.zjft.usp.anyfix.corp.fileconfig.filter.WorkFilesFilter;
import com.zjft.usp.anyfix.corp.fileconfig.service.WorkFilesService;
import com.zjft.usp.common.model.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 分组文件表 前端控制器
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/work-files")
public class WorkFilesController {

    @Resource
    private WorkFilesService workFilesService;

    @GetMapping("/list/{configId}/{workId}")
    public Result workFilesDtoList(@PathVariable("configId") Long  configId,
                                   @PathVariable("workId") Long workId) {
        return Result.succeed(workFilesService.getWorkFileByConfigIdAndWorkId(configId, workId));
    }

    @PostMapping("/update")
    public Result workFilesDtoList(@RequestBody WorkFilesFilter workFilesFilter) {
        workFilesService.updateFiles(workFilesFilter);
        workFilesService.checkStatus(workFilesFilter);
        return Result.succeed();
    }
}
