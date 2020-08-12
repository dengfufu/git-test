package com.zjft.usp.common.model;

import lombok.Data;

import java.util.List;

/**
 * @author ljzhu
 * @Package com.zjft.usp.msg.model
 * @date 2020-07-03 17:24
 * @note
 */
@Data
public class WxTemplateMessage {
    String redirectUrl;
    String templateId;
    String toUser;
    String first;
    String remark;
    List<Object> keyWordList;
}
