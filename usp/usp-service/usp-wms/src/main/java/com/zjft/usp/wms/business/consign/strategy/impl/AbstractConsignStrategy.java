package com.zjft.usp.wms.business.consign.strategy.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.model.LargeClass;
import com.zjft.usp.wms.baseinfo.service.LargeClassService;
import com.zjft.usp.wms.business.consign.dto.ConsignDetailDto;
import com.zjft.usp.wms.business.consign.dto.ConsignMainDto;
import com.zjft.usp.wms.business.consign.external.ConsignStockExternalService;
import com.zjft.usp.wms.business.consign.model.ConsignDetail;
import com.zjft.usp.wms.business.consign.model.ConsignMain;
import com.zjft.usp.wms.business.consign.service.ConsignDetailService;
import com.zjft.usp.wms.business.consign.service.ConsignMainService;
import com.zjft.usp.wms.business.consign.strategy.ConsignStrategy;
import com.zjft.usp.wms.business.outcome.model.OutcomeWareCommon;
import com.zjft.usp.wms.generator.BusinessCodeGenerator;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 物料发货策略抽象类
 *
 * @author zphu
 * @version 1.0
 * @date 2019-12-14 14:46
 **/
public abstract class AbstractConsignStrategy implements ConsignStrategy {

    @Autowired
    private ConsignMainService consignMainService;
    @Autowired
    private ConsignDetailService consignDetailService;
    @Autowired
    private LargeClassService largeClassService;
    @Resource
    private BusinessCodeGenerator businessCodeGenerator;
    @Resource
    private ConsignStockExternalService consignStockExternalService;

    @Override
    public void add(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        // this.checkDate(consignMainDto);
        if (consignMainDto != null) {
            // 设置主表信息
            ConsignMain consignMain = new ConsignMain();
            BeanUtils.copyProperties(consignMainDto, consignMain);
            consignMain.setId(KeyUtil.getId());
            consignMain.setCorpId(reqParam.getCorpId());

            // 获取前缀
            LargeClass largeClass = this.largeClassService.getOne(new QueryWrapper<LargeClass>().eq("corp_id", reqParam.getCorpId()).eq("large_class_id", consignMainDto.getLargeClassId()));
            String groupCode = this.businessCodeGenerator.getConsignCode(reqParam.getCorpId(), largeClass.getCodePrefix());
            consignMain.setConsignCode(groupCode);
            consignMain.setIsSubmit("Y");
            consignMain.setCreateBy(userInfo.getUserId());
            consignMain.setCreateTime(DateUtil.date());
            consignMain.setConsignDate(DateUtil.format(new Date(), "yyyy-MM-dd"));
            this.consignMainService.save(consignMain);
            //设置dto信息供子策略使用
            consignMainDto.setId(consignMain.getId());
        }
    }

    @Override
    public void receive(ConsignMainDto consignMainDto, UserInfo userInfo, ReqParam reqParam) {
        if (CollectionUtil.isNotEmpty(consignMainDto.getConsignDetailDtoList())) {
            List<Long> idList = consignMainDto.getConsignDetailDtoList().stream().map(e -> e.getId()).collect(Collectors.toList());
            Collection<ConsignDetail> consignDetailList = this.consignDetailService.listByIds(idList);
            if (CollectionUtil.isNotEmpty(consignDetailList)) {
                for (ConsignDetail consignDetail : consignDetailList) {
                    consignDetail.setSigned("Y");
                    consignDetail.setSignBy(userInfo.getUserId());
                    consignDetail.setSignTime(DateUtil.date());
                    this.consignStockExternalService.adjustStockReceive(consignDetail,userInfo);
                }
            }
            this.consignDetailService.updateBatchById(consignDetailList);
        }
    }

    private void checkDate(ConsignMainDto consignMainDto) {
        if (consignMainDto == null) {
            throw new AppException("表单信息不能为空。");
        }

        if (LongUtil.isZero(consignMainDto.getConsignBy())) {
            throw new AppException("发货人不能为空。");
        }

        if (LongUtil.isZero(consignMainDto.getReceiverId())) {
            throw new AppException("收货人不能为空。");
        }

        if (StringUtil.isNullOrEmpty(consignMainDto.getReceiveAddress())) {
            throw new AppException("收货地址不能为空。");
        }

        if (IntUtil.isZero(consignMainDto.getTotalBoxNum())) {
            throw new AppException("总箱数不能为空。");
        }

        StringBuilder detailTips = new StringBuilder(64);
        if (CollectionUtil.isNotEmpty(consignMainDto.getConsignDetailDtoList())) {
            for (int i = 0; i < consignMainDto.getConsignDetailDtoList().size(); i++) {
                StringBuilder rowTip = new StringBuilder(32);
                ConsignDetailDto consignDetailDto =
                        consignMainDto.getConsignDetailDtoList().get(i);
                if (IntUtil.isZero(consignDetailDto.getQuantity())) {
                    rowTip.append("物料数量为空，");
                }
                if (LongUtil.isZero(consignDetailDto.getFromDepotId())) {
                    rowTip.append("物料库房ID为空，");
                }
                if (rowTip.length() > 0) {
                    detailTips.append("第").append(i++).append("行明细：").append(rowTip.substring(0,
                            rowTip.length() - 1)).append("；");
                }
            }
        }
    }
}
