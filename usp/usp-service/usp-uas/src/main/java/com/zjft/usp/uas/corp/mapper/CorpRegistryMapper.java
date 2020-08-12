package com.zjft.usp.uas.corp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.uas.corp.dto.CorpDto;
import com.zjft.usp.uas.corp.dto.CorpNameDto;
import com.zjft.usp.uas.corp.dto.CorpRoleDto;
import com.zjft.usp.uas.corp.filter.CorpRegistryFilter;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author canlei
 * @date 2019-08-04
 */
public interface CorpRegistryMapper extends BaseMapper<CorpRegistry> {

    /**
     * 分页查询企业
     *
     * @param page
     * @param corpRegistryFilter
     * @return
     * @author zgpi
     * @date 2019/12/18 16:50
     **/
    List<CorpRegistry> query(Page page,
                             @Param("corpRegistryFilter") CorpRegistryFilter corpRegistryFilter);

    /**
     * 查询用户所在企业的编号和名称映射
     *
     * @param userId
     * @return
     */
    List<CorpNameDto> listCorpNameMapByUserId(Long userId);

    /**
     * 模糊查询企业
     *
     * @param corpRegistryFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 11:02
     **/
    List<CorpRegistry> matchCorp(CorpRegistryFilter corpRegistryFilter);

    /**
     * 查询企业带有注册人姓名
     * @param corpIdList
     * @return
     */
    List<CorpDto> selectCorpWithRegUserName(@Param("corpIdList") List<Long> corpIdList);


    CorpDto selectCorpWithRegUserNameById(Long corpId);

    String selectMaxCorpCode();

    /**
     * 根据用户id查询用户
     * @param userId
     * @return
     */
    List<CorpRoleDto> listCorpRoleByUserId(Long userId);
}