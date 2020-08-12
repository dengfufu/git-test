package com.zjft.usp.anyfix.work.attention.controller;

import com.zjft.usp.anyfix.work.attention.dto.WorkAttentionDto;
import com.zjft.usp.anyfix.work.attention.service.WorkAttentionService;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 工单关注表 前端控制器
 * </p>
 *
 * @author cxd
 * @since 2020-04-15
 */
@RestController
@RequestMapping("/work-attention")
public class WorkAttentionController {
    @Autowired
    private WorkAttentionService workAttentionService;

    @ApiOperation(value = "添加工单关注")
    @PostMapping(value = "/add")
    public Result addWorkAttention(@RequestBody WorkAttentionDto workAttentionDto,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        if (LongUtil.isZero(workAttentionDto.getCorpId())) {
            workAttentionDto.setCorpId(reqParam.getCorpId());
        }
        workAttentionService.addWorkAttention(workAttentionDto, userInfo, reqParam);
        return Result.succeed();
    }

    @ApiOperation("删除工单关注")
    @PostMapping(value = "/delWorkAttention")
    public Result delWorkAttention(@RequestBody WorkAttentionDto workAttentionDto,
                                   @LoginUser UserInfo userInfo,
                                   @CommonReqParam ReqParam reqParam) {
        workAttentionService.deleteByWorkId(workAttentionDto.getWorkId(),userInfo.getUserId(),reqParam.getCorpId());
        return Result.succeed();
    }
}
