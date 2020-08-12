package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.filter.ServiceEvaluateFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluate;
import com.zjft.usp.anyfix.baseinfo.mapper.ServiceEvaluateMapper;
import com.zjft.usp.anyfix.baseinfo.service.ServiceEvaluateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务评价指标表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
public class ServiceEvaluateServiceImpl extends ServiceImpl<ServiceEvaluateMapper, ServiceEvaluate> implements ServiceEvaluateService {

    @Override
    public List<ServiceEvaluate> listServiceEvaluate(Long serviceCrop) {
        Assert.notNull(serviceCrop,"serviceCrop 不能为空");
        return this.list(new QueryWrapper<ServiceEvaluate>().eq("enabled", EnabledEnum.YES.getCode()).eq("service_corp", serviceCrop));
    }

    @Override
    public Map<Integer, ServiceEvaluate> mapIdAndEvaluate(Long serviceCorp) {
        Map<Integer, ServiceEvaluate> map = new HashMap<>();
        if(LongUtil.isZero(serviceCorp)){
            return map;
        }
        List<ServiceEvaluate> list = this.listServiceEvaluate(serviceCorp);
        if(list != null && list.size() > 0){
            for(ServiceEvaluate serviceEvaluate: list){
                map.put(serviceEvaluate.getId(), serviceEvaluate);
            }
        }
        return map;
    }

    /**
     * 分页查询服务评价
     *
     * @param serviceEvaluateFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 15:30
     **/
    @Override
    public ListWrapper<ServiceEvaluate> query(ServiceEvaluateFilter serviceEvaluateFilter) {
        QueryWrapper<ServiceEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", serviceEvaluateFilter.getServiceCorp());
        if(StrUtil.isNotBlank(serviceEvaluateFilter.getName())) {
            queryWrapper.like("name", StrUtil.trimToEmpty(serviceEvaluateFilter.getName()));
        }
        if(StrUtil.isNotBlank(serviceEvaluateFilter.getEnabled())) {
            queryWrapper.eq("enabled", serviceEvaluateFilter.getEnabled());
        }
        Page page = new Page(serviceEvaluateFilter.getPageNum(), serviceEvaluateFilter.getPageSize());
        IPage<ServiceEvaluate> serviceEvaluateIPage = this.page(page, queryWrapper);
        return ListWrapper.<ServiceEvaluate>builder().list(serviceEvaluateIPage.getRecords()).total(serviceEvaluateIPage.getTotal()).build();
    }

    @Override
    public void save(ServiceEvaluate serviceEvaluate, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtils.isEmpty(serviceEvaluate.getName())) {
            throw new AppException("服务评价指标名称不能为空");
        }
        serviceEvaluate.setServiceCorp(reqParam.getCorpId());
        QueryWrapper<ServiceEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",serviceEvaluate.getName());
        queryWrapper.eq("service_corp",serviceEvaluate.getServiceCorp());
        List<ServiceEvaluate> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该服务评价指标名称已经存在");
        }
        serviceEvaluate.setOperator(userInfo.getUserId());
        serviceEvaluate.setOperateTime(DateUtil.date().toTimestamp());
        this.save(serviceEvaluate);
    }

    @Override
    public void update(ServiceEvaluate serviceEvaluate, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(serviceEvaluate.getName()) ) {
            builder.append("服务评价指标名称不能为空");
        }
        if(LongUtil.isZero(serviceEvaluate.getServiceCorp())) {
            builder.append("企业编号不能为空");
        }
        if(serviceEvaluate.getId() == 0) {
            builder.append("服务评价指标名称不能为空");
        }
        if(builder.length() > 0 ){
            throw new AppException(builder.toString());
        }
        QueryWrapper<ServiceEvaluate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",serviceEvaluate.getName());
        queryWrapper.eq("service_corp",serviceEvaluate.getServiceCorp());
        queryWrapper.ne("id", serviceEvaluate.getId());
        List<ServiceEvaluate> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该服务评价指标名称已经存在");
        }
        serviceEvaluate.setOperateTime(DateUtil.date().toTimestamp());
        serviceEvaluate.setOperator(userInfo.getUserId());
        this.updateById(serviceEvaluate);

    }
}
