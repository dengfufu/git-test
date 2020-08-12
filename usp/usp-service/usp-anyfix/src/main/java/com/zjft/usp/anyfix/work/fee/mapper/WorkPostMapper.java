package com.zjft.usp.anyfix.work.fee.mapper;

import com.zjft.usp.anyfix.work.fee.dto.WorkPostDto;
import com.zjft.usp.anyfix.work.fee.model.WorkPost;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单邮寄费 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
public interface WorkPostMapper extends BaseMapper<WorkPost> {

    /**
     * 模糊匹配邮寄公司
     *
     * @param workPostDto
     * @return
     */
    List<WorkPostDto> matchPostCorp(@Param("workPostDto") WorkPostDto workPostDto);

}
