package com.zjft.usp.anyfix.corp.fileconfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.corp.fileconfig.dto.FileConfigDto;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.model.FileConfig;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 文件配置定义表 Mapper 接口
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
public interface FileConfigMapper extends BaseMapper<FileConfig> {

    List<FileConfigDto> selectFileConfig(Long serviceCorp);

    List<FileConfigDto> selectFileConfig(Page page, @Param("refId") Long refId);

    List<FileConfigDto> selectFileConfigByDemander(@Param("filter") FileConfigFilter filter);

    List<FileConfigDto> selectFilesByDemander(@Param("filter") FileConfigFilter filter);
}
