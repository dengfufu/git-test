package com.zjft.usp.anyfix.work.fee.service;

import com.zjft.usp.anyfix.work.fee.dto.ServiceItemFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.ServiceItemFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.model.ServiceItemFeeRule;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.model.ListWrapper;

import java.util.List;

/**
 * <p>
 * 工单服务项目结算规则 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
public interface ServiceItemFeeRuleService extends IService<ServiceItemFeeRule> {

    /**
     * 分页查询
     *
     * @author canlei
     * @param serviceItemFeeRuleFilter
     * @return
     */
    ListWrapper<ServiceItemFeeRuleDto> query(ServiceItemFeeRuleFilter serviceItemFeeRuleFilter);

    /**
     * 添加
     *
     * @author canlei
     * @param serviceItemFeeRuleDto
     */
    void add(ServiceItemFeeRuleDto serviceItemFeeRuleDto);

    /**
     * 更新
     *
     * @author canlei
     * @param serviceItemFeeRuleDto
     */
    void update(ServiceItemFeeRuleDto serviceItemFeeRuleDto);

    /**
     * 删除
     *
     * @author canlei
     * @param ruleId
     */
    void deleteById(Long ruleId);

    /**
     * 匹配服务项目费用规则
     *
     * @author canlei
     * @param workDto
     * @return
     */
    List<ServiceItemFeeRuleDto> matchServiceItemFeeRule(WorkDto workDto);

}
