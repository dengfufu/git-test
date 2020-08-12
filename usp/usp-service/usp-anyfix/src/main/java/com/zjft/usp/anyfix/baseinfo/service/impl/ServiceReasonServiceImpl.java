package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.enums.ServiceReasonEnum;
import com.zjft.usp.anyfix.baseinfo.filter.ServiceReasonFilter;
import com.zjft.usp.anyfix.baseinfo.model.ServiceReason;
import com.zjft.usp.anyfix.baseinfo.mapper.ServiceReasonMapper;
import com.zjft.usp.anyfix.baseinfo.service.ServiceReasonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务商原因表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
public class ServiceReasonServiceImpl extends ServiceImpl<ServiceReasonMapper, ServiceReason> implements ServiceReasonService {

    @Override
    public List<ServiceReason> listEnableServiceReason(Long serviceCorp, Integer reasonType) {
        return this.list(new QueryWrapper<ServiceReason>().eq("enabled", EnabledEnum.YES.getCode())
                .eq("reason_type", reasonType));
    }


    @Override
    public List<ServiceReason> selectByCorpAndType(Long customCorp, String type) {
        Assert.notNull(type,"类型不能为空");
        Assert.notNull(customCorp,"serviceCrop 不能为空");
        // 客户撤单类型
        int reasonType = 0;
        if("service-cancel".equals(type)){
            reasonType = ServiceReasonEnum.SERVICE_CANCEL_ORDER.code;
        } else if ("service-revoke".equals(type)){
            reasonType = ServiceReasonEnum.SERVICE_REVOKE_DISTRIBUTION.code;
        } else if ("engineer-refuse".equals(type)){
            reasonType = ServiceReasonEnum.ENGINEER_REFUSE_DISTRIBUTION.code;
        } else if ("engineer-revoke".equals(type)){
            reasonType = ServiceReasonEnum.ENGINEER_REVOKE_DISTRIBUTION.code;
        }
        return this.list(new QueryWrapper<ServiceReason>().eq("enabled",EnabledEnum.YES.getCode())
                .eq("service_corp", customCorp)
                .eq("reason_type",reasonType));
    }

    /**
     * 分页查询服务商原因列表
     *
     * @param serviceReasonFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 14:42
     **/
    @Override
    public ListWrapper<ServiceReason> query(ServiceReasonFilter serviceReasonFilter) {
        QueryWrapper<ServiceReason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("service_corp", serviceReasonFilter.getServiceCorp());
        queryWrapper.eq("reason_type", serviceReasonFilter.getReasonType());
        if(StrUtil.isNotBlank(serviceReasonFilter.getName())) {
            queryWrapper.like("name", StrUtil.trimToEmpty(serviceReasonFilter.getName()));
        }
        if(StrUtil.isNotBlank(serviceReasonFilter.getEnabled())) {
            queryWrapper.eq("enabled", serviceReasonFilter.getEnabled());
        }
        Page page = new Page(serviceReasonFilter.getPageNum(), serviceReasonFilter.getPageSize());
        IPage<ServiceReason> serviceReasonIPage = this.page(page, queryWrapper);
        return ListWrapper.<ServiceReason>builder().list(serviceReasonIPage.getRecords()).total(serviceReasonIPage.getTotal()).build();
    }

    @Override
    public void save(ServiceReason serviceReason, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtils.isEmpty(serviceReason.getName())) {
            throw new AppException("原因名称不能为空");
        }
        serviceReason.setServiceCorp(reqParam.getCorpId());
        QueryWrapper<ServiceReason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",serviceReason.getName());
        queryWrapper.eq("service_corp",serviceReason.getServiceCorp());
        List<ServiceReason> serviceReasons = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(serviceReasons)) {
            throw new AppException("该原因名称已经存在");
        }
        serviceReason.setOperator(userInfo.getUserId());
        serviceReason.setOperateTime(DateUtil.date().toTimestamp());
        this.save(serviceReason);

    }

    @Override
    public void update(ServiceReason serviceReason, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(serviceReason.getName()) ) {
            builder.append("原因名称不能为空");
        }
        if(LongUtil.isZero(serviceReason.getServiceCorp())) {
            builder.append("企业编号不能为空");
        }
        if(serviceReason.getId() == 0) {
            builder.append("原因编号不能为空");
        }
        if(builder.length() > 0 ){
            throw new AppException(builder.toString());
        }
        QueryWrapper<ServiceReason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",serviceReason.getName());
        queryWrapper.eq("service_corp",serviceReason.getServiceCorp());
        queryWrapper.ne("id", serviceReason.getId());
        List<ServiceReason> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该原因名称已经存在");
        }
        serviceReason.setOperateTime(DateUtil.date().toTimestamp());
        serviceReason.setOperator(userInfo.getUserId());
        this.updateById(serviceReason);
    }
}
