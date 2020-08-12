package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeImplementDto;
import com.zjft.usp.anyfix.work.fee.enums.ImplementTypeEnum;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeImplementFilter;
import com.zjft.usp.anyfix.work.fee.mapper.WorkFeeImplementDefineMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeImplementDefine;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeImplementDefineService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 工单支出费用定义 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-04-17
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkFeeImplementDefineServiceImpl extends ServiceImpl<WorkFeeImplementDefineMapper, WorkFeeImplementDefine> implements WorkFeeImplementDefineService {

    @Autowired
    private WorkFeeDetailService workFeeDetailService;
    @Autowired
    private WorkDealService workDealService;
    @Resource
    private UasFeignService uasFeignService;

    /**
     * 分页查询
     *
     * @param workFeeImplementFilter
     * @param reqParam
     * @return
     */
    @Override
    public ListWrapper<WorkFeeImplementDto> query(WorkFeeImplementFilter workFeeImplementFilter, ReqParam reqParam) {
        if (LongUtil.isZero(workFeeImplementFilter.getServiceCorp())) {
            // 服务商默认为当前企业
            workFeeImplementFilter.setServiceCorp(reqParam.getCorpId());
        }
        ListWrapper<WorkFeeImplementDto> listWrapper = new ListWrapper<>();
        Page page = new Page(workFeeImplementFilter.getPageNum(), workFeeImplementFilter.getPageSize());
        List<WorkFeeImplementDto> dtoList = this.baseMapper.pageByFilter(page, workFeeImplementFilter);
        if (CollectionUtil.isNotEmpty(dtoList)) {
            // 获取企业名称映射
            List<Long> corpIdList = new ArrayList<>();
            for (WorkFeeImplementDto workFeeImplementDto : dtoList) {
                corpIdList.add(workFeeImplementDto.getDemanderCorp());
                corpIdList.add(workFeeImplementDto.getServiceCorp());
            }
            Result<Map<Long, String>> corpResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = corpResult == null ? new HashMap<>() : corpResult.getData();
            dtoList.forEach(workFeeImplementDto -> {
                workFeeImplementDto.setServiceCorpName(corpMap.get(workFeeImplementDto.getServiceCorp()));
                workFeeImplementDto.setDemanderCorpName(corpMap.get(workFeeImplementDto.getDemanderCorp()));
                workFeeImplementDto.setImplementTypeName(ImplementTypeEnum.lookup(workFeeImplementDto.getImplementType()));
            });
        }
        listWrapper.setList(dtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 添加
     *
     * @param workFeeImplementDto
     * @param userInfo
     * @param reqParam
     */
    @Override
    public void add(WorkFeeImplementDto workFeeImplementDto, UserInfo userInfo, ReqParam reqParam) {
        if (workFeeImplementDto == null) {
            throw new AppException("获取参数有误，请重试");
        }
        if (StrUtil.isBlank(workFeeImplementDto.getImplementName())) {
            throw new AppException("请填写实施发生费用名称");
        }
        if (IntUtil.isZero(workFeeImplementDto.getImplementType())) {
            throw new AppException("请选择费用类别");
        }
        WorkFeeImplementDefine workFeeImplementDefine = new WorkFeeImplementDefine();
        // 服务商设置为当前企业
        workFeeImplementDefine.setServiceCorp(reqParam.getCorpId());
        workFeeImplementDefine.setDemanderCorp(workFeeImplementDto.getDemanderCorp());
        workFeeImplementDefine.setImplementName(workFeeImplementDto.getImplementName());
        workFeeImplementDefine.setImplementType(workFeeImplementDto.getImplementType());
        // 是否有效默认是
        workFeeImplementDefine.setEnabled(EnabledEnum.YES.getCode().equalsIgnoreCase(workFeeImplementDto.getEnabled()) ?
                EnabledEnum.YES.getCode() : EnabledEnum.NO.getCode());
        workFeeImplementDefine.setNote(workFeeImplementDto.getNote());
        workFeeImplementDefine.setImplementId(KeyUtil.getId());
        workFeeImplementDefine.setOperator(userInfo.getUserId());
        workFeeImplementDefine.setOperateTime(DateTime.now());
        this.save(workFeeImplementDefine);
    }

    /**
     * 更新
     *
     * @param workFeeImplementDto
     * @param userInfo
     */
    @Override
    public void update(WorkFeeImplementDto workFeeImplementDto, UserInfo userInfo) {
        if (workFeeImplementDto == null || LongUtil.isZero(workFeeImplementDto.getImplementId())) {
            throw new AppException("工单支出费用定义编号不能为空");
        }
        WorkFeeImplementDefine workFeeImplementDefineOld = this.getById(workFeeImplementDto.getImplementId());
        if (workFeeImplementDefineOld == null) {
            throw new AppException("需要更新的工单支出费用定义不存在，请检查");
        }
        if (StrUtil.isBlank(workFeeImplementDto.getImplementName())) {
            throw new AppException("请填写费用名称");
        }
        if (IntUtil.isZero(workFeeImplementDto.getImplementType())) {
            throw new AppException("请选择费用类别");
        }
        List<WorkFeeDetail> workFeeDetails = this.workFeeDetailService.listByImplementId(workFeeImplementDto.getImplementId());
        // 如果该工单支出费用已经被工单引用，则不能修改费用名称和委托商
        if (CollectionUtil.isNotEmpty(workFeeDetails)) {
            if (StrUtil.isNotBlank(workFeeImplementDto.getImplementName()) &&
                    !workFeeImplementDto.getDemanderCorp().equals(workFeeImplementDefineOld.getDemanderCorp())) {
                throw new AppException("该工单支出费用已被工单引用，不能修改委托商，请刷新页面");
            }
        }
        BeanUtils.copyProperties(workFeeImplementDto, workFeeImplementDefineOld);
        workFeeImplementDefineOld.setOperator(userInfo.getUserId());
        workFeeImplementDefineOld.setOperateTime(DateTime.now());
        this.updateById(workFeeImplementDefineOld);
    }

    /**
     * 根据id获取详情
     *
     * @param implementId
     * @return
     */
    @Override
    public WorkFeeImplementDto getDtoById(Long implementId) {
        if (LongUtil.isZero(implementId)) {
            throw new AppException("工单支出费用编号不能为空");
        }
        WorkFeeImplementDto workFeeImplementDto = new WorkFeeImplementDto();
        WorkFeeImplementDefine workFeeImplementDefine = this.getById(implementId);
        if (workFeeImplementDefine == null) {
            throw new AppException("查询的工单支出费用不存在，请检查");
        }
        BeanUtils.copyProperties(workFeeImplementDefine, workFeeImplementDto);
        List<Long> corpIdList = new ArrayList<>();
        corpIdList.add(workFeeImplementDefine.getDemanderCorp());
        corpIdList.add(workFeeImplementDefine.getServiceCorp());
        Result<Map<Long, String>> mapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
        Map<Long, String> corpMap = new HashMap<>();
        if (mapResult != null && mapResult.getCode() == 0) {
            corpMap = mapResult.getData() == null ? new HashMap<>() : mapResult.getData();
        }
        workFeeImplementDto.setDemanderCorpName(corpMap.get(workFeeImplementDto.getDemanderCorp()));
        workFeeImplementDto.setServiceCorpName(corpMap.get(workFeeImplementDto.getServiceCorp()));
        workFeeImplementDto.setImplementTypeName(ImplementTypeEnum.lookup(workFeeImplementDto.getImplementType()));
        // 查询是否被工单引用
        List<WorkFeeDetail> workFeeDetailList = this.workFeeDetailService.listByImplementId(implementId);
        workFeeImplementDto.setUsed(CollectionUtil.isNotEmpty(workFeeDetailList));
        return workFeeImplementDto;
    }

    /**
     * 根据工单编号查询工单支出费用定义列表
     *
     * @param workDto
     * @return
     */
    @Override
    public List<WorkFeeImplementDto> listDtoByWork(WorkDto workDto) {
        if (workDto == null || LongUtil.isZero(workDto.getWorkId())) {
            return null;
        }
        // 需要服务商与委托商才能查询
        if (LongUtil.isZero(workDto.getDemanderCorp()) || LongUtil.isZero(workDto.getServiceCorp())) {
            WorkDeal workDeal = workDealService.getById(workDto.getWorkId());
            if (workDeal == null) {
                throw new AppException("工单不存在，请检查");
            }
            workDto.setServiceCorp(workDeal.getServiceCorp());
            workDto.setDemanderCorp(workDeal.getDemanderCorp());
        }
        QueryWrapper<WorkFeeImplementDefine> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("demander_corp", workDto.getDemanderCorp())
                .eq("service_corp", workDto.getServiceCorp());
        queryWrapper.eq("enabled", EnabledEnum.YES.getCode());
        List<WorkFeeImplementDefine> list = this.list(queryWrapper);
        List<WorkFeeDetailDto> workFeeImplementDetailList = this.workFeeDetailService.listImplementByWorkId(workDto.getWorkId());
        List<WorkFeeImplementDto> dtoList = new ArrayList<>();
        Set<Long> implementIdSet = new HashSet<>();
        // 先增加费用明细
        if (CollectionUtil.isNotEmpty(workFeeImplementDetailList)) {
            workFeeImplementDetailList.forEach(workFeeDetailDto -> {
                WorkFeeImplementDto dto = new WorkFeeImplementDto();
                dto.setImplementId(workFeeDetailDto.getFeeId());
                dto.setImplementName(workFeeDetailDto.getImplementName());
                dto.setAmount(workFeeDetailDto.getAmount());
                dto.setNote(workFeeDetailDto.getNote());
                dtoList.add(dto);
                implementIdSet.add(workFeeDetailDto.getImplementId());
            });
        }
        // 再增加费用明细里不包含的实施费用定义
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach((workFeeImplementDefine -> {
                if (!implementIdSet.contains(workFeeImplementDefine.getImplementId())) {
                    WorkFeeImplementDto dto = new WorkFeeImplementDto();
                    BeanUtils.copyProperties(workFeeImplementDefine, dto);
                    dto.setAmount(BigDecimal.ZERO);
                    dtoList.add(dto);
                    implementIdSet.add(workFeeImplementDefine.getImplementId());
                }
            }));
        }
        return dtoList;
    }

    /**
     * 根据服务商企业编号获取费用编号和名称的映射
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public Map<Long, String> mapIdAndNameByCorp(Long serviceCorp) {
        Map<Long, String> map = new HashMap<>();
        if (LongUtil.isZero(serviceCorp)) {
            return map;
        }
        List<WorkFeeImplementDefine> list = this.list(new QueryWrapper<WorkFeeImplementDefine>().eq("service_corp", serviceCorp));
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(workFeeImplementDefine -> {
                map.put(workFeeImplementDefine.getImplementId(), workFeeImplementDefine.getImplementName());
            });
        }
        return map;
    }

}
