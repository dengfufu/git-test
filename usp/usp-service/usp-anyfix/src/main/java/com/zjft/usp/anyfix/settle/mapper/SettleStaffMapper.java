package com.zjft.usp.anyfix.settle.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.settle.dto.SettleStaffDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffFilter;
import com.zjft.usp.anyfix.settle.model.SettleStaff;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 员工结算单 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleStaffMapper extends BaseMapper<SettleStaff> {

    /**
     * 查询员工结算单
     * @param page
     * @param settleStaffFilter
     * @return
     */
    List<SettleStaffDto> querySettleStaffDto(Page page, @Param("settleStaffFilter") SettleStaffFilter settleStaffFilter);

}
