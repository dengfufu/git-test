package com.zjft.usp.anyfix.goods.controller;

import com.zjft.usp.anyfix.goods.composite.GoodsPostExcelCompoService;
import com.zjft.usp.anyfix.goods.dto.GoodsPostExportDto;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import com.zjft.usp.anyfix.utils.ExcelUtil;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 物品寄送EXCEL 前端控制器
 *
 * @author zgpi
 * @date 2020/4/27 16:30
 */
@RestController
@RequestMapping("/goods-post-excel")
public class GoodsPostExcelController {

    @Autowired
    private GoodsPostExcelCompoService goodsPostExcelCompoService;

    @PostMapping(value = "/export")
    public void downloadExcel(HttpServletResponse response,
                              @RequestBody GoodsPostFilter goodsPostFilter,
                              @LoginUser UserInfo userInfo,
                              @CommonReqParam ReqParam reqParam) {
        String fileName = "物品寄送单";
        String sheetName = "物品寄送单";
        List<GoodsPostExportDto> goodsPostExportDtoList = goodsPostExcelCompoService
                .listGoodsPostExportDto(goodsPostFilter, userInfo.getUserId(), reqParam.getCorpId());
        try {
            ExcelUtil.writeExcel(response, goodsPostExportDtoList, fileName, sheetName,
                    GoodsPostExportDto.class, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
