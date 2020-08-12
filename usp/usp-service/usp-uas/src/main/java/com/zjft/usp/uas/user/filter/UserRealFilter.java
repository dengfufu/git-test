package com.zjft.usp.uas.user.filter;

import lombok.Data;

import java.util.List;

@Data
public class UserRealFilter {

    /**
     * 人员编号列表
     **/
    private List<Long> userIdList;
}
