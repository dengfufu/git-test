package com.zjft.usp.anyfix.corp.fileconfig.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.corp.fileconfig.dto.WorkFilesDto;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.filter.WorkFilesFilter;
import com.zjft.usp.anyfix.corp.fileconfig.mapper.WorkFilesMapper;
import com.zjft.usp.anyfix.corp.fileconfig.model.WorkFiles;
import com.zjft.usp.anyfix.corp.fileconfig.service.FileConfigService;
import com.zjft.usp.anyfix.corp.fileconfig.service.WorkFilesService;
import com.zjft.usp.anyfix.work.finish.enums.FileConfigFormTypeEnum;
import com.zjft.usp.anyfix.work.finish.enums.FilesStatusEnum;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.finish.service.WorkFinishService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.file.service.FileFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 分组文件表 服务实现类
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
@Service
public class WorkFilesServiceImpl extends ServiceImpl<WorkFilesMapper, WorkFiles> implements WorkFilesService {

    @Resource
    private FileFeignService fileFeignService;
    @Resource
    private FileConfigService fileConfigService;

    @Resource
    private WorkFinishService workFinishService;

    @Override
    public void addWorkFiles(WorkFiles workFiles) {
        if (LongUtil.isZero(workFiles.getConfigId())) {
            throw new AppException("附件配置编号不能为空");
        }
        if (StrUtil.isBlank(workFiles.getFileIds())) {
            throw new AppException("文件列表不能为空");
        }
        workFiles.setId(KeyUtil.getId());
        this.save(workFiles);
    }

