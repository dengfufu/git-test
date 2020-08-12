package com.zjft.usp.anyfix.corp.fileconfig.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.corp.fileconfig.dto.WorkFilesDto;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.model.WorkFiles;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 分组文件表 Mapper 接口
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
public interface WorkFilesMapper extends BaseMapper<WorkFiles> {

    List<WorkFilesDto> selectWorkFiles(@Param("filter") FileConfigFilter filter );

    WorkFilesDto selectDetailById(Long id);

    WorkFilesDto selectWorkFilesByConfigIdAndWorkId(@Param("configId") Long configId,
                                                @Param("workId") Long workId);
}
