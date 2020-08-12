package com.zjft.usp.zj.work.cases.atmcase.dto.atmcase;

import com.zjft.usp.zj.common.model.ScSysUser;
import com.zjft.usp.zj.common.model.WoLoginUser;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.PartReplaceDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * ATM机CASE信息
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:57
 **/


@ApiModel(value = "ATM CASE明细信息")
@Data
public class CaseDetailDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private CaseDto caseDto;

    private WoLoginUser woLoginUser;

    private ScSysUser scSysUser;

    private List<CaseTraceDto> caseTraceDtoList;

    private List<PartReplaceDto> partReplaceDtoList;

    private List<PartReplaceDto> downPartList;

    private List<PartReplaceDto> upPartList;

    @ApiModelProperty(value = "CASE签到标志")
    private boolean signFlag;

    @ApiModelProperty(value = "修改CASE标志")
    private boolean modCaseFlag;

    @ApiModelProperty(value = "关闭CASE标志")
    private boolean finishFlag;

    @ApiModelProperty(value = "CASE延期标志")
    private boolean delayFlag;

    @ApiModelProperty(value = "添加备件更换标志")
    private boolean partReplaceFlag;

    @ApiModelProperty(value = "添加工行介质标志")
    private boolean icbcMediaFlag;

    @ApiModelProperty(value = "取消CASE标志")
    private boolean cancelFlag;

    @ApiModelProperty(value = "设置监控标志")
    private boolean setMonitorFlag;

    @ApiModelProperty(value = "取消监控标志")
    private boolean cancelMonitorFlag;
}
