package com.zjft.usp.anyfix.corp.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderContDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderContFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCont;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author zrlin
 * @since 2020-06-29
 */
public interface DemanderContMapper extends BaseMapper<DemanderCont> {

    DemanderContDto selectServiceCont(DemanderContFilter filter);

    /**
     * 根据filter查询
     *
     * @param filter
     * @return
     */
    List<DemanderContDto> listByFilter(@Param("filter") DemanderContFilter filter);

}
