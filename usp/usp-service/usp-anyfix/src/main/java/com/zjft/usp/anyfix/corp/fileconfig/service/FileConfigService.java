package com.zjft.usp.anyfix.corp.fileconfig.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.fileconfig.dto.FileConfigDto;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.model.FileConfig;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件配置定义表 服务类
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
public interface FileConfigService extends IService<FileConfig> {

    void addFileConfig(FileConfig fileConfig, UserInfo userInfo);

    ListWrapper<FileConfigDto> getFileConfigList(FileConfigFilter filter);

    List<FileConfigDto> getFileConfigListByDemander(FileConfigFilter filter);

    void updateFileConfig(FileConfig fileConfig, UserInfo userInfo);

    void deleteFileConfig(Long id);

    FileConfig getFileConfigById(Long id);

    void validateConfig(List<Long> fileList, Long configId);

    boolean validateConfig(List<FileConfigDto> fileConfigDtos, Map<String, List<Long>> groupFileListMap);

    void  testOtherConfigStatus(FileConfigFilter filter);
}
