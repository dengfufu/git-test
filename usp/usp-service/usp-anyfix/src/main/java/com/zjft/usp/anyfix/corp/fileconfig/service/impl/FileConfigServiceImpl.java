package com.zjft.usp.anyfix.corp.fileconfig.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.WorkSysTypeEnum;
import com.zjft.usp.anyfix.corp.fileconfig.dto.FileConfigDto;
import com.zjft.usp.anyfix.corp.fileconfig.dto.WorkFilesDto;
import com.zjft.usp.anyfix.corp.fileconfig.enums.FormTypeEnum;
import com.zjft.usp.anyfix.corp.fileconfig.filter.FileConfigFilter;
import com.zjft.usp.anyfix.corp.fileconfig.mapper.FileConfigMapper;
import com.zjft.usp.anyfix.corp.fileconfig.model.FileConfig;
import com.zjft.usp.anyfix.corp.fileconfig.model.WorkFiles;
import com.zjft.usp.anyfix.corp.fileconfig.service.FileConfigService;
import com.zjft.usp.anyfix.corp.fileconfig.service.WorkFilesService;
import com.zjft.usp.anyfix.work.finish.enums.FileConfigFormTypeEnum;
import com.zjft.usp.anyfix.work.finish.enums.FilesStatusEnum;
import com.zjft.usp.anyfix.work.finish.model.WorkFinish;
import com.zjft.usp.anyfix.work.finish.service.WorkFinishService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static com.zjft.usp.anyfix.corp.fileconfig.constant.GroupConstant.FORM_PREFIX;

/**
 * <p>
 * 文件配置定义表 服务实现类
 * </p>
 *
 * @author zrlin
 * @since 2020-04-30
 */
@Service
public class FileConfigServiceImpl extends ServiceImpl<FileConfigMapper, FileConfig> implements FileConfigService {

    @Resource
    private WorkFilesService workFilesService;

    @Resource
    private WorkFinishService workFinishService;
    @Override
    public void addFileConfig(FileConfig fileConfig, UserInfo userInfo) {
        if(LongUtil.isZero(fileConfig.getRefId())) {
            throw new AppException("委托商不能为空");
        }
        if(StrUtil.isEmpty(fileConfig.getGroupName())) {
            throw new AppException("类型名称不能为空");
        }
        if(fileConfig.getFormType() == null) {
            throw new AppException("用于表单不能为空");
        }
        if(fileConfig.getWorkType() == null) {
            throw new AppException("工单类型不能为空");
        }
        QueryWrapper<FileConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("ref_id", fileConfig.getRefId()).eq("group_name", fileConfig.getGroupName())
                        .eq("form_type", fileConfig.getFormType())
                        .eq("work_type", fileConfig.getWorkType());
        FileConfig config = this.getOne(wrapper);
        if(config != null) {
            throw new AppException("该附件配置已经存在");
        }
        fileConfig.setId(KeyUtil.getId());
        fileConfig.setOperator(userInfo.getUserId());
        fileConfig.setOperateTime(DateUtil.date().toTimestamp());
        this.save(fileConfig);
    }

    @Override
    public ListWrapper<FileConfigDto> getFileConfigList(FileConfigFilter filter) {
        Page<FileConfigDto> page = new Page(filter.getPageNum(), filter.getPageSize());
        if(LongUtil.isZero(filter.getRefId())) {
            throw new AppException("委托关系编号不能为空");
        }
        List<FileConfigDto> fileConfigs = this.baseMapper.selectFileConfig(page, filter.getRefId());

        if(CollectionUtil.isNotEmpty(fileConfigs)) {
            appendFileName(fileConfigs);
        }
         return ListWrapper.<FileConfigDto>builder()
                .list(fileConfigs)
                .total(page.getTotal())
                .build();
    }

    public List<FileConfigDto> getFileConfigListByDemander(FileConfigFilter filter) {
        if(LongUtil.isZero(filter.getServiceCorp())) {
            throw new AppException("服务商编号不能为空");
        }
        if(LongUtil.isZero(filter.getDemanderCorp())) {
            throw new AppException("委托商编号不能为空");
        }
        if( filter.getWorkType() == null) {
            throw new AppException("工单类型不能为空");
        }
        List<FileConfigDto> fileConfigs = this.baseMapper.selectFileConfigByDemander(filter);
        if(CollectionUtil.isNotEmpty(fileConfigs)) {
            appendFileName(fileConfigs);
        }
        return fileConfigs;
    }

