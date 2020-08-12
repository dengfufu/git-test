package com.zjft.usp.anyfix.goods.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.anyfix.goods.composite.GoodsPostCompoService;
import com.zjft.usp.anyfix.goods.composite.GoodsPostExcelCompoService;
import com.zjft.usp.anyfix.goods.dto.GoodsPostDto;
import com.zjft.usp.anyfix.goods.dto.GoodsPostExportDto;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 物品寄送Excel服务实现类
 *
 * @author zgpi
 * @date 2020/4/27 16:35
 */
@Service
public class GoodsPostExcelCompoServiceImpl implements GoodsPostExcelCompoService {

    @Autowired
    private GoodsPostCompoService goodsPostCompoService;

    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final DateFormat TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    /**
     * 获取物品寄送单导出数据
     *
     * @param goodsPostFilter
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/4/27 16:34
     **/
    @Override
    public List<GoodsPostExportDto> listGoodsPostExportDto(GoodsPostFilter goodsPostFilter,
                                                           Long curUserId,
                                                           Long curCorpId) {
        List<GoodsPostDto> list = goodsPostCompoService.exportGoodsPost(goodsPostFilter, curUserId, curCorpId);
        List<GoodsPostExportDto> exportDtoList = new ArrayList<>();
        GoodsPostExportDto exportDto;
        if (CollectionUtil.isNotEmpty(list)) {
            for (GoodsPostDto goodsPostDto : list) {
                exportDto = new GoodsPostExportDto();
                BeanUtils.copyProperties(goodsPostDto, exportDto);
                if (goodsPostDto.getSignTime() != null) {
                    exportDto.setSignTimeStr(TIME_FORMAT.format(goodsPostDto.getSignTime()));
                }
                if (goodsPostDto.getCreateTime() != null) {
                    exportDto.setCreateTimeStr(TIME_FORMAT.format(goodsPostDto.getCreateTime()));
                }
                if (goodsPostDto.getConsignTime() != null) {
                    exportDto.setConsignTimeStr(DATE_FORMAT.format(goodsPostDto.getConsignTime()));
                }
                exportDtoList.add(exportDto);
            }
        }
        return exportDtoList;
    }
}
