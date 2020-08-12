package com.zjft.usp.uas.user.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.AuthFeignService;
import com.zjft.usp.common.model.LoginAppUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.utils.QrCodeUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.uas.baseinfo.dto.AreaDto;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.model.CorpUser;
import com.zjft.usp.uas.corp.service.CorpUserService;
import com.zjft.usp.uas.user.dto.*;
import com.zjft.usp.uas.user.enums.UserOperEnum;
import com.zjft.usp.uas.user.model.*;
import com.zjft.usp.uas.user.service.*;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static com.zjft.usp.uas.user.enums.UserOperEnum.*;

/**
 * @author zphu
 * @date 2019/8/16 9:19
 * @Version 1.0
 **/
@Transactional(rollbackFor = Exception.class)
@Service
public class UserAccountServinceImpl implements UserAccountService {

    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private UserSafeService userSafeService;
    @Autowired
    private UserRealService userRealService;

    @Autowired
    private UserWxService userWxService;
    @Autowired
    private UserLogonIdService userLogonIdService;
    @Autowired
    private UserMobileService userMobileService;
    @Autowired
    private UserMobileInvalidService userMobileInvalidService;
    @Autowired
    private UserOperService userOperService;
    @Autowired
    private CfgAreaService cfgAreaService;
    @Autowired
    private FileFeignService fileFeignService;
    @Autowired
    private AuthFeignService authFeignService;
    @Autowired
    private CorpUserService corpUserService;
    @Autowired
    private AnyfixFeignService anyfixFeignService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public LoginAppUser logonByMobile(String mobile) {
        Assert.notNull(mobile, "mobile 不能为空");
        UserMobile userMobile = userMobileService.getOne(new QueryWrapper<UserMobile>().eq("mobile", mobile), true);
        if (userMobile == null) {
            return null;
        }
        UserSafe userSafe = userSafeService.getOne(new QueryWrapper<UserSafe>().eq("userId", userMobile.getUserId()), true);
        LoginAppUser loginAppUser = new LoginAppUser();
        loginAppUser.setMobile(userMobile.getMobile());
        loginAppUser.setUserId(userMobile.getUserId());
        if (userSafe != null) {
            // OAuth2 UserDetails 的登录方式mobile作为确定用户唯一
            loginAppUser.setUsername(userMobile.getMobile());
            loginAppUser.setPassword(userSafe.getPasswd());
        }
        return loginAppUser;
    }

