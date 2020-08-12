package com.zjft.usp.wms.baseinfo.controller;
import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.CustomListDetailDto;
import com.zjft.usp.wms.baseinfo.service.CustomListDetailService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 自定义列表子表 前端控制器
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@RestController
@RequestMapping("/custom-list-detail")
public class CustomListDetailController {

    @Autowired
    private CustomListDetailService customListDetailService;

    @ApiOperation(value = "修改列表")
    @PostMapping("/update")
    public Result update(@RequestBody CustomListDetailDto customListDetailDto, @LoginUser UserInfo userInfo, @CommonReqParam ReqParam reqParam){
        if(LongUtil.isZero(reqParam.getCorpId())){
            return Result.failed("参数解析失败！");
        }
        customListDetailDto.setCreateBy(userInfo.getUserId());
        customListDetailDto.setCreateTime(DateUtil.date());
        customListDetailDto.setUpdateBy(userInfo.getUserId());
        customListDetailDto.setUpdateTime(DateUtil.date().toTimestamp());
        customListDetailService.updateCustomListDetail(customListDetailDto);
        return Result.succeed();
    }
}
