package com.zjft.usp.uas.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.user.model.UserAddr;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserAddrMapper extends BaseMapper<UserAddr> {

    /**
     * 查询用户地址
     * @author zgpi
     * @date 2019-08-20 17:08
     * @param userId
     * @return
     **/
    List<UserAddr> listUserAddr(@Param("userId") Long userId);

    /**
     * 获得单个用户地址
     * @author zgpi
     * @date 2019-08-21 09:43
     * @param addrId
     * @return
     **/
    UserAddr findUserAddr(Long addrId);

    /**
     * 新增用户地址
     * @author zgpi
     * @date 2019-08-20 17:08
     * @param dto
     * @return
     **/
    void addUserAddr(UserAddr dto);

    /**
     * 修改用户地址
     * @author zgpi
     * @date 2019-08-20 17:09
     * @param dto
     * @return
     **/
    void updateUserAddr(UserAddr dto);
    
    /**
     * 删除用户地址
     * @author zgpi
     * @date 2019-08-20 17:08
     * @param addrId
     * @return
     **/
    void deleteUserAddr(Long addrId);


    /**
     * 设置其他地址都不是默认地址
     * @author zgpi
     * @date 2019-08-21 08:42
     * @param addrId
     * @param userId
     * @return
     **/
    void updateIsNotDefault(@Param("addrId") Long addrId, @Param("userId") Long userId);
}
