package com.zjft.usp.anyfix.baseinfo.controller;

import com.zjft.usp.anyfix.baseinfo.enums.WorkSysTypeEnum;
import com.zjft.usp.anyfix.baseinfo.dto.EnumDto;
import com.zjft.usp.anyfix.work.request.enums.WorkSourceEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.enums.WarrantyEnum;
import com.zjft.usp.common.model.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 工单基础数据接口 控制层
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/14 4:48 下午
 **/
@Api(tags = "工单基础数据接口")
@RestController
public class WorkSysController {

    @ApiOperation(value = "工单系统类型列表")
    @GetMapping("/work-sys-type/list")
    public Result listWorkSysType(){
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for(WorkSysTypeEnum workSysTypeEnum : WorkSysTypeEnum.values()){
            enumDto = new EnumDto();
            enumDto.setCode(workSysTypeEnum.getCode());
            enumDto.setName(workSysTypeEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }

    @ApiOperation(value = "工单状态列表")
    @GetMapping("/work-status/list")
    public Result<List<EnumDto>> listWorkStatus(){
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for(WorkStatusEnum workStatusEnum : WorkStatusEnum.values()){
            enumDto = new EnumDto();
            enumDto.setCode(workStatusEnum.getCode());
            enumDto.setName(workStatusEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }

    @ApiOperation(value = "工单来源列表")
    @GetMapping("/work-source/list")
    public Result listWorkSource(){
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for(WorkSourceEnum WorkSourceEnum : WorkSourceEnum.values()){
            enumDto = new EnumDto();
            enumDto.setCode(WorkSourceEnum.getCode());
            enumDto.setName(WorkSourceEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }

    @ApiOperation(value = "保修状态列表")
    @GetMapping("/warranty/list")
    public Result listWarranty(){
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for(WarrantyEnum warrantyEnum : WarrantyEnum.values()){
            enumDto = new EnumDto();
            enumDto.setCode(warrantyEnum.getCode());
            enumDto.setName(warrantyEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }

}
