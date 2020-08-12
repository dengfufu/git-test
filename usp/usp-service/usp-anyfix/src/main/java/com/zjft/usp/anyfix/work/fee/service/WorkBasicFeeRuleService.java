package com.zjft.usp.anyfix.work.fee.service;

import com.zjft.usp.anyfix.work.fee.dto.WorkBasicFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkBasicFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkBasicFeeRule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.model.ListWrapper;

/**
 * <p>
 * 工单基础服务费规则 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
public interface WorkBasicFeeRuleService extends IService<WorkBasicFeeRule> {

    /**
     * 分页查询
     *
     * @author canlei
     * @param workBasicFeeRuleFilter
     * @return
     */
    ListWrapper<WorkBasicFeeRuleDto> query(WorkBasicFeeRuleFilter workBasicFeeRuleFilter);

    /**
     * 添加
     *
     * @author canlei
     * @param workBasicFeeRuleDto
     */
    void add(WorkBasicFeeRuleDto workBasicFeeRuleDto);

    /**
     * 更新
     *
     * @author canlei
     * @param workBasicFeeRuleDto
     */
    void update(WorkBasicFeeRuleDto workBasicFeeRuleDto);

    /**
     * 删除
     *
     * @author canlei
     * @param ruleId
     */
    void deleteById(Long ruleId);

    /**
     * 匹配工单基础服务费规则
     *
     * @author canlei
     * @param workDto
     * @return
     */
    WorkBasicFeeRuleDto matchWorkBasicFeeRule(WorkDto workDto);

}
