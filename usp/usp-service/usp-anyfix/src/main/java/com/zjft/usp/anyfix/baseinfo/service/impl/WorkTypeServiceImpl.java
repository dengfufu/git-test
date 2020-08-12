package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.dto.WorkTypeDto;
import com.zjft.usp.anyfix.baseinfo.enums.WorkSysTypeEnum;
import com.zjft.usp.anyfix.baseinfo.filter.WorkTypeFilter;
import com.zjft.usp.anyfix.baseinfo.mapper.WorkTypeMapper;
import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
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
 * <p>
 * 工单类型表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkTypeServiceImpl extends ServiceImpl<WorkTypeMapper, WorkType> implements WorkTypeService {

    @Resource
    private UasFeignService uasFeignService;

    @Autowired
    private DemanderServiceService demanderServiceService;

    /**
     * 企业工单类型列表
     *
     * @param workTypeFilter
     * @return
     * @author zgpi
     * @date 2019/11/12 16:44
     **/
    @Override
    public List<WorkType> listWorkType(WorkTypeFilter workTypeFilter) {
        QueryWrapper<WorkType> queryWrapper = new QueryWrapper<>();
        if (StrUtil.isNotBlank(workTypeFilter.getEnabled())) {
            queryWrapper.eq("enabled", StrUtil.trimToEmpty(workTypeFilter.getEnabled()).toUpperCase());
        }
        queryWrapper.and(wrapper -> wrapper.eq("demander_corp", workTypeFilter.getDemanderCorp()).or()
                .eq("demander_corp", 0));
        queryWrapper.orderByAsc("demander_corp").orderByAsc("id");
        return this.list(queryWrapper);
    }

    @Override
    public Map<Integer, String> mapWorkType() {
        Map<Integer, String> map = new HashMap<>();
        List<WorkType> list = this.list();
        if (list != null) {
            for (WorkType workType : list) {
                map.put(workType.getId(), workType.getName());
            }
        }
        return map;
    }

    /**
     * 工单类型映射
     *
     * @param demanderCorp
     * @return
     * @author zgpi
     * @date 2019/10/12 3:43 下午
     **/
    @Override
    public Map<Integer, String> mapWorkType(Long demanderCorp) {
        Map<Integer, String> map = new HashMap<>();
        if (demanderCorp == null || demanderCorp == 0L) {
            return map;
        }
        List<WorkType> list = this.list(new QueryWrapper<WorkType>().eq("demander_corp", demanderCorp)
                .or().eq("demander_corp", 0));
        if (list != null) {
            for (WorkType workType : list) {
                map.put(workType.getId(), workType.getName());
            }
        }
        return map;
    }

    /**
     * 客户的工单类型映射
     *
     * @param demanderCorp
     * @return
     * @author zgpi
     * @date 2019/10/29 5:22 下午
     **/
    @Override
    public Map<Integer, WorkType> mapWorkTypeObj(Long demanderCorp) {
        Map<Integer, WorkType> map = new HashMap<>();
        if (demanderCorp == null || demanderCorp == 0L) {
            return map;
        }
        List<WorkType> list = this.list(new QueryWrapper<WorkType>().eq("demander_corp", demanderCorp)
                .or().eq("demander_corp", 0));
        if (list != null) {
            for (WorkType workType : list) {
                map.put(workType.getId(), workType);
            }
        }
        return map;
    }

    /**
     * 根据客户编号列表获取工单类型映射
     *
     * @param corpIdList
     * @return
     */
    @Override
    public Map<Integer, String> mapWorkTypeByCorpIdList(List<Long> corpIdList) {
        Map<Integer, String> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() <= 0) {
            return map;
        }
        List<WorkType> list = this.list(new QueryWrapper<WorkType>().in("demander_corp", corpIdList)
                .or().eq("demander_corp", 0));
        if (list != null && list.size() > 0) {
            for (WorkType workType : list) {
                map.put(workType.getId(), workType.getName());
            }
        }
        return map;
    }

    @Override
    public ListWrapper<WorkTypeDto> query(WorkTypeFilter workTypeFilter) {
        QueryWrapper<WorkType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("demander_corp", 0);
        if(LongUtil.isZero(workTypeFilter.getDemanderCorp())) {
            List<Long> demanderCorpList =  demanderServiceService.listDemanderCorpId(workTypeFilter.getCorpId());
            if(CollectionUtil.isNotEmpty(demanderCorpList)) {
                queryWrapper.or().in("demander_corp",demanderCorpList);
                queryWrapper.orderByAsc("demander_corp");
            } else {
                return new ListWrapper<>();
            }
        } else {
            queryWrapper.or().eq("demander_corp",workTypeFilter.getDemanderCorp());
        }
        if (StrUtil.isNotBlank(workTypeFilter.getName())) {
            queryWrapper.like("name", StrUtil.trimToEmpty(workTypeFilter.getName()));
        }
        if (workTypeFilter.getSysType() != null && workTypeFilter.getSysType() > 0) {
            queryWrapper.eq("sys_type", workTypeFilter.getSysType());
        }
        if (StrUtil.isNotBlank(workTypeFilter.getEnabled())) {
            queryWrapper.eq("enabled", workTypeFilter.getEnabled());
        }
        queryWrapper.orderByAsc("id");
        Page page = new Page(workTypeFilter.getPageNum(), workTypeFilter.getPageSize());
        IPage<WorkType> workTypeIPage = this.page(page, queryWrapper);
        List<WorkTypeDto> dtoList = new ArrayList<>();
        List<WorkType> workTypeList = workTypeIPage.getRecords();
        if ( CollectionUtil.isNotEmpty(workTypeList)) {
            List<Long> corpIdList = workTypeList.stream().map(e -> e.getDemanderCorp()).distinct().collect(Collectors.toList());
            Result corpResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap;
            if(corpResult !=null && corpResult.getCode() == Result.SUCCESS) {
                corpMap = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), Map.class);
            } else {
                corpMap = new HashMap<>();
            }
            for (WorkType workType: workTypeIPage.getRecords()) {
                WorkTypeDto workTypeDto = new WorkTypeDto();
                BeanUtils.copyProperties(workType, workTypeDto);
                workTypeDto.setDemanderCorpName(corpMap.get(workType.getDemanderCorp()));
                workTypeDto.setSysTypeName(WorkSysTypeEnum.getNameByCode(workType.getSysType()));
                dtoList.add(workTypeDto);
            }
        }
        return ListWrapper.<WorkTypeDto>builder().list(dtoList).total(workTypeIPage.getTotal()).build();
    }

    @Override
    public void save(WorkType workType, UserInfo userInfo, ReqParam reqParam) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(workType.getName())) {
            builder.append("工单类型名称不能为空");
        }
        if (IntUtil.isZero(workType.getSysType())) {
            builder.append("系统类型不能为空");
        }
        if (builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        // 委托商为空时，设置为当前企业
        if (LongUtil.isZero(workType.getDemanderCorp())) {
            workType.setDemanderCorp(reqParam.getCorpId());
        }
        QueryWrapper<WorkType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", workType.getName());

        queryWrapper.and(wrapper -> wrapper.like("demander_corp", workType.getDemanderCorp())
                .or().eq("demander_corp", 0));

        queryWrapper.eq("sys_type", workType.getSysType());
        List<WorkType> list = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该工单类型名称已经存在");
        }
        workType.setOperator(userInfo.getUserId());
        workType.setOperateTime(DateUtil.date().toTimestamp());
        this.save(workType);
    }

    @Override
    public void update(WorkType workType, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(workType.getName())) {
            builder.append("工单类型名称不能为空");
        }
        if (workType.getSysType() == 0) {
            builder.append("系统类型不能为空");
        }
        if (workType.getDemanderCorp() == 0) {
            builder.append("企业编号不能为空");
        }
        if (workType.getId() == 0) {
            builder.append("工单类型编号不能为空");
        }
        if (builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        QueryWrapper<WorkType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", workType.getName());
        queryWrapper.and(wrapper -> wrapper.like("demander_corp", workType.getDemanderCorp())
                .or().eq("demander_corp", 0));
        queryWrapper.eq("sys_type", workType.getSysType());
        queryWrapper.ne("id", workType.getId());
        List<WorkType> list = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该工单类型名称已经存在");
        }
        workType.setOperateTime(DateUtil.date().toTimestamp());
        workType.setOperator(userInfo.getUserId());
        this.updateById(workType);
    }
}
