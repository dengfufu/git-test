package com.zjft.usp.msg.dto;

import com.zjft.usp.common.model.Page;
import lombok.Getter;
import lombok.Setter;

/**
 * @author CK
 * @date 2019-11-14 13:57
 */
@Setter
@Getter
public class MsgFilter extends Page {

    private String yearMonth = "1911";

    private long touser;
}
