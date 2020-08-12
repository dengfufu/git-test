package com.zjft.usp.wms.business.trans.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.trans.dto.TransStatCountDto;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.filter.TransFilter;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 调拨信息共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
public interface TransWareCommonMapper extends BaseMapper<TransWareCommon> {

    /**
     * 分页查询已提交的调拨单
     *
     * @author canlei
     * @date 2019-12-09
     * @param page
     * @param transFilter
     * @return
     */
    List<TransWareCommonDto> queryByPage(Page page, @Param("transFilter") TransFilter transFilter);

    /**
     * 统计各个状态数量
     * @param transFilter
     * @return
     */
    List<TransStatCountDto> countByWareStatus( @Param("transFilter") TransFilter transFilter);

}
