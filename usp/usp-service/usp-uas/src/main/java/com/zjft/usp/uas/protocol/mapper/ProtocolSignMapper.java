package com.zjft.usp.uas.protocol.mapper;

import com.zjft.usp.uas.protocol.dto.ProtocolCheckDto;
import com.zjft.usp.uas.protocol.filter.ProtocolFilter;
import com.zjft.usp.uas.protocol.model.ProtocolSign;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户签订协议  Mapper 接口
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
public interface ProtocolSignMapper extends BaseMapper<ProtocolSign> {

    List<ProtocolCheckDto> checkSign(@Param("filter") ProtocolFilter filter);
}
