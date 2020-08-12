package com.zjft.usp.wms.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.wms.flow.dto.FlowClassNodeDto;
import com.zjft.usp.wms.flow.model.FlowClassNode;
import com.zjft.usp.wms.flow.mapper.FlowClassNodeMapper;
import com.zjft.usp.wms.flow.service.FlowClassNodeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 流程类型节点表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-08
 */
@Service
public class FlowClassNodeServiceImpl extends ServiceImpl<FlowClassNodeMapper, FlowClassNode> implements FlowClassNodeService {

    /**
     * corpId=0表示FlowClassNode根模板，每个企业开通WMS系统时，自动把 corpId = 0 的复制一份作为本企业的根模板。
     * 后续扩展时，企业模板会开放接口允许企业配置。
     * 创建新流程模板时，调用本方法获得默认节点列表显示在前端，相应的属性需要在前端赋给FlowTemplateNode，前端再进行微调提交到后端时，数据以List<FlowTemplateNode> flowTemplateNodeList封装。
     *
     * @param corpId
     * @param largeClassId
     * @param smallClassId
     * @return
     * @throws Exception
     */
    @Override
    public List<FlowClassNodeDto> listBy(long corpId, int largeClassId, int smallClassId) {
        FlowClassNodeDto flowClassNodeDto = new FlowClassNodeDto();
        flowClassNodeDto.setCorpId(corpId);
        flowClassNodeDto.setLargeClassId(largeClassId);
        flowClassNodeDto.setSmallClassId(smallClassId);
        return this.listBy(flowClassNodeDto);
    }

    @Override
    public List<FlowClassNodeDto> listBy(FlowClassNodeDto flowClassNodeDto) {
        QueryWrapper<FlowClassNode> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", flowClassNodeDto.getCorpId());
        if (IntUtil.isNotZero(flowClassNodeDto.getLargeClassId())) {
            queryWrapper.eq("large_class_id", flowClassNodeDto.getLargeClassId());
        }
        if (IntUtil.isNotZero(flowClassNodeDto.getSmallClassId())) {
            queryWrapper.eq("small_class_id", flowClassNodeDto.getSmallClassId());
        }
        queryWrapper.orderByAsc("sort_no");

        List<FlowClassNode> flowClassNodeList = this.list(queryWrapper);
        List<FlowClassNodeDto> flowClassNodeDtoList = new ArrayList<>();
        if (flowClassNodeList != null && flowClassNodeList.size() > 0) {
            for (FlowClassNode flowClassNode : flowClassNodeList) {
                FlowClassNodeDto tmpDto = new FlowClassNodeDto();
                BeanUtils.copyProperties(flowClassNode, tmpDto);
                flowClassNodeDtoList.add(tmpDto);
            }
        }
        return flowClassNodeDtoList;
    }
}
