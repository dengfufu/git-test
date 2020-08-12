package com.zjft.usp.anyfix.corp.manage.dto;

import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderAutoConfig;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 服务委托方与服务商关系
 *
 * @author canlei
 * @version 1.0
 * @date 2019-10-24 08:39
 **/
@ApiModel(value = "服务委托方与服务商关系")
@Getter
@Setter
public class DemanderServiceDto extends DemanderService {

    @ApiModelProperty(value = "服务委托方企业名称")
    private String demanderCorpName;

    @ApiModelProperty(value = "服务委托方企业名称")
    private String demanderShortCorpName;

    @ApiModelProperty(value = "服务商企业名称")
    private String serviceCorpName;

    @ApiModelProperty(value = "地区")
    private String region;

    @ApiModelProperty(value = "联系电话")
    private String telephone;

    @ApiModelProperty(value = "详细地址")
    private String address;

    @ApiModelProperty(value = "注册人姓名")
    private String regUserName;

    @ApiModelProperty(value = "注册人编号")
    private Long regUserId;

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

    @ApiModelProperty(value = "网址")
    private String website;

    @ApiModelProperty(value = "委托商自动化配置更新")
    private DemanderAutoConfigDto demanderAutoConfigDto;

    @ApiModelProperty(value = "协议文件列表")
    private List<DemanderContDto> demanderContDtoList ;

    @ApiModelProperty("人员规模")
    private Integer staffQty;

    @ApiModelProperty("经营业务")
    private String business;

    @ApiModelProperty("所属行业编号")
    private String industry;

    @ApiModelProperty("所属行业编号")
    private String logoImg;

    @ApiModelProperty("地区")
    private String district;

    @ApiModelProperty("地区")
    private String city;


    @ApiModelProperty(value = "委托商级别")
    private Integer demanderLevel;
}
