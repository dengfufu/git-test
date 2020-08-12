package com.zjft.usp.anyfix.work.request.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @author cxd
 * @date 2020-05-07 12:02
 */
@ApiModel(value = "工单导出pdf信息")
@Data
public class WorkRequestPdfDto extends WorkDto{

}
