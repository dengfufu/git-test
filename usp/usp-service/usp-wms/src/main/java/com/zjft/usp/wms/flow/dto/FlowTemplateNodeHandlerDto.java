package com.zjft.usp.wms.flow.dto;

import com.zjft.usp.wms.flow.model.FlowTemplateNodeHandler;
import lombok.Data;

import java.util.List;

/**
 * @Author: JFZOU
 * @Date: 2019-11-19 15:50
 */
@Data
public class FlowTemplateNodeHandlerDto extends FlowTemplateNodeHandler {

    private List<FlowTemplateNodeHandler> handlerList;
}
