package com.zjft.usp.anyfix.work.fee.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.work.fee.dto.WorkBasicFeeRuleDto;
import com.zjft.usp.anyfix.work.fee.filter.WorkBasicFeeRuleFilter;
import com.zjft.usp.anyfix.work.fee.model.WorkBasicFeeRule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单基础服务费规则 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
public interface WorkBasicFeeRuleMapper extends BaseMapper<WorkBasicFeeRule> {

    /**
     * 分页条件查询
     *
     * @author canlei
     * @param page
     * @param workBasicFeeRuleFilter
     * @return
     */
    List<WorkBasicFeeRuleDto> pageByFilter(Page page, @Param("workBasicFeeRuleFilter") WorkBasicFeeRuleFilter workBasicFeeRuleFilter);

    /**
     * 条件查询（无分页）
     *
     * @author canlei
     * @param workBasicFeeRuleFilter
     * @return
     */
    List<WorkBasicFeeRuleDto> listByFilter(@Param("workBasicFeeRuleFilter") WorkBasicFeeRuleFilter workBasicFeeRuleFilter);

    /**
     * 匹配工单基础服务费规则
     *
     * @author canlei
     * @param workDto
     * @return
     */
    List<WorkBasicFeeRuleDto> matchWorkBasicFeeRule(@Param("workDto") WorkDto workDto);

}
