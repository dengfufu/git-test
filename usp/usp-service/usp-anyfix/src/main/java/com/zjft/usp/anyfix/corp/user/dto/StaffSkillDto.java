package com.zjft.usp.anyfix.corp.user.dto;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.anyfix.corp.user.model.StaffSkill;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zphu
 * @date 2019/9/27 16:00
 **/
@ApiModel("人员技能")
@Getter
@Setter
public class StaffSkillDto extends StaffSkill {

    @ApiModelProperty("服务网点编号")
    private Long branchId;

    @ApiModelProperty("工程师姓名")
    private String userName;

    @ApiModelProperty("工单类型编号list")
    private List<Integer> workTypeList;

    @ApiModelProperty("设备小类编号list")
    private List<Long> smallClassIdList;

    @ApiModelProperty("设备品牌编号list")
    private List<Long> brandIdList;

    @ApiModelProperty("设备型号list")
    private List<Long> modelIdList;

    @ApiModelProperty("工单类型名称，多个用，隔开")
    private String workTypeNames;

    @ApiModelProperty("设备小类名称，多个用，隔开")
    private String smallClassNames;

    @ApiModelProperty("设备大类名称")
    private String largeClassName;

    @ApiModelProperty("设备品牌名称，多个用，隔开")
    private String brandNames;

    @ApiModelProperty("设备型号名称，多个用，隔开")
    private String modelNames;

    public void setStringByList() {
        this.setWorkTypes("");
        if (CollectionUtil.isNotEmpty(this.workTypeList)) {
            StringBuilder workTypes = new StringBuilder(32);
            workTypes.append(",");
            for (Integer workType : this.workTypeList) {
                workTypes.append(workType).append(",");
            }
            this.setWorkTypes(workTypes.toString());
        }
        this.setSmallClassIds("");
        if (CollectionUtil.isNotEmpty(this.smallClassIdList)) {
            StringBuilder smallClassIds = new StringBuilder(32);
            smallClassIds.append(",");
            for (Long smallClassId : this.smallClassIdList) {
                smallClassIds.append(smallClassId).append(",");
            }
            this.setSmallClassIds(smallClassIds.toString());
        }
        this.setBrandIds("");
        if (CollectionUtil.isNotEmpty(this.brandIdList)) {
            StringBuilder brandIds = new StringBuilder(32);
            brandIds.append(",");
            for (Long brandId : this.brandIdList) {
                brandIds.append(brandId).append(",");
            }
            this.setBrandIds(brandIds.toString());
        }
        this.setModelIds("");
        if (CollectionUtil.isNotEmpty(this.modelIdList)) {
            StringBuilder modelIds = new StringBuilder(32);
            modelIds.append(",");
            for (Long modelId : this.modelIdList) {
                modelIds.append(modelId).append(",");
            }
            this.setModelIds(modelIds.toString());
        }
    }

    public void setListByString() {
        if (StrUtil.isNotBlank(this.getWorkTypes())) {
            List<String> list = Arrays.stream(this.getWorkTypes().split(","))
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList());
            List<Integer> workTypeList = list
                    .stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            this.setWorkTypeList(workTypeList);
        }
        if (StrUtil.isNotBlank(this.getSmallClassIds())) {
            List<String> list = Arrays.stream(this.getSmallClassIds().split(","))
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList());
            List<Long> smallClassIdList = list
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            this.setSmallClassIdList(smallClassIdList);
        }
        if (StrUtil.isNotBlank(this.getBrandIds())) {
            List<String> list = Arrays.stream(this.getBrandIds().split(","))
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList());
            List<Long> brandIdList = list
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            this.setBrandIdList(brandIdList);
        }
        if (StrUtil.isNotBlank(this.getModelIds())) {
            List<String> list = Arrays.stream(this.getModelIds().split(","))
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList());
            List<Long> modelIdList = list
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            this.setModelIdList(modelIdList);
        }
    }

}
