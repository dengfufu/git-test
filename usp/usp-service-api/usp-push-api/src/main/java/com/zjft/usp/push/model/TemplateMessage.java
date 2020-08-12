package com.zjft.usp.push.model;


import lombok.Data;

import java.util.Map;

@Data
public class TemplateMessage {

    String tplName;
    Map<String,String> messageMap;
    String userId;
}
