package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.user.dto.UserRealDto;
import com.zjft.usp.uas.user.model.UserReal;

import java.util.List;
import java.util.Map;

/**
 * @author zphu
 * @date 2019/8/13 16:24
 * @Version 1.0
 **/
public interface UserRealService extends IService<UserReal> {

    /**
     * 保存用户实名认证信息
     *
     * @param userRealDto 实名认证实体类
     * @param userId 用户id
     * @return boolean true=保存成功
     * @author zphu
     * @date 2019/8/13 16:34
    **/
    boolean save(UserRealDto userRealDto,Long userId);

    /**
     * 根据id查询name的map
     *
     * @param userIdList
     * @return java.util.Map<java.lang.Long,java.lang.String>
     * @author zphu
     * @date 2019/9/27 14:35
     * @throws
    **/
    Map<Long,String> mapIdAndName(List<Long> userIdList);

    /**
     * 人员认证信息
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/13 18:14
     **/
    UserRealDto findUserRealDtoById(Long userId);

    /**
     * 删除人员认证信息
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:37
     **/
    void delUserReal(Long userId);


    /**
     * 身份证实名认证
     *
     * @param userId
     * @return
     * @throws
     * @author zphu
     * @date 2019/8/29 8:48
     **/
    UserRealDto findUserReal(Long userId);
}
