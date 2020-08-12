package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpAdminDto;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.mapper.CorpAdminMapper;
import com.zjft.usp.uas.corp.model.CorpAdmin;
import com.zjft.usp.uas.corp.model.CorpUser;
import com.zjft.usp.uas.corp.service.CorpAdminService;
import com.zjft.usp.uas.corp.service.CorpUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 企业管理员实现类
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-13 10:16
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpAdminServiceImpl extends ServiceImpl<CorpAdminMapper, CorpAdmin> implements CorpAdminService {

//    @Autowired
//    private CorpUserService corpUserService;
//
//    @Override
//    public void setCorpManager(CorpAdminDto corpAdminDto, ReqParam reqParam, Long curUserId) {
//        if (corpAdminDto.getCorpId() == null || corpAdminDto.getUserId() == null) {
//            throw new AppException("参数解析失败！");
//        }
//        CorpAdmin corpAdmin = new CorpAdmin();
//        BeanUtils.copyProperties(corpAdminDto, corpAdmin);
//        corpAdmin.setGrantTime(new DateTime());
//        this.save(corpAdmin);
//    }
//
//    @Override
//    public List<CorpUserDto> listCorpAdmin(Long corpId) {
//        if (corpId == null || corpId == 0) {
//            throw new AppException("企业编号不能为空！");
//        }
//        QueryWrapper<CorpAdmin> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("corpid", corpId);
//        List<CorpAdmin> corpAdminList = this.list(queryWrapper);
//        Map<Long, CorpUser> corpUserMap = this.corpUserService.mapUserIdAndCorpUser(corpId);
//        List<CorpUserDto> corpUserDtoList = new ArrayList<>();
//        if (corpAdminList != null && corpAdminList.size() > 0) {
//            for (CorpAdmin admin : corpAdminList) {
//                CorpUserDto corpUserDto = new CorpUserDto();
//                BeanUtils.copyProperties(admin, corpUserDto);
//                corpUserDtoList.add(corpUserDto);
//            }
//        }
//        return corpUserDtoList;
//    }
//
//    @Override
//    public int addCorpAdmin(long userId, long corpId) {
//        CorpAdmin corpAdmin = new CorpAdmin();
//        corpAdmin.setUserId(userId);
//        corpAdmin.setCorpId(corpId);
//        corpAdmin.setGrantTime(Timestamp.valueOf(LocalDateTime.now()));
//        save(corpAdmin);
//        return 0;
//    }
//
//    @Override
//    public Map<Long, CorpAdmin> mapAdminByCorpId(Long corpId) {
//        QueryWrapper<CorpAdmin> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("corpid", corpId);
//        List<CorpAdmin> corpAdminList = this.list(queryWrapper);
//        Map<Long, CorpAdmin> map = new HashMap<>();
//        if (corpAdminList != null && corpAdminList.size() > 0) {
//            for (CorpAdmin corpAdmin : corpAdminList) {
//                map.put(corpAdmin.getUserId(), corpAdmin);
//            }
//        }
//        return map;
//    }
//
//    @Override
//    public Map<Long, CorpAdmin> mapCorpByAdmin(long userId) {
//        List<CorpAdmin> corpAdminList = lambdaQuery().eq(CorpAdmin::getUserId, userId).list();
//        Map<Long, CorpAdmin> map = new HashMap<>();
//        if (corpAdminList != null && corpAdminList.size() > 0) {
//            for (CorpAdmin corpAdmin : corpAdminList) {
//                map.put(corpAdmin.getCorpId(), corpAdmin);
//            }
//        }
//        return map;
//    }
//
//    /**
//     * 是否企业管理员
//     *
//     * @param userId
//     * @param corpId
//     * @return
//     * @author zgpi
//     * @date 2019/11/28 10:52
//     **/
//    @Override
//    public boolean isCorpAdmin(Long userId, Long corpId) {
//        CorpAdmin corpAdmin = this.getOne(new QueryWrapper<CorpAdmin>().eq("userid", userId)
//                .eq("corpid", corpId));
//        if (corpAdmin != null) {
//            return true;
//        }
//        return false;
//    }

}
