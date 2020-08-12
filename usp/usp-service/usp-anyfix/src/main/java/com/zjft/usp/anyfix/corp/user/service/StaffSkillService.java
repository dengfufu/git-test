package com.zjft.usp.anyfix.corp.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.user.dto.StaffSkillDto;
import com.zjft.usp.anyfix.corp.user.filter.StaffSkillFilter;
import com.zjft.usp.anyfix.corp.user.model.StaffSkill;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 工程师技能表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
public interface StaffSkillService extends IService<StaffSkill> {

    /**
     * 根据技能和服务网点获取技能完全匹配的工程师
     *
     * @param staffSkillDto
     * @return java.util.List<java.lang.Long>
     * @author zphu
     * @date 2019/9/27 16:01
     * @throws
     **/
    List<Long> skillMatchUserByBranch(StaffSkillDto staffSkillDto);

    /**
     * 根据技能和服务网点获取技能部分匹配的工程师
     *
     * @param staffSkillDto
     * @return java.util.List<java.lang.Long>
     * @author zphu
     * @date 2019/9/29 10:30
     * @throws
     **/
    List<Long> skillMatchUserByBranchWithFuzzy(StaffSkillDto staffSkillDto);
    /**
     * 查询某些工程师是否匹配技能
     * 注意：staffSkillDto中的属性值只能为一台机器（一个工单）的值，
     * 不可以有多个
     *
     * @param staffSkillDto
     * @param userIdList
     * @return java.util.List<java.lang.Long>
     * @author zphu
     * @date 2019/9/27 16:52
     * @throws
     **/
    List<Long> skillMatchUser(StaffSkillDto staffSkillDto ,List<Long> userIdList);

    /**
     * 某些工程师是否部分匹配技能
     * staffSkillDto中的非空条件都匹配
     * @param staffSkillDto
     * @param userIdList
     * @return java.util.List<java.lang.Long>
     * @author zphu
     * @date 2019/9/29 10:14
     * @throws
     **/
    List<Long> skillMatchUserWithFuzzy(StaffSkillDto staffSkillDto ,List<Long> userIdList);

    /**
     * 分页查询
     * @param staffSkillFilter
     * @return
     */
    ListWrapper<StaffSkillDto> pageByFilter(StaffSkillFilter staffSkillFilter, ReqParam reqParam);

    /**
     * 添加工程师技能
     * @param staffSkillDto
     * @param reqParam
     */
    void addStaffSkill(StaffSkillDto staffSkillDto, ReqParam reqParam);

    /**
     * 更新工程师技能
     * @param staffSkillDto
     */
    void updateStaffSkill(StaffSkillDto staffSkillDto);
}
