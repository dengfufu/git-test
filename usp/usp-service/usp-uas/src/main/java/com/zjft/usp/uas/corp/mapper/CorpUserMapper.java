package com.zjft.usp.uas.corp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.uas.corp.dto.CorpUserDto;
import com.zjft.usp.uas.corp.dto.CorpUserInfoDto;
import com.zjft.usp.uas.corp.filter.CorpUserFilter;
import com.zjft.usp.uas.corp.model.CorpUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author canlei
 * @date 2019-08-04
 */
public interface CorpUserMapper extends BaseMapper<CorpUser> {

    /**
     * 根据用户编号查询企业用户的名字和头像
     *
     * @param userIdList
     * @param corpId
     * @return
     */
    List<CorpUserInfoDto> queryCorpUserInfoByUserIdList(@Param("userIdList") List<Long> userIdList, @Param("corpId") Long corpId);

    /**
     * 查询企业人员列表
     *
     * @param page
     * @param corpUserFilter
     * @return
     * @author zgpi
     * @date 2019/11/13 15:15
     **/
    List<CorpUserDto> queryCorpUser(Page page, @Param("corpUserFilter") CorpUserFilter corpUserFilter);

    /**
     * 根据用户编号和角色查找企业人员
     * @param page
     * @param corpUserFilter
     * @return
     */
    List<CorpUserDto> selectCorpUser(Page page, @Param("corpUserFilter") CorpUserFilter corpUserFilter);

    /**
     * 模糊查询企业人员列表
     *
     * @param corpUserFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 08:54
     **/
    List<CorpUserDto> matchCorpUser(@Param("corpUserFilter") CorpUserFilter corpUserFilter);

    /**
     * 模糊查询企业人员姓名列表
     *
     * @param corpUserFilter
     * @return
     **/
    List<CorpUserDto> matchCorpUserNames(@Param("corpUserFilter") CorpUserFilter corpUserFilter);

    /**
     * 模糊查询企业人员列表（用于筛选）
     * @date 2020/6/9
     * @param corpUserFilter
     * @return java.util.List<com.zjft.usp.uas.corp.dto.CorpUserDto>
     */
    List<CorpUserDto> matchCorpUserByCorp(@Param("corpUserFilter") CorpUserFilter corpUserFilter);

    /**
     * 根据手机查询用户名和手机
     */
    CorpUserInfoDto selectUserNameMobileByMobile(String mobile);
}