    @Override
    public List<WorkFilesDto> getWorkFileList(FileConfigFilter fileConfigFilter) {
        if(LongUtil.isZero(fileConfigFilter.getWorkId())  ||
                LongUtil.isZero(fileConfigFilter.getDemanderCorp()) ||
                LongUtil.isZero(fileConfigFilter.getServiceCorp()) ||
                fileConfigFilter.getWorkType() == null || fileConfigFilter.getWorkType() == 0
        ) {
            return null;
        }
        WorkFinish workFinish = workFinishService.getById(fileConfigFilter.getWorkId());
        List<WorkFilesDto> filesDtos = this.baseMapper.selectWorkFiles(fileConfigFilter);
        for(WorkFilesDto filesDto : filesDtos) {
            String fileIds = filesDto.getFileIds();
            if(StrUtil.isNotEmpty(fileIds)) {
                List<Long> list = Arrays.asList(fileIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                filesDto.setFileList(list);
                if(list.size() >= filesDto.getMinNum() && list.size() <= filesDto.getMaxNum()) {
                    filesDto.setPass(true);
                } else {
                    filesDto.setPass(false);
                }
            } else if(filesDto.getMinNum() > 0 ) {
                filesDto.setPass(false);
            }
        }
        // 通过
        if( workFinish!= null && FilesStatusEnum.PASS.getCode().equals(workFinish.getFilesStatus()) ) {
            if(CollectionUtil.isNotEmpty(filesDtos)) {
                return filesDtos.stream()
                        .filter(WorkFilesDto::isPass)
                        .collect(Collectors.toList());
            }

        }
        return filesDtos;
    }

    @Override
    public List<WorkFiles> getWorkFileByWorkId(Long workId) {
        if(LongUtil.isZero(workId)) {
            throw new AppException("工单编号不能为空");
        }
        QueryWrapper<WorkFiles> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("work_id", workId);
        List<WorkFiles> workFilesList = this.baseMapper.selectList(queryWrapper);
        return workFilesList;
    }

    @Override
    public WorkFilesDto getWorkFileById(Long id) {
        if(LongUtil.isZero(id)) {
            throw new AppException("编号不能为空");
        }
        WorkFilesDto workFilesDto = this.baseMapper.selectDetailById(id);
        if(workFilesDto != null ) {
            if( StrUtil.isNotEmpty(workFilesDto.getFileIds())) {
                List<Long> list = Arrays.asList(workFilesDto.getFileIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                Result fileResult = fileFeignService.listFileDtoByIdList(JsonUtil.toJson(list));
                if (fileResult.getCode() == Result.SUCCESS) {
                    List<FileInfoDto> fileInfoDtoList = JsonUtil.parseArray(JsonUtil.toJson(fileResult.getData()), FileInfoDto.class);
                    workFilesDto.setFileInfoDtosList(fileInfoDtoList);
                }
            }
        }
        return workFilesDto;
    }

    @Override
    public void updateFiles(WorkFilesFilter filesFilter) {
        if(LongUtil.isZero(filesFilter.getConfigId())) {
            throw new AppException("配置编号不能为空");
        }
        if(LongUtil.isZero(filesFilter.getDemanderCorp())) {
            throw new AppException("委托商编号不能为空");
        }
        if(LongUtil.isZero(filesFilter.getServiceCorp())) {
            throw new AppException("服务商编号不能为空");
        }
        if(filesFilter.getWorkType() == 0 ) {
            throw new AppException("工单类型不能为空");
        }
        if(CollectionUtil.isNotEmpty(filesFilter.getDeletedFileIdList())) {
            fileFeignService.deleteFileList(filesFilter.getDeletedFileIdList());
        }
        if(CollectionUtil.isNotEmpty(filesFilter.getNewFileIdList())) {
            fileFeignService.deleteFileTemporaryByFileIdList(filesFilter.getNewFileIdList());
        }
        if(CollectionUtil.isNotEmpty(filesFilter.getNowFileIdList())) {
            fileConfigService.validateConfig(filesFilter.getNowFileIdList(), filesFilter.getConfigId());
            String groupFiles = CollectionUtil.join(filesFilter.getNowFileIdList(), ",");
            WorkFiles workFiles = new WorkFiles();
            if(LongUtil.isZero(filesFilter.getId())) {
                workFiles.setId(KeyUtil.getId());
                workFiles.setConfigId(filesFilter.getConfigId());
                workFiles.setWorkId(filesFilter.getWorkId());
                workFiles.setFileIds(groupFiles);
                this.save(workFiles);
            } else {
                workFiles.setId(filesFilter.getId());
                workFiles.setFileIds(groupFiles);
                this.updateById(workFiles);
            }
            FileConfigFilter fileConfigFilter = new FileConfigFilter();
            BeanUtils.copyProperties(filesFilter, fileConfigFilter);
            fileConfigService.testOtherConfigStatus(fileConfigFilter);
        }
     }

    @Override
    public void checkStatus(WorkFilesFilter workFilesFilter) {
        if(LongUtil.isZero(workFilesFilter.getWorkId())  ||
            LongUtil.isZero(workFilesFilter.getDemanderCorp()) ||
            LongUtil.isZero(workFilesFilter.getServiceCorp()) ||
            workFilesFilter.getWorkType() == null || workFilesFilter.getWorkType() == 0
             ) {
            return;
        }
        FileConfigFilter fileConfigFilter = new FileConfigFilter();
        fileConfigFilter.setFormType(FileConfigFormTypeEnum.FINISH.getCode());
        BeanUtils.copyProperties(workFilesFilter, fileConfigFilter);
        List<WorkFilesDto> filesDtos = this.baseMapper.selectWorkFiles(fileConfigFilter);
        boolean isPass = true;
        for(WorkFilesDto filesDto : filesDtos) {
            String fileIds = filesDto.getFileIds();
            if(StrUtil.isNotEmpty(fileIds)) {
                List<Long> list = Arrays.asList(fileIds.split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                filesDto.setFileList(list);
                if (list.size() < filesDto.getMinNum() || list.size() > filesDto.getMaxNum()) {
                    isPass = false;
                    break;
                }
            } else if(filesDto.getMinNum() > 0) {
                isPass = false;
            }
        }
        if (isPass) {
            WorkFinish workFinish = new WorkFinish();
            workFinish.setWorkId(workFilesFilter.getWorkId());
            workFinish.setFilesStatus(FilesStatusEnum.PASS.getCode());
            workFinishService.updateById(workFinish);
        }
    }

    @Override
    public WorkFilesDto getWorkFileByConfigIdAndWorkId(Long configId, Long workId) {
        if( LongUtil.isZero(configId) || LongUtil.isZero(workId)) {
            throw new AppException("工单编号或者配置编号不能为空");
        }
        WorkFilesDto workFilesDto = this.baseMapper.selectWorkFilesByConfigIdAndWorkId(configId,workId);
        if(workFilesDto != null ) {
            if( StrUtil.isNotEmpty(workFilesDto.getFileIds())) {
                List<Long> list = Arrays.asList(workFilesDto.getFileIds().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                Result fileResult = fileFeignService.listFileDtoByIdList(JsonUtil.toJson(list));
                if (fileResult.getCode() == Result.SUCCESS) {
                    List<FileInfoDto> fileInfoDtoList = JsonUtil.parseArray(JsonUtil.toJson(fileResult.getData()), FileInfoDto.class);
                    workFilesDto.setFileInfoDtosList(fileInfoDtoList);
                }
            }
        }
        return  workFilesDto;
    }


}
