package com.zjft.usp.anyfix.work.support.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.work.support.dto.WorkSupportDto;
import com.zjft.usp.anyfix.work.support.dto.WorkSupportRecordDto;
import com.zjft.usp.anyfix.work.support.enums.SeverityEnum;
import com.zjft.usp.anyfix.work.support.model.WorkSupport;
import com.zjft.usp.anyfix.work.support.mapper.WorkSupportMapper;
import com.zjft.usp.anyfix.work.support.model.WorkSupportRecord;
import com.zjft.usp.anyfix.work.support.service.WorkSupportRecordService;
import com.zjft.usp.anyfix.work.support.service.WorkSupportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 技术支持 服务实现类
 * </p>
 *
 * @author cxd
 * @since 2020-04-21
 */
@Service
public class WorkSupportServiceImpl extends ServiceImpl<WorkSupportMapper, WorkSupport> implements WorkSupportService {

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private RightFeignService rightFeignService;
    @Resource
    private WorkSupportMapper workSupportMapper;
    @Autowired
    private WorkSupportRecordService workSupportRecordService;

    private Map<Long, String> userMap = null;
    private Map<Long, List<WorkSupportRecord>> workSupportRecordMap = null;

    /**
     * 根据id查询列表
     * @date 2020/4/29
     * @param Id
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupport>
     */
    @Override
    public WorkSupport queryById(Long Id) {
        if(Id == null){
            throw new AppException("参数错误，请检查！");
        }
        return workSupportMapper.selectById(Id);
    }

    /**
     * 根据用户查询所有技术支持的工单列表
     * @date 2020/4/21
     * @param userId
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupport>
     */
    @Override
    public List<WorkSupport> queryWorkSupportByUserId(Long userId) {
        if(userId == null){
            throw new AppException("参数错误，请检查！");
        }
        return this.list(new QueryWrapper<WorkSupport>().eq("user_id",userId));
    }

    /**
     * 根据用户id和工单id查询所有工单技术支持列表
     * @date 2020/4/21
     * @param workId
     * @param userId
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupport>
     */
    @Override
    public List<WorkSupport> queryByWorkIdAndUserId(Long workId, Long userId) {
        if(userId == null){
            throw new AppException("参数错误，请检查！");
        }
        return this.list(new QueryWrapper<WorkSupport>().eq("user_id",userId));
    }

    /**
     * 根据工单id和企业id查询所有工单技术支持列表
     * @date 2020/4/22
     * @param workId
     * @return java.util.List<com.zjft.usp.anyfix.work.support.model.WorkSupport>
     */
    @Override
    public List<WorkSupportDto> queryByWorkIdAndCorpId(Long workId,Long corpId) {
        if(workId == null){
            throw new AppException("参数错误，请检查！");
        }
        List<WorkSupport> workSupportList = this.list(new QueryWrapper<WorkSupport>().eq("work_id",workId)
                .eq("corp_id",corpId).orderByAsc("operate_time"));
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> supportIdIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(workSupportList)) {
            for (WorkSupport workSupport : workSupportList) {
                // 处理人
                if(workSupport.getHandler() != null && !workSupport.getHandler().equals("")) {
                    userIdSet.add(Long.parseLong(workSupport.getHandler()));
                }
                supportIdIdSet.add(workSupport.getId());
            }
        }
        List<Long> userIdList = new ArrayList<>(userIdSet);
        // 获得处理人姓名集合
        Result<Map<Long, String>> userResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
        if (userResult.getCode() == Result.SUCCESS) {
            userMap = userResult.getData();
            userMap = userMap == null ? new HashMap<>(0) : userMap;
        }

