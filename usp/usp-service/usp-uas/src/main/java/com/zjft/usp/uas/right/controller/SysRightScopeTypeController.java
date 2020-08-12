package com.zjft.usp.uas.right.controller;

import com.zjft.usp.common.enums.RightScopeTypeEnum;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.right.dto.EnumDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 权限范围类型表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-3-11
 */
@Api(tags = "系统权限范围类型")
@RestController
@RequestMapping("/sys-right-scope-type")
public class SysRightScopeTypeController {


    @ApiOperation(value = "获得权限范围类型列表")
    @GetMapping(value = "/type/list")
    public Result<List<EnumDto>> listRightScopeType() {
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for(RightScopeTypeEnum rightScopeTypeEnum : RightScopeTypeEnum.values()){
            enumDto = new EnumDto();
            enumDto.setCode(rightScopeTypeEnum.getCode());
            enumDto.setName(rightScopeTypeEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }
}