    @Override
    public void updateFileConfig(FileConfig fileConfig, UserInfo userInfo) {
        if(LongUtil.isZero(fileConfig.getId())) {
            throw new AppException("主键编号不能为空");
        }
        if(LongUtil.isZero(fileConfig.getRefId())) {
            throw new AppException("委托商不能为空");
        }
        if(StrUtil.isEmpty(fileConfig.getGroupName())) {
            throw new AppException("类型名称不能为空");
        }
        if(fileConfig.getFormType() == null) {
            throw new AppException("用于表单不能为空");
        }
        fileConfig.setOperator(userInfo.getUserId());
        fileConfig.setOperateTime(DateUtil.date().toTimestamp());
        this.updateById(fileConfig);
    }

    public void appendFileName(List<FileConfigDto> fileConfigs) {
        for(FileConfigDto fileConfigDto: fileConfigs) {
            fileConfigDto.setFormTypeName(FormTypeEnum.getNameByCode(fileConfigDto.getFormType()));
            fileConfigDto.setWorkTypeName(WorkSysTypeEnum.getNameByCode(fileConfigDto.getWorkType()));
        }
    }

    @Override
    public void deleteFileConfig(Long id) {
        if(LongUtil.isZero(id)) {
            throw new AppException("编号不能为空");
        }
        QueryWrapper<WorkFiles> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").eq("config_id", id);
        List<WorkFiles> workFilesList = this.workFilesService.getBaseMapper()
                                            .selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(workFilesList)) {
            throw new AppException("该分组已经在使用，无法进行删除");
        }
        this.baseMapper.deleteById(id);
    }

    @Override
    public FileConfig getFileConfigById(Long id) {
        if(LongUtil.isZero(id)) {
            throw new AppException("编号不能为空");
        }
        FileConfig fileConfig = this.getById(id);
        return fileConfig;
    }

    @Override
    public void validateConfig(List<Long> fileList, Long configId) {
        if(LongUtil.isZero(configId)) {
            throw new AppException("配置编号不能为空");
        }
        FileConfig fileConfig = this.getById(configId);
        if(CollectionUtil.isEmpty(fileList) && fileConfig.getMinNum() > 0) {
            throw new AppException(fileConfig.getGroupName() + "至少需要" + fileConfig.getMinNum() +"张");
        }
        if(fileList.size() < fileConfig.getMinNum()) {
            throw new AppException(fileConfig.getGroupName() + "至少需要" + fileConfig.getMinNum() +"张");
        }
        if(fileList.size() > fileConfig.getMinNum()) {
            throw new AppException(  fileConfig.getGroupName() + "最多允许" + fileConfig.getMaxNum() +"张");
        }
    }
    @Override
    public void  testOtherConfigStatus(FileConfigFilter filter) {
        filter.setFormType(FileConfigFormTypeEnum.FINISH.getCode());
        List<WorkFilesDto> workFiles = this.workFilesService.getWorkFileList(filter);
        boolean isPass = true;
        for(WorkFilesDto filesDto : workFiles) {
            if(!filesDto.isPass()) {
                isPass = false;
                break;
            }
        }
        if (isPass) {
            WorkFinish workFinish = new WorkFinish();
            workFinish.setWorkId(filter.getWorkId());
            workFinish.setFilesStatus(FilesStatusEnum.PASS.getCode());
            workFinishService.updateById(workFinish);
        }
    }

     @Override
     public boolean validateConfig(List<FileConfigDto> fileConfigDtos, Map<String, List<Long>> groupFileListMap) {
         StringBuilder stringBuilder = null;
         boolean flag = true;
        if(CollectionUtil.isNotEmpty(fileConfigDtos)) {
            // 没有数据
            if(CollectionUtil.isEmpty(groupFileListMap)) {
                return false;
            } else {
                stringBuilder = new StringBuilder();
                for (FileConfigDto fileConfigDto : fileConfigDtos) {
                    Long id = fileConfigDto.getId();
                    List<Long> groupFileList = groupFileListMap.get(FORM_PREFIX + id);
                    if(groupFileList == null ) {
                        continue;
                    }
                    // 有数据但是小于指定数量
                    if (CollectionUtil.isEmpty(groupFileList) || groupFileList.size() < fileConfigDto.getMinNum()) {
                        flag = false;
                    } else if (groupFileList.size() > fileConfigDto.getMaxNum()) {
                        // 超过所需数量
                        stringBuilder.append(fileConfigDto.getGroupName() + "超过所需" + fileConfigDto.getMaxNum() + "张" + "\n");
                    }
                }
            }
        }
        if(stringBuilder != null && stringBuilder.length() > 0) {
            throw new AppException(stringBuilder.toString());
        }
        return  flag;
    }
}
