package com.zjft.usp.anyfix.corp.fileconfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.fileconfig.dto.WorkFilesDto;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.filter.WorkFilesFilter;
import com.zjft.usp.anyfix.corp.fileconfig.model.WorkFiles;

import java.util.List;

/**
 * <p>
 * 分组文件表 服务类
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
public interface WorkFilesService extends IService<WorkFiles> {

    void addWorkFiles(WorkFiles workFiles);

    List<WorkFilesDto> getWorkFileList(FileConfigFilter filter);

    List<WorkFiles> getWorkFileByWorkId(Long workId);

    WorkFilesDto  getWorkFileById(Long id);

    void updateFiles(WorkFilesFilter filesFilter);

    void checkStatus(WorkFilesFilter workFilesFilter);

    WorkFilesDto getWorkFileByConfigIdAndWorkId(Long configId, Long workId);
}
