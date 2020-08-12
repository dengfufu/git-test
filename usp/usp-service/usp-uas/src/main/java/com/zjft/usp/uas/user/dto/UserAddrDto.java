package com.zjft.usp.uas.user.dto;

import com.zjft.usp.uas.user.model.UserAddr;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @Author : dcyu
 * @Date : 2019年8月13日
 * @Desc : 地址信息传输类
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class UserAddrDto extends UserAddr {
    /** 地址ID **/
    private String addrIdStr;
    private String provinceName;
    private String cityName;
    private String districtName;
}
