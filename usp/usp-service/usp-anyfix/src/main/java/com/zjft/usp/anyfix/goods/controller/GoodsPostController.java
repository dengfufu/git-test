package com.zjft.usp.anyfix.goods.controller;


import com.zjft.usp.anyfix.baseinfo.dto.EnumDto;
import com.zjft.usp.anyfix.goods.composite.GoodsPostCompoService;
import com.zjft.usp.anyfix.goods.dto.GoodsPostDto;
import com.zjft.usp.anyfix.goods.dto.GoodsPostSignDto;
import com.zjft.usp.anyfix.goods.enums.ConsignTypeEnum;
import com.zjft.usp.anyfix.goods.enums.ExpressTypeEnum;
import com.zjft.usp.anyfix.goods.enums.TransportTypeEnum;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 物品寄送基本信息表 前端控制器
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@RestController
@RequestMapping("/goods-post")
public class GoodsPostController {

    @Autowired
    private GoodsPostCompoService goodsPostCompoService;

    @ApiOperation(value = "运输方式列表")
    @GetMapping("/transport-type/list")
    public Result<List<EnumDto>> listTransportType() {
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for (TransportTypeEnum transportTypeEnum : TransportTypeEnum.values()) {
            enumDto = new EnumDto();
            enumDto.setCode(transportTypeEnum.getCode());
            enumDto.setName(transportTypeEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }

    @ApiOperation(value = "托运方式列表")
    @GetMapping("/consign-type/list")
    public Result<List<EnumDto>> listConsignType() {
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for (ConsignTypeEnum consignTypeEnum : ConsignTypeEnum.values()) {
            enumDto = new EnumDto();
            enumDto.setCode(consignTypeEnum.getCode());
            enumDto.setName(consignTypeEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }

    @ApiOperation(value = "快递类型列表")
    @GetMapping("/express-type/list")
    public Result<List<EnumDto>> listExpressType() {
        List<EnumDto> list = new ArrayList<>();
        EnumDto enumDto;
        for (ExpressTypeEnum expressTypeEnum : ExpressTypeEnum.values()) {
            enumDto = new EnumDto();
            enumDto.setCode(expressTypeEnum.getCode());
            enumDto.setName(expressTypeEnum.getName());
            list.add(enumDto);
        }
        return Result.succeed(list);
    }

    @ApiOperation(value = "获得地址信息")
    @PostMapping("/address")
    public Result<GoodsPostDto> findAddress(@RequestBody GoodsPostFilter goodsPostFilter) {
        return Result.succeed(goodsPostCompoService.findAddress(goodsPostFilter));
    }

    @ApiOperation("分页查询物品寄送单")
    @PostMapping(value = "/query")
    public Result<ListWrapper<GoodsPostDto>> queryGoodsPost(@RequestBody GoodsPostFilter goodsPostFilter,
                                                            @LoginUser UserInfo userInfo,
                                                            @CommonReqParam ReqParam reqParam) {
        return Result.succeed(goodsPostCompoService.queryGoodsPost(goodsPostFilter, userInfo.getUserId(),
                reqParam.getCorpId()));
    }

    @ApiOperation("查看物品寄送单详情")
    @GetMapping(value = "/detail/{postId}")
    public Result<GoodsPostDto> viewGoodsPost(@PathVariable("postId") Long postId) {
        return Result.succeed(goodsPostCompoService.viewGoodsPost(postId));
    }

    @ApiOperation("添加物品寄送单")
    @PostMapping(value = "/add")
    public Result addGoodsPost(@RequestBody GoodsPostDto goodsPostDto,
                               @LoginUser UserInfo userInfo) {
        goodsPostCompoService.addGoodsPost(goodsPostDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("编辑物品寄送单")
    @PostMapping(value = "/edit")
    public Result editGoodsPost(@RequestBody GoodsPostDto goodsPostDto,
                                @LoginUser UserInfo userInfo) {
        goodsPostCompoService.editGoodsPost(goodsPostDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("删除物品寄送单")
    @DeleteMapping(value = "/{postId}")
    public Result delGoodsPost(@PathVariable("postId") Long postId) {
        goodsPostCompoService.delGoodsPost(postId);
        return Result.succeed();
    }

    @ApiOperation("签收物品寄送单")
    @PostMapping(value = "/sign")
    public Result signGoodsPost(@RequestBody GoodsPostSignDto goodsPostSignDto,
                                @LoginUser UserInfo userInfo) {
        goodsPostCompoService.signGoodsPost(goodsPostSignDto, userInfo.getUserId());
        return Result.succeed();
    }

    @ApiOperation("是否属于企业人员")
    @GetMapping(value = "/ifCorpUser/{postId}")
    public Result<GoodsPostDto> findIfCorpUser(@PathVariable("postId") Long postId,
                                               @LoginUser UserInfo userInfo) {
        return Result.succeed(goodsPostCompoService.findIfCorpUser(postId, userInfo.getUserId()));
    }
}
