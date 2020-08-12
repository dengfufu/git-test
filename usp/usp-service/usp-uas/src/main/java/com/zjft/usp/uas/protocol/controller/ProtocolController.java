package com.zjft.usp.uas.protocol.controller;


import cn.hutool.core.util.StrUtil;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.protocol.dto.ProtocolCheckDto;
import com.zjft.usp.uas.protocol.dto.ProtocolSignDto;
import com.zjft.usp.uas.protocol.model.ProtocolDef;
import com.zjft.usp.uas.protocol.service.ProtocolDefService;
import com.zjft.usp.uas.protocol.service.ProtocolSignService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户签订协议  前端控制器
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
@Api(tags = "协议")
@RestController
@RequestMapping("/protocol")
public class ProtocolController {

    @Autowired
    ProtocolDefService protocolDefService;

    @Autowired
    ProtocolSignService protocolSignService;

    @ApiOperation(value = "获取所有协议定义")
    @GetMapping(value = "/list")
    public Result<List<ProtocolDef>> list(@RequestParam(required = false) String module) {
        if(StrUtil.isNullOrUndefined(module)) {
            module = "";
        }
        List<ProtocolDef> list = protocolDefService.listByModule(module);
        return Result.succeed(list);
    }

    @ApiOperation(value = "企业签订协议")
    @PostMapping(value = "/sign-tob")
    public Result signToB(@RequestBody ProtocolSignDto param, @LoginUser UserInfo user) {
        Long operator = user.getUserId();
        this.protocolSignService.signToB(param.getCorpId(),operator,param.getProtocolIds());
        return Result.succeed();
    }

    @ApiOperation(value = "个人签订协议")
    @PostMapping(value = "/sign-toc")
    public Result signToC(@RequestBody ProtocolSignDto param, @LoginUser UserInfo user) {
        this.protocolSignService.signToC(param.getUserId(),param.getProtocolIds());
        return Result.succeed();
    }

    @ApiOperation(value = "远程调用: 签订协议")
    @PostMapping(value = "/feign/sign-tob")
    public Result signFeignToB(@RequestBody Map<String, Object> param) {
        Long corpId = null;
        Long operator = null;
        if(param.get("corpId") != null) {
            corpId = Long.parseLong((String) param.get("corpId"));
        }
        if(param.get("operator") != null) {
            operator = Long.parseLong((String) param.get("operator"));
        }

        List<Integer> protocolIds = (List<Integer>) param.get("protocolIds");
        this.protocolSignService.signToB(corpId,operator, protocolIds);
        return Result.succeed();
    }

    @ApiOperation(value = "用户协议签订日期检查")
    @PostMapping(value = "/check-sign")
    public Result<List<ProtocolCheckDto>> checkSign(@RequestBody Map<String, Object> param) {
        Long userId = null;
        Long corpId = null;
        if(param.get("corpId") != null) {
            corpId = Long.parseLong((String) param.get("corpId"));
        }
        if(param.get("userId") != null) {
            userId = Long.parseLong((String) param.get("userId"));
        }
        String module = (String) param.get("module");
        if(StrUtil.isNullOrUndefined(module)){
            module = "";
        }
        List<ProtocolCheckDto> list = protocolSignService.checkSign(userId, corpId, module);
        return Result.succeed(list);
    }
}
