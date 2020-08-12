package com.zjft.usp.uas.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.user.dto.UserInfoDto;
import com.zjft.usp.uas.user.dto.UserRegionDto;
import com.zjft.usp.uas.user.filter.UserFilter;
import com.zjft.usp.uas.user.filter.UserInfoFilter;
import com.zjft.usp.uas.user.model.UserAddr;

import java.util.List;
import java.util.Map;

/**
 * @author zphu
 * @date 2019/8/6 8:58
 **/
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 用户列表
     * @author zgpi
     * @date 2019/9/10 4:41 下午
     * @param userFilter
     * @return
     **/
    ListWrapper<UserInfo> query(UserFilter userFilter);

    /**
     * 根据用户手机号注册
     *
     * @param mobile 手机号
     * @return void
     * @Author zphu
     * @Date 2019/8/13 11:20
     **/
    UserInfo registerUserByMobile(String mobile);



    /**
     * 更新基本用户表
     *
     * @param userInfoDto 用户信息
     * @param userId 用户Id
     * @return void 无返回值
     * @author zphu
     * @date 2019/8/13 11:16
     **/
    Boolean setUserInfo(UserInfoDto userInfoDto, Long userId);

    /**
     * 更新签名
     * @param newSignature
     * @param userId
     */
    void setSignature(String newSignature, Long userId);

    /**
     * 获取用户信息根据用户ID
     * @param userId
     * @return 用户信息类
     */
    UserInfo getUserInfo(Long userId);


    /**
     * 检查验证码是否正确
     *
     * @param mobile
     * @param verifyCode
     * @return true=更新成功
     * @author zphu
     * @date 2019/8/12 19:50
     */
    boolean checkVerifyCode(String mobile,String verifyCode);

    /**
     * 获取地区列表
     * @author dcyu
     * @return 地区列表
     */
    List<CfgArea> getRegions();

    /**
     * 获得用户地区
     * @author zgpi
     * @date 2019-08-22 16:49
     * @param userId
     * @return
     **/
    UserRegionDto getUserRegion(long userId);

    /**
     * 设置个人地区信息
     * @author dcyu
     * @param dto
     * @return
     **/
    void setUserRegion(UserRegionDto dto);

    /**
     * 获取地址信息
     * @author dcyu
     * @param userId
     * @return
     */
    List<UserAddr> getUserInfoAddresses(String userId);



    /**
     *  更新用户昵称
     * @param newNickname
     * @param userId
     * @return boolean true=更新成功 false=失败
     * @author zphu
     * @date 2019/8/13 10:23
     */
    boolean updateNickname(String newNickname, Long userId);

    /**
     * 更新用户性别
     * @date 2020/2/17
     * @param newSex
     * @param userId
     * @return boolean
     */
    boolean updateSex(String newSex, Long userId);

    /**
     * 根据手机号获取用户信息
     *
     * @param mobile
     * @return com.zjft.usp.uas.user.model.UserInfo
     * @author zphu
     * @date 2019/8/20 17:10
     * @throws
    **/
    UserInfo getUserInfoByMobile(String mobile);

    /**
     * 获取用户大头像base64字符串
     *
     * @param faceImgBig
     * @return java.lang.String
     * @author zphu
     * @date 2019/9/12 11:30
     * @throws
    **/
    String getFaceImgBigBase64(Long faceImgBig);

    /**
     * 根据用户编号list获取用户编号和手机号的映射
     * @param userIdList
     * @return
     */
    Map<Long, String> mapUserIdAndMobileByUserIdList(List<Long> userIdList);

    /**
     * 删除用户基本信息
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/28 10:35
     **/
    void delUserInfo(Long userId);

    /**
     * 跟新mobile
     * @param corpUserDto
     * @return
     */
    boolean updateByMobile(CorpUserDto corpUserDto);

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
