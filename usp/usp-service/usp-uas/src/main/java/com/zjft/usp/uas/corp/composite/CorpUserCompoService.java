package com.zjft.usp.uas.corp.composite;

import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.dto.CorpUserDto;

/**
 * 企业人员聚合接口类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/18 10:41
 */
public interface CorpUserCompoService {

    /**
     * 获得企业人员详情
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/4 10:03
     **/
    CorpUserDto findCorpUserDetail(Long userId, Long corpId);

    /**
     * 添加企业人员
     *
     * @param corpUserDto
     * @return
     * @author zgpi
     * @date 2019/12/18 10:43
     **/
    void addCorpUser(CorpUserDto corpUserDto, Long currentUserId, String clientId, ReqParam reqParam);

    /**
     * 修改企业人员
     *
     * @param corpUserDto
     * @return
     * @author zgpi
     * @date 2019/12/18 10:44
     **/
    void updateCorpUser(CorpUserDto corpUserDto);

    /**
     * 删除企业人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/18 10:44
     **/
    void delCorpUser(Long userId, Long corpId, Long currentUserId, String clientId);
}
