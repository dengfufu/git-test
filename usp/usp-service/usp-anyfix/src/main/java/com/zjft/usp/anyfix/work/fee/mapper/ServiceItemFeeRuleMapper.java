package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.fee.dto.ServiceItemFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.ServiceItemFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.model.ServiceItemFeeRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单服务项目结算规则 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
public interface ServiceItemFeeRuleMapper extends BaseMapper<ServiceItemFeeRule> {

    /**
     * 分页条件查询
     *
     * @author canlei
     * @param page
     * @param serviceItemFeeRuleFilter
     * @return
     */
    List<ServiceItemFeeRuleDto> pageByFilter(Page page, @Param("serviceItemFeeRuleFilter") ServiceItemFeeRuleFilter serviceItemFeeRuleFilter);

    /**
     * 条件查询（无分页）
     *
     * @author canlei
     * @param serviceItemFeeRuleFilter
     * @return
     */
    List<ServiceItemFeeRuleDto> listByFilter(@Param("serviceItemFeeRuleFilter") ServiceItemFeeRuleFilter serviceItemFeeRuleFilter);

    /**
     * 匹配服务项目费用规则
     *
     * @author canlei
     * @param workDto
     * @return
     */
    List<ServiceItemFeeRuleDto> matchServiceItemFeeRule(@Param("workDto") WorkDto workDto);

}
