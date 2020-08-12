package com.zjft.usp.anyfix.corp.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.corp.user.dto.DeviceBranchUserDto;
import com.zjft.usp.anyfix.corp.user.filter.DeviceBranchUserFilter;
import com.zjft.usp.anyfix.corp.user.mapper.DeviceBranchUserMapper;
import com.zjft.usp.anyfix.corp.user.model.DeviceBranchUser;
import com.zjft.usp.anyfix.corp.user.model.DeviceBranchUserTrace;
import com.zjft.usp.anyfix.corp.user.service.DeviceBranchUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.user.service.DeviceBranchUserTraceService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.service.UasFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备网点人员表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceBranchUserServiceImpl extends ServiceImpl<DeviceBranchUserMapper, DeviceBranchUser> implements DeviceBranchUserService {

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private RightFeignService rightFeignService;

    @Autowired
    private DeviceBranchUserTraceService deviceBranchUserTraceService;

    @Autowired
    private DeviceBranchService deviceBranchService;

    /**
     * 分页查询设备网点人员
     *
     * @param deviceBranchUserFilter
     * @return
     * @author zgpi
     * @date 2019/12/18 09:57
     **/
    @Override
    public ListWrapper<DeviceBranchUserDto> query(DeviceBranchUserFilter deviceBranchUserFilter) {
        Page page = new Page(deviceBranchUserFilter.getPageNum(), deviceBranchUserFilter.getPageSize());
        QueryWrapper<DeviceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", deviceBranchUserFilter.getBranchId());
        queryWrapper.orderByAsc("add_time");
        queryWrapper.orderByAsc("user_id");
        IPage<DeviceBranchUser> deviceBranchUserIPage = this.page(page, queryWrapper);
        List<DeviceBranchUser> deviceBranchUserList = deviceBranchUserIPage.getRecords();
        List<DeviceBranchUserDto> deviceBranchUserDtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(deviceBranchUserList)) {
            List<Long> userIdList = deviceBranchUserList.stream()
                    .map(e -> e.getUserId()).collect(Collectors.toList());
            Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            Map<Long, String> userMap = new HashMap<>();
            if (userMapResult != null && userMapResult.getCode() == Result.SUCCESS) {
                userMap = userMapResult.getData();
            }
            Result<Map<Long, String>> userMobileMapResult = uasFeignService.mapUserIdAndMobileByUserIdList(JsonUtil.toJson(userIdList));
            Map<Long, String> userMobileMap = new HashMap<>();
            if (userMobileMapResult != null && userMobileMapResult.getCode() == Result.SUCCESS) {
                userMobileMap = userMobileMapResult.getData();
            }
            String roleUserJson = JsonUtil.toJsonString("corpId", deviceBranchUserFilter.getCorpId(),
                    "userIdList", userIdList);
            Result<Map<Long, String>> userRoleMapResult = rightFeignService
                    .mapUserIdAndRoleNames(roleUserJson);
            Map<Long, String> userRoleMap = new HashMap<>();
            if (userRoleMapResult != null && userRoleMapResult.getCode() == Result.SUCCESS) {
                userRoleMap = userRoleMapResult.getData();
            }
            DeviceBranchUserDto deviceBranchUserDto;
            for (DeviceBranchUser deviceBranchUser : deviceBranchUserList) {
                deviceBranchUserDto = new DeviceBranchUserDto();
                deviceBranchUserDto.setUserId(deviceBranchUser.getUserId());
                deviceBranchUserDto.setUserName(StrUtil.trimToEmpty(userMap.get(deviceBranchUser.getUserId())));
                deviceBranchUserDto.setPhone(StrUtil.trimToEmpty(userMobileMap.get(deviceBranchUser.getUserId())));
                deviceBranchUserDto.setRoleNames(StrUtil.trimToEmpty(userRoleMap.get(deviceBranchUser.getUserId())));
                deviceBranchUserDto.setAddTime(deviceBranchUser.getAddTime());
                deviceBranchUserDtoList.add(deviceBranchUserDto);
            }
        }
        return ListWrapper.<DeviceBranchUserDto>builder()
                .list(deviceBranchUserDtoList)
                .total(deviceBranchUserIPage.getTotal())
                .build();
    }

    /**
     * 添加网点人员
     *
     * @param deviceBranchUserDto
     * @return
     * @author zgpi
     * @date 2019/11/28 09:48
     **/
    @Override
    public void addBranchUser(DeviceBranchUserDto deviceBranchUserDto) {
        if (CollectionUtil.isNotEmpty(deviceBranchUserDto.getUserIdList())) {
            List<DeviceBranchUser> deviceBranchUserList = new ArrayList<>();
            DeviceBranchUser deviceBranchUser;
            for (Long userId : deviceBranchUserDto.getUserIdList()) {
                deviceBranchUser = new DeviceBranchUser();
                deviceBranchUser.setBranchId(deviceBranchUserDto.getBranchId());
                deviceBranchUser.setUserId(userId);
                deviceBranchUser.setAddTime(DateUtil.date());
                deviceBranchUserList.add(deviceBranchUser);
            }
            this.saveBatch(deviceBranchUserList);
        }
    }

    /**
     * 删除网点人员
     *
     * @param userId
     * @param branchId
     * @return
     * @author zgpi
     * @date 2019/11/28 09:48
     **/
    @Override
    public void delBranchUser(Long userId, Long branchId) {
        UpdateWrapper<DeviceBranchUser> deviceBranchUserUpdateWrapper = new UpdateWrapper<>();
        deviceBranchUserUpdateWrapper.eq("branch_id", branchId);
        deviceBranchUserUpdateWrapper.eq("user_id", userId);
        this.remove(deviceBranchUserUpdateWrapper);
    }

    @Override
    public ListWrapper<CorpUserDto> queryAvailable(DeviceBranchUserFilter serviceBranchUserFilter) {
        if (LongUtil.isZero(serviceBranchUserFilter.getBranchId())) {
            throw new AppException("网点编号不能为空");
        }
        QueryWrapper<DeviceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", serviceBranchUserFilter.getBranchId());
        List<DeviceBranchUser> deviceBranchUserList = this.list(queryWrapper);
        List<Long> userIdList = deviceBranchUserList.stream()
                .map(e -> e.getUserId()).collect(Collectors.toList());
        JSONObject dataJson = new JSONObject();
        dataJson.put("matchFilter", serviceBranchUserFilter.getUserName());
        dataJson.put("corpId", serviceBranchUserFilter.getCorpId());
        dataJson.put("excludeUserIdList", userIdList);
        dataJson.put("pageNum", serviceBranchUserFilter.getPageNum());
        dataJson.put("pageSize", serviceBranchUserFilter.getPageSize());
        ListWrapper userDtoList = new ListWrapper();
        Result result = uasFeignService.queryCorpUser(dataJson.toJSONString());
        if (result != null && result.getCode() == Result.SUCCESS) {
            userDtoList = JsonUtil.parseObject(JsonUtil.toJson(result.getData()), ListWrapper.class);
        }
        return userDtoList;
    }

    @Override
    public List<CorpUserDto> matchAvailable(DeviceBranchUserFilter serviceBranchUserFilter) {
        if (LongUtil.isZero(serviceBranchUserFilter.getBranchId())) {
            throw new AppException("网点编号不能为空");
        }
        QueryWrapper<DeviceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", serviceBranchUserFilter.getBranchId());
        List<DeviceBranchUser> deviceBranchUserList = this.list(queryWrapper);
        List<Long> userIdList = deviceBranchUserList.stream()
                .map(e -> e.getUserId()).collect(Collectors.toList());
        JSONObject dataJson = new JSONObject();
        dataJson.put("matchFilter", serviceBranchUserFilter.getUserName());
        dataJson.put("corpId", serviceBranchUserFilter.getCorpId());
        dataJson.put("excludeUserIdList", userIdList);
        Result result = uasFeignService.matchCorpUser(dataJson.toJSONString());
        List<CorpUserDto> userDtoList = new ArrayList<>();
        if (result != null && result.getCode() == Result.SUCCESS) {
            userDtoList = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpUserDto.class);
        }
        return userDtoList;
    }

    @Override
    public void delDeviceUserByCorp(Long userId, Long corpId, Long currentUserId, String clientId) {
        // 当前企业的DeviceBranch
        List<DeviceBranch> branchList = deviceBranchService.list(new QueryWrapper<DeviceBranch>().eq("custom_corp", corpId));
        if (CollectionUtil.isEmpty(branchList)) {
            log.info("企业:{},没有DeviceBranch，无需delDeviceUserByCorp操作", corpId);
        } else {
            List<Long> branchIdList = branchList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
            // 用户所属的DeviceBranch
            List<DeviceBranchUser> deviceBranchUserList = this.list(new QueryWrapper<DeviceBranchUser>().in("branch_id", branchIdList).eq("user_id", userId));
            if (CollectionUtil.isNotEmpty(deviceBranchUserList)) {
                List<Long> deviceBranchUserIdList = deviceBranchUserList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
                this.remove(new UpdateWrapper<DeviceBranchUser>().in("branch_id", deviceBranchUserIdList).eq("user_id", userId));
                // 增加备份
                List<DeviceBranchUserTrace> deviceBranchUserTraceList = new ArrayList<>();
                Date date = DateUtil.date();
                deviceBranchUserIdList.forEach(branchId -> {
                    DeviceBranchUserTrace deviceBranchUserTrace = new DeviceBranchUserTrace();
                    deviceBranchUserTrace
                            .setBranchId(branchId)
                            .setUserId(userId)
                            .setOperate(2)
                            .setOperator(currentUserId)
                            .setClientId(clientId)
                            .setOperateTime(date);
                    deviceBranchUserTraceList.add(deviceBranchUserTrace);
                });
                deviceBranchUserTraceService.saveBatch(deviceBranchUserTraceList);
            }
        }
    }
}
