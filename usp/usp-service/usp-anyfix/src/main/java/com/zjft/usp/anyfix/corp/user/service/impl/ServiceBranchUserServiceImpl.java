package com.zjft.usp.anyfix.corp.user.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.branch.filter.ServiceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.corp.user.dto.CorpUserDto;
import com.zjft.usp.anyfix.corp.user.dto.ServiceBranchUserDto;
import com.zjft.usp.anyfix.corp.user.filter.ServiceBranchUserFilter;
import com.zjft.usp.anyfix.corp.user.mapper.ServiceBranchUserMapper;
import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUser;
import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUserTrace;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserService;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserTraceService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.feign.service.RightFeignService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务网点人员表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceBranchUserServiceImpl extends ServiceImpl<ServiceBranchUserMapper, ServiceBranchUser> implements ServiceBranchUserService {

    @Autowired
    private ServiceBranchService serviceBranchService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private RightFeignService rightFeignService;

    @Autowired
    private ServiceBranchUserTraceService serviceBranchUserTraceService;

    @Override
    public List<ServiceBranchUser> listByBranchId(Long branchId) {
        Assert.notNull(branchId, "网点编号不能为空");
        QueryWrapper<ServiceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", branchId);
        List<ServiceBranchUser> serviceBranchUsers = this.list(queryWrapper);
        return serviceBranchUsers;
    }

    @Override
    public List<ServiceBranchUserDto> listDtoBy(ServiceBranchUserFilter filter) {
        List<ServiceBranchUserDto> dtoList = new ArrayList<>();
        Map<Long, String> userIdAndNameMap = null;
        List<ServiceBranchUser> list;
        if (LongUtil.isZero(filter.getBranchId())) {
            if (LongUtil.isZero(filter.getCorpId())) {
                return dtoList;
            }
            userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByCorpId(
                    filter.getCorpId()).getData();
            ServiceBranchFilter serviceBranchFilter = new ServiceBranchFilter();
            serviceBranchFilter.setServiceCorp(filter.getCorpId());
            List<ServiceBranch> branchList = serviceBranchService.listServiceBranch(serviceBranchFilter);
            List<Long> branchIdList = branchList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
            list = this.list(new QueryWrapper<ServiceBranchUser>().in("branch_id", branchIdList));

        } else {
            list = this.listByBranchId(filter.getBranchId());
            if (list != null && list.size() > 0) {
                List<Long> userIdList = new ArrayList<>();
                for (ServiceBranchUser serviceBranchUser : list) {
                    userIdList.add(serviceBranchUser.getUserId());
                }
                userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(
                        JsonUtil.toJson(userIdList)).getData();
            }
        }
        if (list != null && list.size() > 0) {
            Set<Long> userIdAddSet = new HashSet<>();
            for (ServiceBranchUser serviceBranchUser : list) {
                String userName = userIdAndNameMap.get(serviceBranchUser.getUserId());
                if ((StrUtil.isBlank(filter.getUserName())
                        || StrUtil.isNotBlank(filter.getUserName())
                        && StrUtil.isNotBlank(userName) && userName.contains(filter.getUserName()))
                        && !userIdAddSet.contains(serviceBranchUser.getUserId())) {
                    ServiceBranchUserDto dto = new ServiceBranchUserDto();
                    BeanUtils.copyProperties(serviceBranchUser, dto);
                    dto.setUserName(userName);
                    dtoList.add(dto);
                    userIdAddSet.add(serviceBranchUser.getUserId());
                }
            }
        }
        return dtoList;
    }

    @Override
    public List<Long> listUserIdsByBranchId(Long branchId) {
        List<Long> userIdList = new ArrayList<>();
        List<ServiceBranchUser> serviceBranchUsers = listByBranchId(branchId);
        if (serviceBranchUsers != null && serviceBranchUsers.size() > 0) {
            for (ServiceBranchUser serviceBranchUser : serviceBranchUsers) {
                userIdList.add(serviceBranchUser.getUserId());
            }
        }
        return userIdList;
    }

    @Override
    public void addBranchUser(ServiceBranchUserDto serviceBranchUserDto, ReqParam reqParam) {
        if (CollectionUtil.isEmpty(serviceBranchUserDto.getUserIdList())) {
            throw new AppException("人员不能为空！");
        }
        List<Long> dbList = this.listUserIdsByBranchId(serviceBranchUserDto.getBranchId());
        if (CollectionUtil.isNotEmpty(dbList)) {
            // 已存在的列表
            List<Long> existList = dbList.stream().filter(item -> serviceBranchUserDto.getUserIdList().contains(item))
                    .collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(existList)) {
                Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(existList));
                Map<Long, String> userMap = new HashMap<>();
                if (userMapResult != null && userMapResult.getCode() == Result.SUCCESS) {
                    userMap = userMapResult.getData();
                }
                StringBuilder names = new StringBuilder(32);
                for (Long userId : existList) {
                    if (names.length() > 0) {
                        names.append(",");
                    }
                    names.append(StrUtil.trimToEmpty(userMap.get(userId)));
                }
                throw new AppException("人员[" + names.toString() + "]已存在！");
            }
        }
        List<ServiceBranchUser> serviceBranchUserList = new ArrayList<>();
        ServiceBranchUser serviceBranchUser;
        for (Long userId : serviceBranchUserDto.getUserIdList()) {
            serviceBranchUser = new ServiceBranchUser();
            serviceBranchUser.setBranchId(serviceBranchUserDto.getBranchId());
            serviceBranchUser.setUserId(userId);
            serviceBranchUser.setAddTime(DateUtil.date());
            serviceBranchUserList.add(serviceBranchUser);
        }
        this.saveBatch(serviceBranchUserList);
        // 需要删除人员范围权限，后面会重新初始化
        String json = JsonUtil.toJsonString("userIdList", serviceBranchUserDto.getUserIdList(),
                "corpId", reqParam.getCorpId());
        uasFeignService.delUserRightScope(json);
    }

    @Override
    public ListWrapper<ServiceBranchUserDto> query(ServiceBranchUserFilter serviceBranchUserFilter) {
        Page page = new Page(serviceBranchUserFilter.getPageNum(), serviceBranchUserFilter.getPageSize());
        QueryWrapper<ServiceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", serviceBranchUserFilter.getBranchId());
        if (LongUtil.isNotZero(serviceBranchUserFilter.getUserId())) {
            queryWrapper.eq("user_id", serviceBranchUserFilter.getUserId());
        }
        queryWrapper.orderByAsc("add_time", "user_id");
        IPage<ServiceBranchUser> serviceBranchUserIPage = this.page(page, queryWrapper);
        List<ServiceBranchUser> serviceBranchUserList = serviceBranchUserIPage.getRecords();
        List<ServiceBranchUserDto> serviceBranchUserDtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(serviceBranchUserList)) {
            List<Long> userIdList = serviceBranchUserList.stream()
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
            String roleUserJson = JsonUtil.toJsonString("corpId", serviceBranchUserFilter.getCorpId(),
                    "userIdList", userIdList);
            Result<Map<Long, String>> userRoleMapResult = rightFeignService
                    .mapUserIdAndRoleNames(roleUserJson);
            Map<Long, String> userRoleMap = new HashMap<>();
            if (userRoleMapResult != null && userRoleMapResult.getCode() == Result.SUCCESS) {
                userRoleMap = userRoleMapResult.getData();
            }
            ServiceBranchUserDto serviceBranchUserDto;
            for (ServiceBranchUser serviceBranchUser : serviceBranchUserList) {
                serviceBranchUserDto = new ServiceBranchUserDto();
                serviceBranchUserDto.setUserId(serviceBranchUser.getUserId());
                serviceBranchUserDto.setUserName(StrUtil.trimToEmpty(userMap.get(serviceBranchUser.getUserId())));
                serviceBranchUserDto.setPhone(StrUtil.trimToEmpty(userMobileMap.get(serviceBranchUser.getUserId())));
                serviceBranchUserDto.setRoleNames(StrUtil.trimToEmpty(userRoleMap.get(serviceBranchUser.getUserId())));
                serviceBranchUserDto.setAddTime(serviceBranchUser.getAddTime());
                serviceBranchUserDtoList.add(serviceBranchUserDto);
            }
        }
        return ListWrapper.<ServiceBranchUserDto>builder()
                .list(serviceBranchUserDtoList)
                .total(serviceBranchUserIPage.getTotal())
                .build();
    }

    /**
     * 删除网点人员
     *
     * @param userId
     * @param branchId
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/28 09:52
     **/
    @Override
    public void delBranchUser(Long userId, Long branchId, ReqParam reqParam) {
        UpdateWrapper<ServiceBranchUser> serviceBranchUserUpdateWrapper = new UpdateWrapper<>();
        serviceBranchUserUpdateWrapper.eq("branch_id", branchId);
        serviceBranchUserUpdateWrapper.eq("user_id", userId);
        this.remove(serviceBranchUserUpdateWrapper);
        List<Long> userIdList = new ArrayList<>();
        userIdList.add(userId);
        // 需要删除人员范围权限，后面会重新初始化
        String json = JsonUtil.toJsonString("userIdList", userIdList,
                "corpId", reqParam.getCorpId());
        uasFeignService.delUserRightScope(json);
    }

    /**
     * 模糊查询人员
     *
     * @param serviceBranchUserFilter
     * @return
     * @author zgpi
     * @date 2019/12/23 14:51
     **/
    @Override
    public List<CorpUserDto> matchCorpUser(ServiceBranchUserFilter serviceBranchUserFilter) {
        if (LongUtil.isZero(serviceBranchUserFilter.getBranchId())) {
            throw new AppException("网点编号不能为空！");
        }
        if (LongUtil.isZero(serviceBranchUserFilter.getCorpId())) {
            throw new AppException("企业编号不能为空！");
        }
        List<ServiceBranchUser> serviceBranchUserList = this.list(new QueryWrapper<ServiceBranchUser>()
                .eq("branch_id", serviceBranchUserFilter.getBranchId()));
        List<Long> userIdList = serviceBranchUserList.stream().map(e -> e.getUserId())
                .collect(Collectors.toList());
        List<CorpUserDto> corpUserDtoList = new ArrayList<>();
        if (CollectionUtil.isEmpty(userIdList)) {
            return corpUserDtoList;
        }
        serviceBranchUserFilter.setUserIdList(userIdList);
        String json = JsonUtil.toJson(serviceBranchUserFilter);
        Result result = uasFeignService.matchCorpUser(json);
        if (result != null && result.getCode() == Result.SUCCESS) {
            corpUserDtoList = JsonUtil.parseArray(JsonUtil.toJson(result.getData()), CorpUserDto.class);
        }
        return corpUserDtoList;
    }

    @Override
    public List<CorpUserDto> matchAvailable(ServiceBranchUserFilter serviceBranchUserFilter) {
        if (LongUtil.isZero(serviceBranchUserFilter.getBranchId())) {
            throw new AppException("网点编号不能为空");
        }
        QueryWrapper<ServiceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", serviceBranchUserFilter.getBranchId());
        List<ServiceBranchUser> serviceBranchUserList = this.list(queryWrapper);
        List<Long> userIdList = serviceBranchUserList.stream()
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
    public ListWrapper<CorpUserDto> queryAvailable(ServiceBranchUserFilter serviceBranchUserFilter) {
        if (LongUtil.isZero(serviceBranchUserFilter.getBranchId())) {
            throw new AppException("网点编号不能为空");
        }
        QueryWrapper<ServiceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("branch_id", serviceBranchUserFilter.getBranchId());
        List<ServiceBranchUser> serviceBranchUserList = this.list(queryWrapper);
        List<Long> userIdList = serviceBranchUserList.stream()
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

    /**
     * 删除网点人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/2/25 17:19
     */
    @Override
    public void delBranchUserByCorp(Long userId, Long corpId, Long currentUserId, String clientId) {
        // 当前企业的serviceBranch
        List<ServiceBranch> branchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>().eq("service_corp", corpId));
        if (CollectionUtil.isEmpty(branchList)) {
            log.info("企业:{},没有ServiceBranch，无需delBranchUserByCorp操作", corpId);
        } else {
            List<Long> branchIdList = branchList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
            // 用户所属的serviceBranch
            List<ServiceBranchUser> serviceBranchUserList = this.list(new QueryWrapper<ServiceBranchUser>().in("branch_id", branchIdList).eq("user_id", userId));
            if (CollectionUtil.isNotEmpty(serviceBranchUserList)) {
                List<Long> serviceBranchUserIdList = serviceBranchUserList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
                this.remove(new UpdateWrapper<ServiceBranchUser>().in("branch_id", serviceBranchUserIdList).eq("user_id", userId));
                // 增加备份
                List<ServiceBranchUserTrace> serviceBranchUserTraceList = new ArrayList<>();
                Date date = DateUtil.date();
                serviceBranchUserIdList.forEach(branchId -> {
                    ServiceBranchUserTrace serviceBranchUserTrace = new ServiceBranchUserTrace();
                    serviceBranchUserTrace
                            .setBranchId(branchId)
                            .setUserId(userId)
                            .setOperate(2)
                            .setOperator(currentUserId)
                            .setClientId(clientId)
                            .setOperateTime(date);
                    serviceBranchUserTraceList.add(serviceBranchUserTrace);
                });
                serviceBranchUserTraceService.saveBatch(serviceBranchUserTraceList);
            }
        }
    }

    /**
     * 人员编号与服务网点名称(带省份)映射
     *
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2020/3/13 09:46
     */
    @Override
    public Map<Long, String> mapUserIdAndServiceBranchNames(List<Long> userIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(userIdList)) {
            return map;
        }
        List<ServiceBranchUser> serviceBranchUserList = this.list(new QueryWrapper<ServiceBranchUser>()
                .in("user_id", userIdList));
        List<Long> branchIdList = serviceBranchUserList.stream()
                .map(e -> e.getBranchId()).collect(Collectors.toList());
        if (CollectionUtil.isEmpty(branchIdList)) {
            return map;
        }
        Map<Long, List<Long>> userAndBranchListMap = new HashMap<>();
        for (ServiceBranchUser serviceBranchUser : serviceBranchUserList) {
            List<Long> list = new ArrayList<>();
            if (userAndBranchListMap.containsKey(serviceBranchUser.getUserId())) {
                list = userAndBranchListMap.get(serviceBranchUser.getUserId());
            }
            list.add(serviceBranchUser.getBranchId());
            userAndBranchListMap.put(serviceBranchUser.getUserId(), list);
        }
        List<ServiceBranch> serviceBranchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>()
                .in("branch_id", branchIdList));
        List<String> areaCodeList = new ArrayList<>();
        for (ServiceBranch serviceBranch : serviceBranchList) {
            areaCodeList.add(serviceBranch.getProvince());
        }
        Result<Map<String, String>> result = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeList));
        Map<String, String> areaMap = result.getData() == null ? new HashMap<>() : result.getData();
        Map<Long, ServiceBranch> serviceBranchMap = new HashMap<>();
        for (ServiceBranch serviceBranch : serviceBranchList) {
            String provinceName = StrUtil.trimToEmpty(areaMap.get(serviceBranch.getProvince()));
            serviceBranch.setProvince(provinceName);
            serviceBranchMap.put(serviceBranch.getBranchId(), serviceBranch);
        }
        Map<Long, String> userAndBranchNamesMap = new HashMap<>();
        for (Long userId : userAndBranchListMap.keySet()) {
            List<Long> list = userAndBranchListMap.get(userId);
            Set<String> provinces = new LinkedHashSet<>();
            List<String> nameList = new ArrayList<>();
            for (Long branchId : list) {
                ServiceBranch serviceBranch = serviceBranchMap.get(branchId);
                if (serviceBranch != null) {
                    provinces.add(serviceBranch.getProvince());
                    nameList.add(serviceBranch.getBranchName());
                }
            }
            userAndBranchNamesMap.put(userId, StrUtil.join(",", provinces) + " " +
                    StrUtil.join(",", nameList));
        }
        return userAndBranchNamesMap;
    }

    /**
     * 根据条件获得人员列表
     *
     * @param serviceBranchUserFilter
     * @return
     * @author zgpi
     * @date 2020/3/13 10:30
     */
    @Override
    public List<Long> listUserIdByFeign(ServiceBranchUserFilter serviceBranchUserFilter) {
        List<Long> userIdList = new ArrayList<>();
        QueryWrapper<ServiceBranchUser> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isZero(serviceBranchUserFilter.getBranchId())
                && StrUtil.isBlank(serviceBranchUserFilter.getProvince())) {
            return userIdList;
        }
        if (LongUtil.isNotZero(serviceBranchUserFilter.getBranchId())) {
            queryWrapper.eq("branch_id", serviceBranchUserFilter.getBranchId());
        }
        if (StrUtil.isNotBlank(serviceBranchUserFilter.getProvince())) {
            List<ServiceBranch> serviceBranchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>()
                    .eq("province", StrUtil.trimToEmpty(serviceBranchUserFilter.getProvince())));
            List<Long> branchIdList = serviceBranchList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(branchIdList)) {
                queryWrapper.in("branch_id", branchIdList);
            } else {
                return userIdList;
            }
        }
        List<ServiceBranchUser> serviceBranchUserList = this.list(queryWrapper);
        return serviceBranchUserList.stream().map(e -> e.getUserId()).collect(Collectors.toList());
    }

    /**
     * 根据人员获得所在服务网点
     *
     * @param userid
     * @return
     */
    @Override
    public List<Long> listBranchsByUserId(Long userid) {
        QueryWrapper<ServiceBranchUser> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(userid)) {
            queryWrapper.eq("user_id", userid);
        }
        List<ServiceBranchUser> serviceBranchUserList = this.list(queryWrapper);
        return serviceBranchUserList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
    }

    /**
     * 查询某个人员的服务网点信息
     *
     * @param userId
     * @return
     */
    @Override
    public String getBranchNamesByUserId(Long userId) {
        QueryWrapper<ServiceBranchUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<ServiceBranchUser> serviceBranchUserList = this.list(queryWrapper);
        List<Long> branchIdList = serviceBranchUserList.stream()
                .map(e -> e.getBranchId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(branchIdList)) {
            List<ServiceBranch> serviceBranchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>()
                    .in("branch_id", branchIdList));
            Set<String> set = new HashSet<>();
            for (ServiceBranch serviceBranch : serviceBranchList) {
                if (StrUtil.isNotEmpty(serviceBranch.getBranchName())) {
                    set.add(serviceBranch.getBranchName());
                }
            }
            String str = "";
            if (set.size() > 0) {
                str = set.stream().collect(Collectors.joining(","));
            }
            return str;
        }
        return "";
    }

    /**
     * 是否该网点人员
     *
     * @param branchId
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/4/24 14:47
     **/
    @Override
    public boolean ifBranchUser(Long branchId, Long userId) {
        List<ServiceBranchUser> serviceBranchUserList = this.list(new QueryWrapper<ServiceBranchUser>()
                .eq("branch_id", branchId)
                .eq("user_id", userId));
        if (CollectionUtil.isNotEmpty(serviceBranchUserList)) {
            return true;
        }
        return false;
    }

}
