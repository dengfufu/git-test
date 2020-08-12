package com.zjft.usp.uas.corp.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CorpAppDto {

    @ApiModelProperty("应用菜单URI")
    private List<CorpMenu> menus;

    @ApiModelProperty("应用Cookie")
    private String cookie;

    @ApiModelProperty("应用域名")
    private String domain;
}
