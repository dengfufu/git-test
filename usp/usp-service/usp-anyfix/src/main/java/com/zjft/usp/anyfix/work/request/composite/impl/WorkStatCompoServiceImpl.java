package com.zjft.usp.anyfix.work.request.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.service.DataScopeCompoService;
import com.zjft.usp.anyfix.work.request.composite.WorkStatCompoService;
import com.zjft.usp.anyfix.work.request.dto.WorkStatAreaDto;
import com.zjft.usp.anyfix.work.request.dto.WorkStatDto;
import com.zjft.usp.anyfix.work.request.dto.WorkStatusCountDto;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 工单统计实现类
 *
 * @author zgpi
 * @date 2020/2/16 20:24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkStatCompoServiceImpl implements WorkStatCompoService {

    @Autowired
    private DataScopeCompoService dataScopeCompoService;

    @Resource
    private WorkRequestMapper workRequestMapper;

    @Resource
    private UasFeignService uasFeignService;

    /**
     * 按状态统计工单
     *
     * @param workFilter 查询条件
     * @param userInfo   当前用户
     * @param reqParam   公共参数
     * @return
     * @author zgpi
     * @date 2019/10/23 11:20 上午
     **/
    @Override
    public List<WorkStatDto> countWorkStatus(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        this.findUserRight(workFilter, userInfo, reqParam);
        if (LongUtil.isZero(workFilter.getCorpId())) {
            return new ArrayList<>();
        }
        List<WorkStatDto> list = new ArrayList<>();
        List<Map<String, Object>> listMap = workRequestMapper.countWorkStatus(workFilter);
        if (listMap != null && !listMap.isEmpty()) {
            WorkStatDto workStatDto;
            for (Map<String, Object> map : listMap) {
                Integer key = null;
                Long value = null;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("work_status".equals(entry.getKey())) {
                        key = (Integer) entry.getValue();
                    } else if ("work_number".equals(entry.getKey())) {
                        value = (Long) entry.getValue();
                    }
                }
                workStatDto = new WorkStatDto();
                workStatDto.setWorkStatus(key);
                workStatDto.setWorkNumber(value);
                list.add(workStatDto);
            }
        }
        return list;
    }

    /**
     * 按状态统计当前用户的工单
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/16 20:21
     */
    @Override
    public List<WorkStatDto> countUserWorkStatus(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        workFilter.setUserId(userInfo.getUserId());
        workFilter.setCorpId(reqParam.getCorpId());
        this.findUserRight(workFilter, userInfo, reqParam);
        workFilter = this.addRightTypeList(workFilter);

        List<WorkStatDto> list = new ArrayList<>();
        List<Map<String, Object>> listMap = workRequestMapper.countUserWorkStatus(workFilter);
        if (CollectionUtil.isNotEmpty(listMap)) {
            WorkStatDto workStatDto;
            for (Map<String, Object> map : listMap) {
                Integer key = null;
                Long value = null;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("work_status".equals(entry.getKey())) {
                        key = (Integer) entry.getValue();
                    } else if ("work_number".equals(entry.getKey())) {
                        value = (Long) entry.getValue();
                    }
                }
                workStatDto = new WorkStatDto();
                workStatDto.setWorkStatus(key);
                workStatDto.setWorkNumber(value);
                list.add(workStatDto);
            }
        }
        return list;
    }

    /**
     * 统计当前用户的待录入费用工单数
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/5/17 11:56
     **/
    @Override
    public Integer countUserWorkFee(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        workFilter.setUserId(userInfo.getUserId());
        workFilter.setCorpId(reqParam.getCorpId());
        Integer count = workRequestMapper.countUserWorkFee(workFilter);
        return count == null ? 0 : count;
    }

    /**
     * 统计当前用户审核不通过的工单数
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/5/19 10:12
     **/
    @Override
    public Integer countUserReject(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        workFilter.setUserId(userInfo.getUserId());
        workFilter.setCorpId(reqParam.getCorpId());
        Integer count = workRequestMapper.countUserReject(workFilter);
        return count == null ? 0 : count;
    }

    /**
     * 按区域统计工单
     *
     * @param workFilter 查询条件
     * @param userInfo   当前用户
     * @param reqParam   公共参数
     * @return
     * @author zgpi
     * @date 2019/12/10 10:48
     **/
    @Override
    public List<WorkStatAreaDto> countWorkArea(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        this.findUserRight(workFilter, userInfo, reqParam);
        List<WorkStatAreaDto> workStatAreaDtoList = workRequestMapper.countWorkArea(workFilter);
        if (CollectionUtil.isNotEmpty(workStatAreaDtoList)) {
            List<String> provinceCodeList = workStatAreaDtoList.stream()
                    .map(e -> e.getProvinceCode()).collect(Collectors.toList());
            Result<Map<String, String>> areaResult = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(provinceCodeList));
            Map<String, String> codeNameMap = null;
            if (areaResult.getCode() == Result.SUCCESS) {
                codeNameMap = areaResult.getData();
                codeNameMap = codeNameMap == null ? new HashMap<>(0) : codeNameMap;
            }
            for (WorkStatAreaDto statAreaDto : workStatAreaDtoList) {
                statAreaDto.setProvinceName(codeNameMap.get(statAreaDto.getProvinceCode()));
            }
        }
        return workStatAreaDtoList;
    }

    /**
     * 统计待处理工单
     *
     * @param workFilter 查询条件
     * @param userInfo   当前用户
     * @param reqParam   公共参数
     * @return
     * @author zgpi
     * @date 2019/12/10 11:31
     **/
    @Override
    public Map<String, Object> countWorkDeal(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        this.findUserRight(workFilter, userInfo, reqParam);
        List<WorkStatusCountDto> workStatusCountDtoList = workRequestMapper.countWorkDeal(workFilter);
        List<Integer> workStatusList = WorkStatusEnum.getStatusList();
        Map<Integer, WorkStatusCountDto> statusWorkStatDtoMap = new HashMap<>();
        WorkStatusCountDto workStatusCountDto;
        for (Integer workStatus : workStatusList) {
            workStatusCountDto = new WorkStatusCountDto();
            workStatusCountDto.setStatus(workStatus);
            workStatusCountDto.setName(WorkStatusEnum.getNameByCode(workStatus));
            statusWorkStatDtoMap.put(workStatus, workStatusCountDto);
        }

        Map<String, Object> resultMap = new HashMap<>(3);
        // 工单数
        int workCount = 0;
        // 完成工单数
        int finishCount = 0;
        for (WorkStatusCountDto entity : workStatusCountDtoList) {
            entity.setName(WorkStatusEnum.getNameByCode(entity.getStatus()));
            WorkStatusCountDto dto = statusWorkStatDtoMap.get(entity.getStatus());
            if (dto != null) {
                dto.setCount(entity.getCount());
            }
            workCount += entity.getCount();
            if (WorkStatusEnum.CLOSED.getCode() == entity.getStatus()
                    || WorkStatusEnum.TO_EVALUATE.getCode() == entity.getStatus()) {
                finishCount += entity.getCount();
            }
        }
        resultMap.put("new", workCount);
        resultMap.put("finish", finishCount);
        List<WorkStatusCountDto> dtoList = statusWorkStatDtoMap.values()
                .stream().collect(Collectors.toList());
        resultMap.put("list", dtoList);
        return resultMap;
    }

    /**
     * 获得用户权限
     *
     * @param workFilter 查询条件
     * @param userInfo   当前用户
     * @param reqParam   公共参数
     * @return 空
     * @author zgpi
     * @date 2019/12/10 09:12
     **/
    private void findUserRight(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam) {
        Long userId = userInfo.getUserId();
        Long corpId = reqParam.getCorpId();
        workFilter.setUserId(userId);
        workFilter.setCorpId(reqParam.getCorpId());
        Long rightId = RightConstants.WORK_QUERY;
        // 范围权限列表
        List<RightScopeDto> rightScopeDtoList = dataScopeCompoService.listUserRightScope(corpId, userId, rightId);
        workFilter.setRightScopeList(rightScopeDtoList);
    }

    /**
     * 添加权限类型列表
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/2/16 17:18
     */
    private WorkFilter addRightTypeList(WorkFilter workFilter) {
        Result<List<Long>> rightIdListResult = uasFeignService.listUserRightId(workFilter.getUserId(), workFilter.getCorpId());
        List<Long> rightIdList = new ArrayList<>();
        if (rightIdListResult != null && rightIdListResult.getCode() == Result.SUCCESS) {
            rightIdList = rightIdListResult.getData();
            rightIdList = rightIdList == null ? new ArrayList<>() : rightIdList;
        }
        List<Integer> rightTypeList = new ArrayList<>();
        // 有提单权限
        if (rightIdList.contains(RightConstants.WORK_DISPATCH)) {
            rightTypeList.add(1);
        }
        // 有分配权限
        if (rightIdList.contains(RightConstants.WORK_HANDLE)) {
            rightTypeList.add(2);
        }
        // 有派单权限
        if (rightIdList.contains(RightConstants.WORK_ASSIGN)) {
            rightTypeList.add(3);
        }
        // 有接单权限
        if (rightIdList.contains(RightConstants.WORK_CLAIM)) {
            rightTypeList.add(4);
        }
        // 有签到权限
        if (rightIdList.contains(RightConstants.WORK_SIGN)) {
            rightTypeList.add(5);
        }
        // 有远程服务权限
        if (rightIdList.contains(RightConstants.WORK_TO_SERVICE)) {
            rightTypeList.add(6);
        }
        // 有现场服务权限
        if (rightIdList.contains(RightConstants.WORK_IN_SERVICE)) {
            rightTypeList.add(7);
        }
        workFilter.setRightTypeList(rightTypeList);
        return workFilter;
    }
}
