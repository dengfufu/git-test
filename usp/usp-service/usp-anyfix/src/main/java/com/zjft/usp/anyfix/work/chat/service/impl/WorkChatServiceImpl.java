package com.zjft.usp.anyfix.work.chat.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.work.chat.dto.WorkChatDto;
import com.zjft.usp.anyfix.work.chat.enums.QueryTypeEnum;
import com.zjft.usp.anyfix.work.chat.filter.WorkChatFilter;
import com.zjft.usp.anyfix.work.chat.mapper.WorkChatMapper;
import com.zjft.usp.anyfix.work.chat.model.WorkChat;
import com.zjft.usp.anyfix.work.chat.service.WorkChatService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.listener.WorkMqTopic;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.file.dto.FileDto;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.mq.util.MqSenderUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author linzerun
 * @since 2020-03-09
 */
@Service
public class WorkChatServiceImpl extends ServiceImpl<WorkChatMapper, WorkChat> implements WorkChatService {

    @Autowired
    private UasFeignService uasFeignService;

    @Autowired
    private FileFeignService fileFeignService;

    @Autowired
    private MqSenderUtil mqSenderUtil;

    @Autowired
    private WorkDealService workDealService;

    @Override
    public void sendMsg(WorkChat workChat) {
        if (workChat.getWorkId() == null) {
            throw new AppException("编号不能为空");
        }
        if (workChat.getMsgType() == 0) {
            throw new AppException("消息类型不能为空");
        }
        Integer order = this.baseMapper.selectMaxOrder(workChat.getWorkId());
        order = order == null ? 0 : order + 1;
        workChat.setMsgOrder(order);
        workChat.setId(KeyUtil.getId());
        workChat.setCreateTime(DateUtil.date());
        save(workChat);
        if (LongUtil.isNotZero(workChat.getFileId())) {
            List<Long> files = new ArrayList<>();
            files.add(workChat.getFileId());
            if (LongUtil.isNotZero(workChat.getThumbnail())) {
                files.add(workChat.getThumbnail());
            }
            fileFeignService.deleteFileTemporaryByFileIdList(files);
        }
    }

