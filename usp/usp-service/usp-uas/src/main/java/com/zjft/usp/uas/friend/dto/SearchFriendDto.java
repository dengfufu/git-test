package com.zjft.usp.uas.friend.dto;


import lombok.Data;
import org.apache.kafka.common.protocol.types.Field;

/**
 * @Author : zrLin
 * @Date : 2019年8月19日
 * @Desc : 查询好友返回类
 */
@Data
public class SearchFriendDto {
    private String nickname;
    private String userId;
    private String faceImg;
    private String faceImgBig;
    private String province;
    private String city;
    private String signature;
    private String sex;
}
