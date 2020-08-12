package com.zjft.usp.uas.corp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpAdminDto;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.model.CorpAdmin;

import java.util.List;
import java.util.Map;

/**
 * 企业管理员
 *
 * @author canlei
 * @version 1.0
 * @date 2019-08-13 10:15
 **/
public interface CorpAdminService extends IService<CorpAdmin> {

//    /**
//     * 设置管理员
//     * @param corpAdminDto
//     * @param reqParam
//     * @param curUserId
//     */
//    void setCorpManager(CorpAdminDto corpAdminDto, ReqParam reqParam, Long curUserId);
//
//    /**
//     * 查询企业管理员
//     * @param corpId
//     * @return
//     */
//    List<CorpUserDto> listCorpAdmin(Long corpId);
//
//    /**
//     * 添加管理员
//     * @param userId
//     * @param corpId
//     * @return
//     */
//    int addCorpAdmin(long userId, long corpId);
//
//    /**
//     * 获取管理员map
//     * @param corpId
//     * @return
//     */
//    Map<Long, CorpAdmin> mapAdminByCorpId(Long corpId);
//
//    /**
//     * 获取用户为管理员的企业map
//     * @param userId
//     * @return
//     */
//    Map<Long, CorpAdmin> mapCorpByAdmin(long userId);
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
//    boolean isCorpAdmin(Long userId, Long corpId);
}
