package com.zjft.usp.uas.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.filter.UserInfoFilter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    /**
     * 模糊查询有效用户
     *
     * @param userInfoFilter
     * @return
     * @author zgpi
     * @date 2020/6/29 18:37
     **/
    List<UserInfoDto> matchUser(UserInfoFilter userInfoFilter);

}
