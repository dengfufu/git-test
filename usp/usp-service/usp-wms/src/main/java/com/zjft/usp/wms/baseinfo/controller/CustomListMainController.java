package com.zjft.usp.wms.baseinfo.controller;


import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.CustomListMainDto;
import com.zjft.usp.wms.baseinfo.filter.CustomListMainFilter;
import com.zjft.usp.wms.baseinfo.model.CustomListMain;
import com.zjft.usp.wms.baseinfo.service.CustomListMainService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 自定义列表主表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/custom-list")
public class CustomListMainController {

    @Autowired
    private CustomListMainService customListMainService;

    @ApiOperation(value = "分页查询自定义列表")
    @PostMapping(value = "/pageBy")
    public Result<ListWrapper<CustomListMainDto>> pageBy(@RequestBody CustomListMainFilter customListMainFilter, @CommonReqParam ReqParam reqParam) {
        customListMainFilter.setCorpId(reqParam.getCorpId());
        return Result.succeed(customListMainService.pageBy(customListMainFilter));
    }

    @ApiOperation(value = "查询已启用的自定义列表")
    @PostMapping(value = "/listEnabledBy")
    public Result<List<CustomListMain>> listEnabledBy(@CommonReqParam ReqParam reqParam) {
        return Result.succeed(customListMainService.listEnabledBy(reqParam.getCorpId()));
    }

    @ApiOperation(value = "根据id查询自定义列表")
    @GetMapping("/{customListMainId}")
    public Result<CustomListMainDto> find(@PathVariable Long customListMainId){

        return Result.succeed(customListMainService.findCustomListBy(customListMainId));
    }

    @ApiOperation(value = "添加自定义列表")
    @PostMapping("/insert")
    public Result insert(@RequestBody CustomListMainDto customListMainDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam){
        customListMainDto.setCorpId(reqParam.getCorpId());
        customListMainService.insertCustomList(customListMainDto,userInfo);
        return Result.succeed();
    }

    @ApiOperation(value = "修改自定义列表")
    @PostMapping("/update")
    public Result update(@RequestBody CustomListMainDto customListMainDto, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        customListMainDto.setCorpId(reqParam.getCorpId());
        customListMainDto.setUserId(user.getUserId());
        customListMainService.updateCustomList(customListMainDto);
        return Result.succeed();
    }

    @ApiOperation(value = "删除自定义列表")
    @DeleteMapping("/{customListMainId}")
    public Result delete(@PathVariable Long customListMainId){
        customListMainService.deleteCustomList(customListMainId);
        return Result.succeed();
    }

    @ApiOperation(value = "修改自定义列表基础数据")
    @PostMapping("/updateCustomListMain")
    public Result updateCustomListMain(@RequestBody CustomListMainDto customListMainDto, @LoginUser UserInfo user, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        customListMainDto.setCorpId(reqParam.getCorpId());
        customListMainDto.setUserId(user.getUserId());
        customListMainService.updateCustomListMain(customListMainDto);
        return Result.succeed();
    }
}
