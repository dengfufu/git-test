package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.filter.RemoteWayFilter;
import com.zjft.usp.anyfix.baseinfo.model.RemoteWay;
import com.zjft.usp.anyfix.baseinfo.mapper.RemoteWayMapper;
import com.zjft.usp.anyfix.baseinfo.service.RemoteWayService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 远程处理方式表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
public class RemoteWayServiceImpl extends ServiceImpl<RemoteWayMapper, RemoteWay> implements RemoteWayService {
    @Override
    public List<RemoteWay> selectByServiceCrop(Long serviceCrop) {
        Assert.notNull(serviceCrop,"serviceCrop 不能为空");
        return this.list(new QueryWrapper<RemoteWay>().eq("enabled", EnabledEnum.YES.getCode()).eq("service_corp", serviceCrop));
    }

    /**
     * 分页查询远程处理方式
     *
     * @param remoteWayFilter
     * @return
     * @author zgpi
     * @date 2019/11/16 15:11
     **/
    @Override
    public ListWrapper<RemoteWay> query(RemoteWayFilter remoteWayFilter) {
        QueryWrapper<RemoteWay> queryWrapper = new QueryWrapper<>();
        BigInteger Zijin_ServiceCorp = new BigInteger("1229327791726825475");
        //queryWrapper.and(wrapper -> wrapper.eq("service_corp", remoteWayFilter.getServiceCorp()));
        queryWrapper.and(wrapper -> wrapper.eq("service_corp", Zijin_ServiceCorp));
        if(StrUtil.isNotBlank(remoteWayFilter.getName())) {
            queryWrapper.like("name", StrUtil.trimToEmpty(remoteWayFilter.getName()));
        }
        if(StrUtil.isNotBlank(remoteWayFilter.getEnabled())) {
            queryWrapper.eq("enabled", remoteWayFilter.getEnabled());
        }
        Page page = new Page(remoteWayFilter.getPageNum(), remoteWayFilter.getPageSize());
        IPage<RemoteWay> remoteWayIPage = this.page(page, queryWrapper);
        return ListWrapper.<RemoteWay>builder().list(remoteWayIPage.getRecords()).total(remoteWayIPage.getTotal()).build();
    }

    @Override
    public void save(RemoteWay remoteWay, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtils.isEmpty(remoteWay.getName())) {
            throw new AppException("远程处理方式名称不能为空");
        }
        remoteWay.setServiceCorp(reqParam.getCorpId());
        QueryWrapper<RemoteWay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",remoteWay.getName());
        queryWrapper.eq("service_corp",remoteWay.getServiceCorp());
        List<RemoteWay> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该远程处理方式已经存在");
        }
        remoteWay.setOperator(userInfo.getUserId());
        remoteWay.setOperateTime(DateUtil.date().toTimestamp());
        this.save(remoteWay);
    }

    @Override
    public void update(RemoteWay remoteWay, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(remoteWay.getName())) {
            builder.append("远程处理方式名称不能为空");
        }
        if(LongUtil.isZero(remoteWay.getServiceCorp())) {
            builder.append("企业编号不能为空");
        }
        if( remoteWay.getId() == 0){
            builder.append("远程处理方式编号不能为空");
        }
        if(builder.length() > 0 ){
            throw new AppException(builder.toString());
        }
        QueryWrapper<RemoteWay> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",remoteWay.getName());
        queryWrapper.eq("service_corp",remoteWay.getServiceCorp());
        queryWrapper.ne("id", remoteWay.getId());
        List<RemoteWay> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该远程处理方式已经存在");
        }
        remoteWay.setOperateTime(DateUtil.date().toTimestamp());
        remoteWay.setOperator(userInfo.getUserId());
        this.updateById(remoteWay);
    }
}