    @Override
    public LoginAppUser logonByLogonId(String logonId) {
        Assert.notNull(logonId, "loginId 不能为空");

        UserLogonId userLogonId = userLogonIdService.getOne(new QueryWrapper<UserLogonId>().eq("logonId", logonId), true);
        if (userLogonId == null) {
            return null;
        }
        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("userId", userLogonId.getUserId()), true);
        UserSafe userSafe = userSafeService.getOne(new QueryWrapper<UserSafe>().eq("userId", userLogonId.getUserId()), true);
        if (userInfo == null || userSafe == null) {
            return null;
        }
        LoginAppUser loginAppUser = new LoginAppUser();
        loginAppUser.setUserId(userLogonId.getUserId());
        // OAuth2 UserDetails 的登录方式
        loginAppUser.setUsername(userInfo.getMobile());
        loginAppUser.setMobile(userInfo.getMobile());
        loginAppUser.setPassword(userSafe.getPasswd());
        return loginAppUser;
    }

    @Override
    public LoginAppUser logonByWx(String openId) {
        Assert.notNull(openId, "openId 不能为空");
        // TODO, openId或者unionId
        UserWx userWx = userWxService.getOne(new QueryWrapper<UserWx>().eq("openId", openId), true);
        if (userWx == null) {
            return null;
        }
        UserInfo userInfo = userInfoService.getOne(new QueryWrapper<UserInfo>().eq("userId", userWx.getUserId()), true);
        UserSafe userSafe = userSafeService.getOne(new QueryWrapper<UserSafe>().eq("userId", userWx.getUserId()), true);
        if (userInfo == null || userSafe == null) {
            return null;
        }
        LoginAppUser loginAppUser = new LoginAppUser();
        loginAppUser.setUserId(userWx.getUserId());
        // OAuth2 UserDetails 的登录名称
        loginAppUser.setUsername(userInfo.getMobile());
        loginAppUser.setMobile(userInfo.getMobile());
        loginAppUser.setPassword(userSafe.getPasswd());
        return loginAppUser;
    }


    /**
     * 根据手机号获得用户编号
     *
     * @param mobile
     * @return
     * @author zgpi
     * @date 2019/11/28 11:06
     **/
    @Override
    public Long findUserIdByMobile(String mobile) {
        UserMobile userMobile = userMobileService.getOne(new QueryWrapper<UserMobile>().eq("mobile", mobile), true);
        if (userMobile != null) {
            return userMobile.getUserId();
        }
        return null;
    }

    @Override
    public UserInfoDto getUserInfoDtoById(Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        UserInfo userInfo = userInfoService.getById(userId);

        UserInfoDto userInfoDto = new UserInfoDto();
        userInfoDto.setUserId(String.valueOf(userId));
        if (userInfo != null) {
            BeanUtils.copyProperties(userInfo, userInfoDto);
            String regionCode = StrUtil.trimToEmpty(userInfo.getDistrict());
            regionCode = StrUtil.isBlank(regionCode) ? StrUtil.trimToEmpty(userInfo.getCity()) : regionCode;
            regionCode = StrUtil.isBlank(regionCode) ? StrUtil.trimToEmpty(userInfo.getProvince()) : regionCode;
            if (StrUtil.isNotBlank(regionCode)) {
                userInfoDto.setRegionCode(regionCode);
                AreaDto areaDto = cfgAreaService.findAreaByCode(regionCode);
                if (areaDto != null) {
                    userInfoDto.setProvinceName(StrUtil.trimToEmpty(areaDto.getProvinceName()));
                    userInfoDto.setCityName(StrUtil.trimToEmpty(areaDto.getCityName()));
                    userInfoDto.setDistrictName(StrUtil.trimToEmpty(areaDto.getDistrictName()));
                    userInfoDto.setRegionName(StrUtil.trimToEmpty(areaDto.getAreaName()));
                }
            }

            userInfoDto.setFaceImg(String.valueOf(userInfo.getFaceImg()));
            userInfoDto.setFaceImgBig(String.valueOf(userInfo.getFaceImgBig()));
        }

        UserSafe userSafe = userSafeService.getOne(new QueryWrapper<UserSafe>().eq("userId", userId), true);
        if (userSafe != null) {
            userInfoDto.setLogonId(userSafe.getLogonId());
            userInfoDto.setEmail(userSafe.getEmail());
            userInfoDto.setExistPassword(StrUtil.isNotBlank(userSafe.getPasswd()));
        }
        UserReal userReal = userRealService.getById(userId);
        if (userReal != null) {
            userInfoDto.setUserName(userReal.getUserName());
            userInfoDto.setIsUserReal("Y");
        }
        Result<String> result = anyfixFeignService.getBranchesNameByUserId(userId);
        if (result != null && result.getCode() == Result.SUCCESS) {
            userInfoDto.setServiceBranchNames(result.getData());
        }

        List<CorpUser> corpUserList = corpUserService.lambdaQuery().eq(CorpUser::getUserId, userId).list();
        if (corpUserList != null && corpUserList.size() > 0) {
            List<Long> corpIdList = new ArrayList<>();
            for (CorpUser corpUser : corpUserList) {
                corpIdList.add(corpUser.getCorpId());
            }
            userInfoDto.setCorpIdList(corpIdList);;
        }

        return userInfoDto;
    }

    @Override
    public UserInfoDto queryFaceImgBig(UserInfo user) {
        UserInfo userInfo = userInfoService.getById(user.getUserId());
        UserInfoDto userInfoDto = null;
        if (LongUtil.isNotZero(userInfo.getFaceImgBig())) {
            String img = userInfoService.getFaceImgBigBase64(userInfo.getFaceImgBig());
            userInfoDto = new UserInfoDto();
            userInfoDto.setFaceImgBig(img);
        }
        return userInfoDto;
    }

    @Override
    public void updateNickname(UserInfoDto userInfoDto, Long userId, ReqParam reqParam, String clientId) {
        Assert.notNull(userInfoDto, "userInfoDto 不能为空");
        Assert.notNull(reqParam, "reqParam 不能为空");
        Assert.notEmpty(clientId, "clientId 不能为空");

        UserInfo userInfo = userInfoService.getById(userId);
        if (userInfo == null) {
            throw new AppException("系统异常，请重试");
        }
        userInfoService.updateNickname(userInfoDto.getNickname(), userId);
        userOperService.insert(userInfoDto.getNickname(), userInfo.getNickname(), UO_UPDATE_NICKNAME,
                userId, reqParam, clientId);
    }

    @Override
    public void updateSex(UserInfoDto userInfoDto, Long userId, ReqParam reqParam, String clientId) {
        Assert.notNull(userInfoDto, "userInfoDto 不能为空");
        Assert.notNull(reqParam, "reqParam 不能为空");
        Assert.notEmpty(clientId, "clientId 不能为空");

        UserInfo userInfo = userInfoService.getById(userId);
        if (userInfo == null) {
            throw new AppException("系统异常，请重试");
        }
        userInfoService.updateSex(userInfoDto.getSex(), userId);
        userOperService.insert(userInfoDto.getSex(), userInfo.getSex(), UO_UPDATE_SEX,
                userId, reqParam, clientId);
    }

    @Override
    public void updateLoginId(UserInfoDto userInfoDto, Long userId, ReqParam reqParam, String clientId) {
        Assert.notNull(userInfoDto, "userInfoDto 不能为空");
        Assert.notNull(userId, "userId 不能为空");
        Assert.notNull(reqParam, "reqParam 不能为空");
        Assert.notEmpty(clientId, "clientId 不能为空");

        String oldLogonId = userSafeService.getById(userId).getLogonId();
        userSafeService.updateLoginId(userInfoDto.getLogonId(), userId);
        if (StrUtil.isNotBlank(userInfoDto.getLogonId())) {
            userLogonIdService.update(new UpdateWrapper<UserLogonId>().set("logonid", userInfoDto.getLogonId()).eq("logonid", oldLogonId));
        } else {
            userLogonIdService.removeById(oldLogonId);
        }
        userOperService.insert(userInfoDto.getLogonId(), oldLogonId, UO_UPDATE_LOGINID,
                userId, reqParam, clientId);
    }

    @Override
    public void updateUserInfo(UserInfoDto userInfoDto) {
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(Long.valueOf(userInfoDto.getUserId()));
        userInfo.setNickname(userInfoDto.getNickname());
        userInfo.setSex(userInfoDto.getSex());
        userInfo.setSignature(userInfoDto.getSignature());
        userInfoService.updateById(userInfo);
    }

    @Override
    public void updateMobile(VerifyMessageDto verifyMessageDto, UserInfo userInfo, ReqParam reqParam, String clientId) {
        //验证验证码
        String result = authFeignService.verifySmsCode(verifyMessageDto.getMobile(), verifyMessageDto.getSmsCode());
        if (result == null) {
            throw new AppException("系统异常，请重试");
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if (!"0".equals(code)) {
            throw new AppException(jsonObject.getString("msg"));
        }

        UserInfo userInfoLogin = userInfoService.getById(userInfo.getUserId());

        if (verifyMessageDto.getCorpId() != null) {
            //查询出员工企业所有员工信息
            CorpUserDto corpUserDto = new CorpUserDto();
            corpUserDto.setCorpId(verifyMessageDto.getCorpId());
            List<CorpUserDto> corpUserList = corpUserService.getCorpUserByCorpId(corpUserDto, reqParam);
            if (corpUserList != null && corpUserList.size() > 0) {
                for (CorpUserDto c : corpUserList) {
                    //判断该手机号是否是企业中其他员工的手机号
                    if (c.getMobile().equals(verifyMessageDto.getMobile())) {
                        throw new AppException("该手机号是企业中其他员工的手机号，请重新输入");
                    }
                }
            }
        }
        //查询出原来的手机信息表信息
        UserMobile userMobileOld = userMobileService.getById(verifyMessageDto.getMobile());
        if (userMobileOld != null) {
            //解绑该手机号绑定的原账号
            if (userMobileOld.getUserId().equals(userInfo.getUserId())) {
                throw new AppException("手机号仍为原手机号，不可重复绑定");
            }
            //手机号先失效，而后才能修改uas_user_mobile表
            userMobileInvalidService.insert(userMobileOld.getMobile());
            userMobileService.removeById(userMobileOld.getMobile());
            userInfoService.update(new UpdateWrapper<UserInfo>().set("mobile", "").eq("userid", userMobileOld.getUserId()));
            userOperService.insert("", userMobileOld.getMobile(), UO_UPDATE_MOBILE,
                    userMobileOld.getUserId(), reqParam, clientId);
        }
        // 解绑原来的手机号
        userMobileInvalidService.insert(userInfoLogin.getMobile());
        //绑定新的手机号
        userMobileService.update(new UpdateWrapper<UserMobile>().set("mobile", verifyMessageDto.getMobile()).eq("mobile", userInfoLogin.getMobile()));
        userInfoService.update(new UpdateWrapper<UserInfo>().set("mobile", verifyMessageDto.getMobile()).eq("userid", userInfo.getUserId()));
        userOperService.insert(verifyMessageDto.getMobile(), userInfoLogin.getMobile(), UO_UPDATE_MOBILE,
                userInfo.getUserId(), reqParam, clientId);
    }

    @Override
    public void idCardCertify(UserRealDto userRealDto, UserInfo userInfo, ReqParam reqParam, String clientId) {
//        String realNameResult = RealNameUtil.getIdCardResult(userRealDto.getUserName(), userRealDto.getIdCard());
//        JsonObject jsonObject = JsonUtil.parseObject(realNameResult, JsonObject.class);
//        if ("认证通过".equals(jsonObject.get("reason"))) {
//            userRealService.save(userRealDto, userInfo.getUserId());
//            String changeInfo = "姓名：" + userRealDto.getUserName() + "; 身份证号：" + userRealDto.getIdCard();
//            userOperService.insert(changeInfo, "", UO_IDCARD_VERIFY,
//                    userInfo.getUserId(), reqParam, Integer.parseInt(appId));
//        }
        userRealService.save(userRealDto, userInfo.getUserId());
        String changeInfo = "姓名：" + userRealDto.getUserName() + "; 身份证号：" + userRealDto.getIdCard();
        userOperService.insert(changeInfo, "", UO_IDCARD_VERIFY,
                userInfo.getUserId(), reqParam, clientId);
    }

    @Override
    public Boolean setUserInfo(UserInfoDto userInfoDto, Long userId) {
        return userInfoService.setUserInfo(userInfoDto, userId);
    }

    @Override
    public void setSignature(String newSignature, UserInfo userInfo, ReqParam reqParam, String clientId) {
        userInfoService.setSignature(newSignature, userInfo.getUserId());
        userOperService.insert(newSignature, userInfo.getSignature(), UO_SET_SIGNATURE, userInfo.getUserId(), reqParam, clientId);
    }

    @Override
    public void setEmailAddress(String emailAddress, UserInfo userInfo, ReqParam reqParam, String clientId) {
        UserSafe userSafe = userSafeService.getUserSafeInfo(userInfo.getUserId());
        userSafeService.setEmailAddress(emailAddress, userInfo.getUserId());
        userOperService.insert(emailAddress, userSafe.getEmail(), UO_SET_EMAIL, userInfo.getUserId(), reqParam, clientId);
    }

    @Override
    public String getQRCodeInfo(Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        UserInfo userInfo = userInfoService.getUserInfo(userId);
        String qrCodeBase64 = "";
        if (userInfo != null) {
            Long faceImg = userInfo.getFaceImg();
            if (faceImg == null || faceImg == 0) {
                return qrCodeBase64;
            }
            JSONObject dataJson = new JSONObject();
            dataJson.put("userId", userInfo.getUserId());
            dataJson.put("faceImg", faceImg);

            ResponseEntity<byte[]> bytes = fileFeignService.getImgByteArrayByFileId(faceImg);
            byte[] imgIbytes = bytes.getBody();
            if (imgIbytes != null && imgIbytes.length > 0) {
                InputStream inputStream = new ByteArrayInputStream(imgIbytes);
                byte[] imgBytes = QrCodeUtil.createQrCodeImgByte(dataJson.toJSONString(), inputStream, 300, 300);
                Base64.Encoder base64 = Base64.getEncoder();
                qrCodeBase64 = base64.encodeToString(imgBytes);
            }
        }
        return qrCodeBase64;
    }

    @Override
    public List<CfgArea> getRegions() {
        return userInfoService.getRegions();
    }

    @Override
    public UserRegionDto setUserRegion(UserRegionDto dto, UserInfo userInfo, ReqParam reqParam, String clientId) {
        Assert.notNull(dto, "regionDto 不能为NULL");
        String newRegion = "国家：" + userInfo.getCountry() + "；省份：" + userInfo.getProvince() + "；城市：" + userInfo.getCity();
        String oldRegion = "国家：" + dto.getState() + "；省份：" + dto.getProvinceCode() + "；城市：" + dto.getCityCode();
        String province = "";
        String city = "";
        String district = "";
        if (dto.getSelectType() == 1) {
            province = cfgAreaService.parseProvince(dto.getProvinceName());
            city = cfgAreaService.parseCity(province, dto.getCityName());
            district = cfgAreaService.parseDistrict(city, dto.getDistrictName());
        } else {
            province = dto.getProvinceCode().substring(0, 2);
            city = dto.getCityCode().substring(0, 4);
            district = dto.getDistrictCode();
        }
        dto.setProvinceCode(province);
        dto.setCityCode(city);
        dto.setDistrictCode(district);
        dto.setUserId(String.valueOf(userInfo.getUserId()));
        String areaCode = "";
        areaCode = StrUtil.isNotBlank(province) ? province : areaCode;
        areaCode = StrUtil.isNotBlank(city) ? city : areaCode;
        areaCode = StrUtil.isNotBlank(district) ? district : areaCode;
        dto.setAreaCode(areaCode);
        Map<String, String> areaMap = cfgAreaService.mapAreaCodeAndName(province);
        StringBuilder regionName = new StringBuilder(16);
        if (StrUtil.isNotBlank(province)) {
            regionName.append(StrUtil.trimToEmpty(areaMap.get(province)));
        }
        if (StrUtil.isNotBlank(city)) {
            regionName.append(StrUtil.trimToEmpty(areaMap.get(city)));
        }
        if (StrUtil.isNotBlank(district)) {
            regionName.append(StrUtil.trimToEmpty(areaMap.get(district)));
        }
        dto.setAreaNames(regionName.toString());
        userInfoService.setUserRegion(dto);
        userOperService.insert(newRegion, oldRegion, UserOperEnum.UO_SET_REGION, userInfo.getUserId(), reqParam, clientId);
        return dto;
    }

    @Override
    public List<UserAddr> getUserInfoAddresses(String userId) {
        Assert.notEmpty(userId, "userId 不能为空");
        return userInfoService.getUserInfoAddresses(userId);
    }

    @Override
    public void uploadFaceImage(UserInfoDto userInfoDto, Long userId) {
        Assert.notNull(userId, "userId 不能为空");
        if (StrUtil.isNotBlank(userInfoDto.getFaceImg()) && StrUtil.isNotBlank(userInfoDto.getFaceImgBig())) {
            UserInfo userInfoOld = this.userInfoService.getById(userId);
            Long faceImg = Long.parseLong(userInfoDto.getFaceImg());
            Long faceImgBig = Long.parseLong(userInfoDto.getFaceImgBig());
            this.userInfoService.update(new UpdateWrapper<UserInfo>()
                    .set("faceimg", faceImg)
                    .set("faceimgbig", faceImgBig).eq("userid", userId));
            // 删除旧文件
            if (LongUtil.isNotZero(userInfoOld.getFaceImg()) &&
                    !userInfoOld.getFaceImg().equals(faceImg)) {
                fileFeignService.delFile(userInfoOld.getFaceImg());
            }
            if (LongUtil.isNotZero(userInfoOld.getFaceImgBig()) &&
                    !userInfoOld.getFaceImgBig().equals(faceImgBig) &&
                    !userInfoOld.getFaceImgBig().equals(userInfoOld.getFaceImg())) {
                fileFeignService.delFile(userInfoOld.getFaceImgBig());
            }
            // 删除临时文件表数据
            if (LongUtil.isNotZero(faceImg)) {
                this.fileFeignService.deleteFileTemporaryByID(faceImg);
            }
            if (LongUtil.isNotZero(faceImgBig)) {
                this.fileFeignService.deleteFileTemporaryByID(faceImgBig);
            }
        }
    }

    @Override
    public void changePassword(String oldPassword, String newPassword, ReqParam reqParam, Long curUserId, String clientId) {
        if (StringUtil.isNullOrEmpty(oldPassword) || StringUtil.isNullOrEmpty(newPassword)) {
            throw new AppException("参数解析错误！");
        }
        UserSafe userSafeOld = userSafeService.getById(curUserId);
        if (userSafeOld == null) {
            throw new AppException("出现异常！");
        }
        if (this.passwordEncoder.matches(oldPassword, userSafeOld.getPasswd())) {
            String safeNewPassword = this.passwordEncoder.encode(newPassword);
            userSafeOld.setPasswd(safeNewPassword);
            userSafeService.updateById(userSafeOld);
            userOperService.insert("", "", UO_UPDATE_PASSWORD,
                    curUserId, reqParam, clientId);
        } else {
            throw new AppException("旧密码输入错误！");
        }
    }

    @Override
    public void changePasswordBySms(String mobile, String smsCode, String newPassword, ReqParam reqParam, String clientId) {
        if (StringUtil.isNullOrEmpty(mobile) || StringUtil.isNullOrEmpty(smsCode) || StringUtil.isNullOrEmpty(newPassword)) {
            throw new AppException("参数解析错误！");
        }
        // 验证验证码
        String result = authFeignService.verifySmsCode(mobile, smsCode);
        JSONObject jsonObject = JSONObject.parseObject(result);
        String code = jsonObject.getString("code");
        if (!"0".equals(code)) {
            throw new AppException(jsonObject.getString("msg"));
        }
        // 查询用户并修改密码
        UserInfo userInfo = userInfoService.getUserInfoByMobile(mobile);
        if (userInfo == null) {
            throw new AppException("该账号不存在！");
        }
        UserSafe userSafeOld = userSafeService.getById(userInfo.getUserId());
        String safeNewPassword = this.passwordEncoder.encode(newPassword);
        if (userSafeOld == null) {
            UserSafe userSafe = new UserSafe();
            userSafe.setUserId(userInfo.getUserId());
            userSafe.setPasswd(safeNewPassword);
            userSafeService.save(userSafe);
            userOperService.insert("", "", UO_SET_PASSWORD,
                    userInfo.getUserId(), reqParam, clientId);
        } else {
            userSafeOld.setPasswd(safeNewPassword);
            userSafeService.updateById(userSafeOld);
            userOperService.insert("", "", UO_FORGET_UPDATE_PASSWORD,
                    userInfo.getUserId(), reqParam, clientId);
        }
        // 消除账号锁定信息
        authFeignService.lockAccountReset(mobile);
    }
}
