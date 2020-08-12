package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.dto.CustomReasonDto;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.baseinfo.filter.CustomReasonFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomReason;
import com.zjft.usp.anyfix.baseinfo.mapper.CustomReasonMapper;
import com.zjft.usp.anyfix.baseinfo.service.CustomReasonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 客户原因表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
public class CustomReasonServiceImpl extends ServiceImpl<CustomReasonMapper, CustomReason> implements CustomReasonService {

    @Resource
    private UasFeignService uasFeignService;

    @Override
    public List<CustomReason> selectByCorpAndType(Long customCorp, Integer reasonType) {
        Assert.notNull(reasonType, "类型不能为空");
        Assert.notNull(customCorp, "customCorp 不能为空");
        return this.list(new QueryWrapper<CustomReason>().eq("enabled", EnabledEnum.YES.getCode())
                .eq("custom_corp", customCorp)
                .eq("reason_type", reasonType));
    }

    /**
     * 分页查询客户原因列表
     *
     * @param customReasonFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 11:22
     **/
    @Override
    public ListWrapper<CustomReasonDto> query(CustomReasonFilter customReasonFilter) {
        QueryWrapper<CustomReason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("custom_corp", customReasonFilter.getCustomCorp());
        if (StrUtil.isNotBlank(customReasonFilter.getName())) {
            queryWrapper.like("name", StrUtil.trimToEmpty(customReasonFilter.getName()));
        }
        if (StrUtil.isNotBlank(customReasonFilter.getEnabled())) {
            queryWrapper.eq("enabled", customReasonFilter.getEnabled());
        }
        Page page = new Page(customReasonFilter.getPageNum(), customReasonFilter.getPageSize());
        IPage<CustomReason> customReasonIPage = this.page(page, queryWrapper);
        List<CustomReasonDto> customReasonDtoList = new ArrayList<>();
        if (customReasonIPage != null && CollectionUtil.isNotEmpty(customReasonIPage.getRecords())) {
            Result corpResult = this.uasFeignService.findCorpById(customReasonFilter.getCustomCorp());
            CorpDto corpDto = new CorpDto();
            if (corpResult != null && corpResult.getCode() == Result.SUCCESS) {
                corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
            }
            for (CustomReason customReason: customReasonIPage.getRecords()) {
                CustomReasonDto customReasonDto = new CustomReasonDto();
                BeanUtils.copyProperties(customReason, customReasonDto);
                customReasonDto.setCustomCorpName(corpDto == null ? "" : corpDto.getCorpName());
                customReasonDtoList.add(customReasonDto);
            }
        }
        return ListWrapper.<CustomReasonDto>builder().list(customReasonDtoList).total(customReasonIPage.getTotal()).build();
    }

    @Override
    public void save(CustomReason customReason, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtils.isEmpty(customReason.getName())) {
            throw new AppException("原因名称不能为空");
        }
        // 客户为空时，设置为当前企业
        if (LongUtil.isZero(customReason.getCustomCorp())) {
            customReason.setCustomCorp(reqParam.getCorpId());
        }
        QueryWrapper<CustomReason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",customReason.getName());
        queryWrapper.eq("custom_corp",customReason.getCustomCorp());
        List<CustomReason> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该原因名称已经存在");
        }
        customReason.setOperator(userInfo.getUserId());
        customReason.setOperateTime(DateUtil.date().toTimestamp());
        this.save(customReason);
    }

    @Override
    public void update(CustomReason customReason, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(customReason.getName()) ) {
            builder.append("原因名称不能为空");
        }
        if(LongUtil.isZero(customReason.getCustomCorp())) {
            builder.append("企业编号不能为空");
        }
        if(customReason.getId() == 0) {
            builder.append("原因编号不能为空");
        }
        if(builder.length() > 0 ){
            throw new AppException(builder.toString());
        }
        QueryWrapper<CustomReason> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",customReason.getName());
        queryWrapper.eq("custom_corp",customReason.getCustomCorp());
        queryWrapper.ne("id", customReason.getId());
        List<CustomReason> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该原因名称已经存在");
        }
        customReason.setOperateTime(DateUtil.date().toTimestamp());
        customReason.setOperator(userInfo.getUserId());
        this.updateById(customReason);
    }
}
