package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.dto.UserAddrDto;
import com.zjft.usp.uas.user.model.UserAddr;

import java.util.List;

/**
 * 用户地址服务接口类
 * @author zgpi
 * @version 1.0
 * @date 2019-08-20 10:38
 **/
public interface UserAddrService extends IService<UserAddr> {

    /**
     * 查询用户地址
     * @author zgpi
     * @date 2019-08-20 16:49
     * @param userId
     * @return
     **/
    List<UserAddrDto> listUserAddr(long userId);

    /**
     * 获得单个用户地址
     * @author zgpi
     * @date 2019-08-21 09:44
     * @param addrId
     * @return
     **/
    UserAddrDto findUserAddr(long addrId);

    /**
     * 增加地址信息
     * @author zgpi
     * @date 2019-08-20 16:48
     * @param dto
     * @return
     **/
    void addUserAddr(UserAddrDto dto);

    /**
     * 更新地址信息
     * @author zgpi
     * @date 2019-08-20 16:48
     * @param dto
     * @return
     **/
    void updateUserAddr(UserAddrDto dto);

    /**
     * 删除地址信息
     * @author zgpi
     * @date 2019-08-20 16:48
     * @param addrId
     * @return
     **/
    void deleteUserAddr(Long addrId);
}
