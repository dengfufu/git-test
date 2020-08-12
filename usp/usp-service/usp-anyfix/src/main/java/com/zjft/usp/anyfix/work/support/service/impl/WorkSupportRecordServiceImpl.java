package com.zjft.usp.anyfix.work.support.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.netty.util.internal.StringUtil;
import com.zjft.usp.anyfix.work.support.dto.WorkSupportRecordDto;
import com.zjft.usp.anyfix.work.support.model.WorkSupport;
import com.zjft.usp.anyfix.work.support.model.WorkSupportRecord;
import com.zjft.usp.anyfix.work.support.mapper.WorkSupportRecordMapper;
import com.zjft.usp.anyfix.work.support.service.WorkSupportRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.support.service.WorkSupportService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 技术支持跟踪记录表 服务实现类
 * </p>
 *
 * @author cxd
 * @since 2020-04-28
 */
@Service
public class WorkSupportRecordServiceImpl extends ServiceImpl<WorkSupportRecordMapper, WorkSupportRecord> implements WorkSupportRecordService {
    @Resource
    private WorkSupportRecordMapper workSupportRecordMapper;
    @Autowired
    private WorkSupportService workSupportService;

    @Resource
    private UasFeignService uasFeignService;
    private Map<Long, String> userMap = null;
    /**
     * 根据技术id查询所有工单技术支持列表
     * @date 2020/4/28
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupportRecord>
     */
    @Override
    public List<WorkSupportRecord> queryBySupportId(Long supportId) {
        if(supportId == null){
            throw new AppException("参数错误，请检查！");
        }
        return this.list(new QueryWrapper<WorkSupportRecord>().eq("support_id",supportId));
    }

    /**
     * 添加跟踪记录
     * @date 2020/4/28
     * @param  workSupportRecordDto
     * @param  userInfo
     * @param  reqParam
     * @return void
     */
    @Override
    public void addWorkSupportRecord(WorkSupportRecordDto workSupportRecordDto, UserInfo userInfo, ReqParam reqParam) {
        WorkSupport workSupport = workSupportService.queryById(workSupportRecordDto.getSupportId());
        if("Y".equals(workSupport.getSupportType())){
            // 关闭技术支持
            if(workSupportRecordDto.getSupportType() != null){
                if("N".equals(workSupportRecordDto.getSupportType())){
                    workSupport.setSupportType(workSupportRecordDto.getSupportType());
                    workSupport.setCloseTime(DateTime.now());
                }
            }
            if(workSupport.getHandler().equals("")){
                workSupport.setHandler(String.valueOf(workSupportRecordDto.getOperator()));
            }
            WorkSupportRecord record = new WorkSupportRecord();
            BeanUtils.copyProperties(workSupportRecordDto, record);
            record.setId(KeyUtil.getId());
            if(LongUtil.isNotZero(workSupportRecordDto.getOperator())){
                record.setOperator(workSupportRecordDto.getOperator());
            }else {
                record.setOperator(userInfo.getUserId());
            }
            record.setOperateTime(DateTime.now());

            workSupportService.saveOrUpdate(workSupport);
            this.save(record);
        }else if("N".equals(workSupport.getSupportType())){
            throw new AppException("技术支持已关闭，请勿操作！");
        }

    }

    /**
     * 根据supportId查询跟踪记录列表
     * @date 2020/4/28
     * @param supportIdList
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupportRecord>
     */
    @Override
    public List<WorkSupportRecordDto> queryWorkSupportRecordList(List<Long> supportIdList) {
        List<WorkSupportRecordDto> RecordDtoList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(supportIdList)) {
            List<Long> userIdList = new ArrayList<>();
            List<WorkSupportRecord> recordList= workSupportRecordMapper.selectList(new QueryWrapper<WorkSupportRecord>().
                    in("support_id",supportIdList).orderByAsc("operate_time"));
            if (CollectionUtil.isNotEmpty(recordList)) {
                for (WorkSupportRecord record : recordList) {
                    if(LongUtil.isNotZero(record.getOperator())) {
                        userIdList.add(record.getOperator());
                    }
                }
            }
            Result<Map<Long, String>> userResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            if (userResult.getCode() == Result.SUCCESS) {
                userMap = userResult.getData();
                userMap = userMap == null ? new HashMap<>(0) : userMap;
            }
            if(CollectionUtil.isNotEmpty(recordList)){
                WorkSupportRecordDto RecordDto;
                for (WorkSupportRecord record : recordList) {
                    RecordDto = new WorkSupportRecordDto();
                    BeanUtils.copyProperties(record, RecordDto);
                    RecordDto.setOperatorName(userMap.get(record.getOperator()));
                    RecordDtoList.add(RecordDto);
                }
            }
        }
        return RecordDtoList;
    }
}
