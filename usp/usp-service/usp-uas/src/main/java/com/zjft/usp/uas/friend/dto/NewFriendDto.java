package com.zjft.usp.uas.friend.dto;


import lombok.Data;

@Data
public class NewFriendDto {

    private String userId;  //好友id
    private String friendName; //如果有备注名，显示备注名
    private String faceImg;
    private String applyNote;
    private short  status;


}
