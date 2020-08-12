package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.jiguang.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.dto.ServiceItemDto;
import com.zjft.usp.anyfix.baseinfo.filter.ServiceItemFilter;
import com.zjft.usp.anyfix.baseinfo.mapper.ServiceItemMapper;
import com.zjft.usp.anyfix.baseinfo.model.ServiceItem;
import com.zjft.usp.anyfix.baseinfo.service.ServiceItemService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 服务项目表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
public class ServiceItemServiceImpl extends ServiceImpl<ServiceItemMapper, ServiceItem> implements ServiceItemService {

    @Resource
    private UasFeignService uasFeignService;

    /**
     * 根据过滤器查询服务项目列表
     *
     * @param serviceItemFilter
     * @param serviceCorp
     * @return
     */
    @Override
    public List<ServiceItem> listServiceItemByFilter(ServiceItemFilter serviceItemFilter, Long serviceCorp) {
        QueryWrapper<ServiceItem> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isZero(serviceItemFilter.getServiceCorp())) {
            serviceItemFilter.setServiceCorp(serviceCorp);
        }
        queryWrapper.eq("service_corp", serviceItemFilter.getServiceCorp());
        if (LongUtil.isNotZero(serviceItemFilter.getDemanderCorp())) {
            queryWrapper.eq("demander_corp", serviceItemFilter.getDemanderCorp());
        }
        return this.list(queryWrapper);

    }

    /**
     * 分页查询服务项目列表
     *
     * @param serviceItemFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 09:58
     **/
    @Override
    public ListWrapper<ServiceItemDto> query(ServiceItemFilter serviceItemFilter) {
        QueryWrapper<ServiceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", serviceItemFilter.getServiceCorp());
        if(StrUtil.isNotBlank(serviceItemFilter.getName())) {
            queryWrapper.like("name", StrUtil.trimToEmpty(serviceItemFilter.getName()));
        }
        if (LongUtil.isNotZero(serviceItemFilter.getDemanderCorp())) {
            queryWrapper.eq("demander_corp", serviceItemFilter.getDemanderCorp());
        }
        if(StrUtil.isNotBlank(serviceItemFilter.getEnabled())) {
            queryWrapper.eq("enabled", serviceItemFilter.getEnabled());
        }
        Page page = new Page(serviceItemFilter.getPageNum(), serviceItemFilter.getPageSize());
        IPage<ServiceItem> serviceItemIPage = this.page(page, queryWrapper);
        List<ServiceItem> list = serviceItemIPage.getRecords();
        List<ServiceItemDto> dtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> corpIdSet = new HashSet<>();
            list.forEach(serviceItem -> {
                corpIdSet.add(serviceItem.getDemanderCorp());
            });
            List<Long> corpIdList = new ArrayList<>(corpIdSet);
            Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = new HashMap<>();
            if (corpMapResult != null && Result.SUCCESS == corpMapResult.getCode().intValue()) {
                corpMap = corpMapResult.getData() == null ? new HashMap<>() : corpMapResult.getData();
            }
            for (ServiceItem serviceItem : list) {
                ServiceItemDto serviceItemDto = new ServiceItemDto();
                BeanUtils.copyProperties(serviceItem, serviceItemDto);
                serviceItemDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(serviceItemDto.getDemanderCorp())));
                dtoList.add(serviceItemDto);
            }
        }
        return ListWrapper.<ServiceItemDto>builder().list(dtoList).total(serviceItemIPage.getTotal()).build();
    }

    /**
     * 获取id和name的映射
     *
     * @author canlei
     * @date 2020/01/08
     * @param serviceCorp
     * @return
     */
    @Override
    public Map<Integer, String> mapIdAndNameByCorp(Long serviceCorp) {
        Map<Integer, String> map = new HashMap<>();
        if (LongUtil.isZero(serviceCorp)) {
            return map;
        }
        QueryWrapper<ServiceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", serviceCorp);
        List<ServiceItem> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(serviceItem -> {
                map.put(serviceItem.getId(), serviceItem.getName());
            });
        }
        return map;
    }

    @Override
    public void save(ServiceItem serviceItem, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtils.isEmpty(serviceItem.getName())) {
            throw new AppException("服务项目名称不能为空");
        }
        if (LongUtil.isZero(serviceItem.getDemanderCorp())) {
            throw new AppException("委托商不能为空");
        }
        serviceItem.setServiceCorp(reqParam.getCorpId());
        QueryWrapper<ServiceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",serviceItem.getName());
        queryWrapper.eq("service_corp",serviceItem.getServiceCorp());
        queryWrapper.eq("demander_corp", serviceItem.getDemanderCorp());
        List<ServiceItem> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该委托商该服务项目名称已经存在");
        }
        serviceItem.setOperator(userInfo.getUserId());
        serviceItem.setOperateTime(DateUtil.date().toTimestamp());
        this.save(serviceItem);

    }

    @Override
    public void update(ServiceItem serviceItem, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(serviceItem.getName()) ) {
            builder.append("服务项目名称不能为空");
        }
        if (LongUtil.isZero(serviceItem.getDemanderCorp())) {
            throw new AppException("委托商不能为空");
        }
        if(LongUtil.isZero(serviceItem.getServiceCorp())) {
            builder.append("企业编号不能为空");
        }
        if(IntUtil.isZero(serviceItem.getId())) {
            builder.append("服务项目编号不能为空");
        }
        if(builder.length() > 0 ){
            throw new AppException(builder.toString());
        }
        QueryWrapper<ServiceItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",serviceItem.getName());
        queryWrapper.eq("service_corp",serviceItem.getServiceCorp());
        queryWrapper.eq("demander_corp", serviceItem.getDemanderCorp());
        queryWrapper.ne("id", serviceItem.getId());
        List<ServiceItem> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该委托商该服务项目名称已经存在");
        }
        serviceItem.setOperateTime(DateUtil.date().toTimestamp());
        serviceItem.setOperator(userInfo.getUserId());
        this.updateById(serviceItem);
    }


}
