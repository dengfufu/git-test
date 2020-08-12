package com.zjft.usp.anyfix.work.chat.filter;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author zrlin
 * @date 2020-03-09 20:50
 */
@Getter
@Setter
public class WorkChatFilter extends Page {

    /**
     * 沟通记录ID
     */
    private Long id;

    /**
     * 工单编号
     */
    private Long workId;

    /**
     * 往前查询的消息顺序
     */
    private Long orderForMore;

    /**
     * 往后查询的的消息顺序
     */
    private Long orderForLatest;

    /**
     * 是否往后查询
     */
    private Integer queryType;

}