        List<Long> supportIdList = new ArrayList<>(supportIdIdSet);
        // 获得跟踪记录
        List<WorkSupportRecordDto> workSupportRecordList = workSupportRecordService.queryWorkSupportRecordList(supportIdList);
        workSupportRecordMap = new HashMap<>();
        List<WorkSupportRecord> recordList;
        if (CollectionUtil.isNotEmpty(workSupportRecordList) &&CollectionUtil.isNotEmpty(supportIdIdSet)) {
            for (Long supportId : supportIdIdSet) {
                recordList = new ArrayList<>();
                for (WorkSupportRecordDto recordDto : workSupportRecordList) {
                    if(recordDto.getSupportId().equals(supportId)){
                        recordList.add(recordDto);
                    }
                }
                workSupportRecordMap.put(supportId, recordList);
            }

        }
        List<WorkSupportDto> workSupportDtolist = new ArrayList<>();;
        if (CollectionUtil.isNotEmpty(workSupportList)) {
            WorkSupportDto workSupportDto;
            for (WorkSupport workSupport : workSupportList) {
                workSupportDto = new WorkSupportDto();
                BeanUtils.copyProperties(workSupport, workSupportDto);
                workSupportDto.setSeverityName(SeverityEnum.getNameByCode(workSupportDto.getSeverity()));
                // 处理人
                if(workSupport.getHandler() != null && !workSupport.getHandler().equals("")) {
                    workSupportDto.setHandlerName(userMap.get(Long.parseLong(workSupport.getHandler())));
                }
                workSupportDto.setRecordList(workSupportRecordMap.get(workSupport.getId()));
                workSupportDtolist.add(workSupportDto);
            }
        }
        return workSupportDtolist;
    }

    /**
     * 添加工单技术支持
     * @date 2020/4/21
     * @param workSupportDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void addWorkSupport(WorkSupportDto workSupportDto, UserInfo userInfo, ReqParam reqParam) {
        List<WorkSupportDto> workSupportDtoList = this.queryByWorkIdAndCorpId(workSupportDto.getWorkId(),reqParam.getCorpId());
        WorkSupport workSupport = new WorkSupport();
        BeanUtils.copyProperties(workSupportDto, workSupport);
        workSupport.setId(KeyUtil.getId());
        // 如果处理人为空，则填入空字符串
        if(StringUtil.isNullOrEmpty(workSupportDto.getHandler())){
            workSupport.setHandler("");
        }
        workSupport.setOperator(userInfo.getUserId());
        workSupport.setOperateTime(DateTime.now());
        workSupport.setSupportType("Y");
        if (CollectionUtil.isNotEmpty(workSupportDtoList)) {
            for(WorkSupportDto supportDto : workSupportDtoList){
                if("Y".equals(supportDto.getSupportType())){
                    throw new AppException("技术支持已添加，请勿重复操作！");
                }
            }
            this.save(workSupport);
        }else {
            this.save(workSupport);
        }
    }

    /**
     * 根据工单编号删除工单技术支持
     * @date 2020/4/21
     * @param workId
     * @param corpId
     * @return void
     */
    @Override
    public void deleteByWorkId(Long workId,Long corpId) {
        if (workId != null&&corpId != null) {
            this.remove(new UpdateWrapper<WorkSupport>().eq("work_id", workId).eq("corp_id",corpId));
        }else {
            throw new AppException("参数错误，请检查！");
        }
    }


    /**
     * 关闭工单技术支持
     * @date 2020/4/21
     * @param workSupportDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    @Override
    public void finishWorkSupport(WorkSupportDto workSupportDto, UserInfo userInfo, ReqParam reqParam) {
        if(workSupportDto.getWorkId() == null){
            throw new AppException("参数错误，请检查！");
        }
        addWorkSupport(workSupportDto,userInfo,reqParam);

        List<WorkSupport> workSupportlist = this.list(new QueryWrapper<WorkSupport>().eq("work_id",workSupportDto.getWorkId())
                .eq("corp_id",reqParam.getCorpId()));
        List<WorkSupport> supportlist = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(workSupportlist)) {
            for (WorkSupport w : workSupportlist) {
                w.setSupportType("Y");
                supportlist.add(w);
            }
        }
        this.saveOrUpdateBatch(supportlist);
    }

    /**
     * 根据企业获取技术支持人员列表
     * @date 2020/5/14
     * @param workSupportDto
     * @return java.util.List<com.zjft.usp.anyfix.corp.user.dto.CorpUserDto>
     */
    @Override
    public List<CorpUserDto> queryListCorpUser(WorkSupportDto workSupportDto,ReqParam reqParam) {
        List<CorpUserDto> corpUserDtoList = new ArrayList<>();
        CorpUserDto corpUserDto ;
        Result<List<Long>> authResult = rightFeignService.listUserByRightId(
                workSupportDto.getCorpId(), RightConstants.WORK_SUPPORT_ADD);
        if (authResult != null && authResult.getCode() == Result.SUCCESS) {
            List<Long> list = authResult.getData();
            // 获得处理人姓名集合
            Result<Map<Long, String>> userResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(list));
            if (userResult.getCode() == Result.SUCCESS) {
                userMap = userResult.getData();
                userMap = userMap == null ? new HashMap<>(0) : userMap;
            }
            if (CollectionUtil.isNotEmpty(list)) {
                for (Long corpUserId : list) {
                    corpUserDto = new CorpUserDto();
                    corpUserDto.setUserId(corpUserId);
                    corpUserDto.setUserName(userMap.get(corpUserId));
                    corpUserDtoList.add(corpUserDto);
                }
            }
        }
        return corpUserDtoList;
    }
}
