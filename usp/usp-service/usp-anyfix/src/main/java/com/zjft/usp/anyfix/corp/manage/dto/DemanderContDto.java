package com.zjft.usp.anyfix.corp.manage.dto;

import com.baomidou.mybatisplus.annotation.TableName;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCont;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 
 * </p>
 *
 * @author zrlin
 * @since 2020-06-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("demander_cont")
@ApiModel(value="DemanderCont对象", description="")
public class DemanderContDto extends DemanderCont implements Serializable  {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "删除的文件编号")
    private List<Long> deleteFileIdList;

    @ApiModelProperty(value = "新增文件编号")
    private List<Long> newFileIdList;


    @ApiModelProperty(value = "收费文件描述")
    private List<Long> feeRuleFileList;

    @ApiModelProperty(value = "收费文件描述")
    private List<FileInfoDto> feeRuleFileInfoList;

    @ApiModelProperty(value = "收费文件描述")
    private List<Long> serviceStandardFileList;

    @ApiModelProperty(value = "委托商企业编号")
    private Long demanderCorp;

    @ApiModelProperty(value = "服务商企业编号")
    private Long serviceCorp;


}