    @Override
    public List<WorkChatDto> selectWorkChatList(WorkChatFilter workChatFilter, ReqParam reqParam) {
        if (LongUtil.isZero(workChatFilter.getWorkId())) {
            throw new AppException("工单编号不能为空");
        }
        QueryWrapper<WorkChat> wrapper = new QueryWrapper<>();
        wrapper.eq("work_id", workChatFilter.getWorkId());
        // 正常消息
        wrapper.eq("operate_type",0);
        wrapper.orderByDesc("msg_order");
        List<WorkChat> workChatList;
        // 查询发送后往后面的消息
        if(workChatFilter.getQueryType() == QueryTypeEnum.QUERY_LATEST.getCode()) {
            wrapper.ge("msg_order", workChatFilter.getOrderForLatest());
            workChatList = this.baseMapper.selectList(wrapper);
        } else  {
            IPage<WorkChat> page = new Page(workChatFilter.getPageNum(), workChatFilter.getPageSize());
            if(workChatFilter.getQueryType() == QueryTypeEnum.QUERY_MORE.getCode()) {
                if (LongUtil.isNotZero(workChatFilter.getOrderForMore())) {
                    // 往前查询消息
                    wrapper.lt("msg_order", workChatFilter.getOrderForMore());
                } else {
                 return new ArrayList<>();
                }
            }
            workChatList = this.page(page, wrapper).getRecords();
        }
        List<WorkChatDto> workChatDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(workChatList)) {
            List<Long> userIdList = new ArrayList<>();
            List<Long> fileIdList = new ArrayList<>();
            Set<Long> corpIdSet = new HashSet<>();
            for (WorkChat workChat : workChatList) {
                WorkChatDto workChatDto = new WorkChatDto();
                if (LongUtil.isNotZero(workChat.getFileId())) {
                    fileIdList.add(workChat.getFileId());
                }
                if (LongUtil.isNotZero(workChat.getCorpId())) {
                    corpIdSet.add(workChat.getCorpId());
                }
                BeanUtils.copyProperties(workChat, workChatDto);
                userIdList.add(workChat.getUserId());
                workChatDtoList.add(workChatDto);
            }
            Result<Map<Long, String>> corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdSet));
            Map<Long, String> corpMap = corpResult.getData();
            corpMap = corpMap == null ? new HashMap<>() : corpMap;

            // 设置发送人的用户名头像
            Map<String, Map<String, String>> userNameMap = uasFeignService.mapCorpUserInfoByUserIdList(userIdList,
                    reqParam.getCorpId()).getData();
            Map<Long, FileDto> fileDtoMap = new HashMap<>();
            // 如果消息包含文件，查询文件详细信息，并组装成map形式
            if (CollectionUtils.isNotEmpty(fileIdList)) {
                Result result =  fileFeignService.listFileDtoByIdList(JsonUtil.toJson(fileIdList));
                List<FileDto> fileDtoList = new ArrayList<>();
                if(result != null && result.getCode() == Result.SUCCESS) {
                    fileDtoList = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), FileDto.class);
                }
                for (FileDto fileDto : fileDtoList) {
                    fileDtoMap.put(fileDto.getFileId(), fileDto);
                }
            }

            for (WorkChatDto workChatDto : workChatDtoList) {
                workChatDto.setCorpName(corpMap.get(workChatDto.getCorpId()));
                Map<String, String> userInfoMap = userNameMap.get(workChatDto.getUserId() + "");
                if (userInfoMap != null) {
                    workChatDto.setUserName(userInfoMap.get("userName"));
                    workChatDto.setFaceImg(userInfoMap.get("faceImg"));
                }
                if (LongUtil.isNotZero(workChatDto.getFileId())) {
                    FileDto fileDto = fileDtoMap.get(workChatDto.getFileId());
                    String fileName = fileDto.getFileName();
                    int index = fileName.lastIndexOf(".");
                    String extension = "";
                    if(index > -1) {
                        extension = fileName.substring(index + 1);
                    }
                    workChatDto.setExtension(extension.toLowerCase());
                    workChatDto.setSize(fileDto.getSize());
                    workChatDto.setFileName(fileDto.getFileName());
                    workChatDto.setImg(isImg(extension));
                }
            }
        }
        return workChatDtoList;
    }

    @Override
    public void operateMsg(Long id, Integer status) {
        if(LongUtil.isZero(id)) {
            throw new AppException("沟通记录编号不能为空");
        }
        if(status == null || status == 0 ) {
            throw new AppException("操作类型不能为空");
        }
        WorkChat workChat = new WorkChat();
        workChat.setId(id);
        workChat.setOperateType(status);
        this.updateById(workChat);
    }

    @Override
    public void notify(Long workId,Long excludeUserId) {
        QueryWrapper<WorkChat> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("user_id").eq("work_id",workId);
        List<WorkChat> workChatList = this.list(queryWrapper);
        List<Long > userIdList =  workChatList.stream().map(e -> e.getUserId()).distinct().collect(Collectors.toList());
        Set<Long> userSet = new HashSet<>(userIdList);
        QueryWrapper<WorkDeal> workDealQueryWrapper = new QueryWrapper<>();
        workDealQueryWrapper.select("assign_staff","engineer","work_code").eq("work_id",workId);
        WorkDeal workDeal = workDealService.getOne(workDealQueryWrapper);
        if(LongUtil.isNotZero(workDeal.getEngineer())) {
            userSet.add(workDeal.getEngineer());
        }
        if(LongUtil.isNotZero(workDeal.getAssignStaff())) {
            userSet.add(workDeal.getAssignStaff());
        }
        userSet.remove(excludeUserId);
        if(CollectionUtil.isEmpty(userSet)) {
            return;
        }
        String userIds = CollectionUtil.join(userSet, ",");
        Map<String, Object> msg = new HashMap<>(1);
        msg.put("workId", workId);
        msg.put("workCode", workDeal.getWorkCode());
        msg.put("userIds",userIds);
        mqSenderUtil.sendMessage(WorkMqTopic.WORK_CHAT, JsonUtil.toJson(msg));
    }

    public boolean isImg(String extension) {
        extension = extension != null ? extension.toLowerCase().trim() : null;
        if("png".equals(extension)
                || "bmp".equals(extension)
//                || "tif".equals(extension)
//                || "tiff".equals(extension)
                || "jpe".equals(extension)
                || "gif".equals(extension)
                || "jpeg".equals(extension)
                || "jpg".equals(extension)) {
            return true;
        }
        return false;
    }
}
