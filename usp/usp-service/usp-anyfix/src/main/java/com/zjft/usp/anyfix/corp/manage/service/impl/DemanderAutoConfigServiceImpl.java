package com.zjft.usp.anyfix.corp.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderAutoConfigDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderAutoConfig;
import com.zjft.usp.anyfix.corp.manage.mapper.DemanderAutoConfigMapper;
import com.zjft.usp.anyfix.corp.manage.service.DemanderAutoConfigService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.settle.enums.SettleTypeEnum;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 委托商自动化配置表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-07-20
 */
@Service
public class DemanderAutoConfigServiceImpl extends ServiceImpl<DemanderAutoConfigMapper, DemanderAutoConfig> implements DemanderAutoConfigService {

    /**
     * 根据id获取Dto
     *
     * @param id
     * @return
     */
    @Override
    public DemanderAutoConfigDto findById(Long id) {
        if (LongUtil.isZero(id)) {
            return null;
        }
        DemanderAutoConfigDto dto = new DemanderAutoConfigDto();
        DemanderAutoConfig demanderAutoConfig = this.getById(id);
        if (demanderAutoConfig == null) {
            return null;
        }
        BeanUtils.copyProperties(demanderAutoConfig, dto);
        dto.setSettleTypeName(SettleTypeEnum.lookup(dto.getSettleType()));
        return dto;
    }

    /**
     * 设置委托商自动化
     *
     * @param demanderAutoConfigDto
     * @param userId
     * @param corpId
     */
    @Override
    public void autoConfig(DemanderAutoConfigDto demanderAutoConfigDto, Long userId, Long corpId) {
        String msg = checkData(demanderAutoConfigDto);
        if (msg != null && msg.length() > 0) {
            throw new AppException(msg);
        }
        if (SettleTypeEnum.PER_WORK.getCode().equals(demanderAutoConfigDto.getSettleType())) {
            demanderAutoConfigDto.setSettleDay(0);
        }
        if (!EnabledEnum.YES.getCode().equals(demanderAutoConfigDto.getAutoConfirmService())) {
            demanderAutoConfigDto.setAutoConfirmServiceHours(BigDecimal.ZERO);
        }
        if (!EnabledEnum.YES.getCode().equals(demanderAutoConfigDto.getAutoConfirmFee())) {
            demanderAutoConfigDto.setAutoConfirmFeeHours(BigDecimal.ZERO);
        }
        DemanderAutoConfig demanderAutoConfig = new DemanderAutoConfig();
        BeanUtils.copyProperties(demanderAutoConfigDto, demanderAutoConfig);
        demanderAutoConfig.setOperator(userId);
        demanderAutoConfig.setOperateTime(DateUtil.date());
        this.saveOrUpdate(demanderAutoConfig);
    }

    /**
     * 查询委托商和自动化配置的映射
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public Map<Long, DemanderAutoConfigDto> mapDemanderAndAutoConfig(Long serviceCorp) {
        Map<Long, DemanderAutoConfigDto> map = new HashMap<>();
        if (LongUtil.isZero(serviceCorp)) {
            return map;
        }
        List<DemanderAutoConfigDto> list = this.baseMapper.listDtoByFilter(serviceCorp, 0L);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(demanderAutoConfigDto -> {
                map.put(demanderAutoConfigDto.getDemanderCorp(), demanderAutoConfigDto);
            });
        }
        return map;
    }

    /**
     * 查询服务商和自动化配置的映射
     *
     * @param demanderCorp
     * @return
     */
    @Override
    public Map<Long, DemanderAutoConfigDto> mapServiceAndAutoConfig(Long demanderCorp) {
        Map<Long, DemanderAutoConfigDto> map = new HashMap<>();
        if (LongUtil.isZero(demanderCorp)) {
            return map;
        }
        List<DemanderAutoConfigDto> list = this.baseMapper.listDtoByFilter(0L, demanderCorp);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(demanderAutoConfigDto -> {
                map.put(demanderAutoConfigDto.getServiceCorp(), demanderAutoConfigDto);
            });
        }
        return map;
    }

    /**
     * 根据服务商和委托商获取自动化配置
     *
     * @param demanderCorp
     * @param serviceCorp
     * @return
     */
    @Override
    public DemanderAutoConfigDto findByDemanderAndService(Long demanderCorp, Long serviceCorp) {
        if (LongUtil.isZero(demanderCorp) || LongUtil.isZero(serviceCorp)) {
            return null;
        }
        List<DemanderAutoConfigDto> list = this.baseMapper.listDtoByFilter(serviceCorp, demanderCorp);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : null;
    }

    /**
     * 校验数据
     *
     * @param demanderAutoConfigDto
     * @return
     */
    private String checkData(DemanderAutoConfigDto demanderAutoConfigDto) {
        StringBuilder sb = new StringBuilder(32);
        if (demanderAutoConfigDto == null) {
            throw new AppException("参数不能为空");
        }
        if (LongUtil.isZero(demanderAutoConfigDto.getId())) {
            sb.append("<br>委托关系编号不能为空；");
        }
        if (IntUtil.isZero(demanderAutoConfigDto.getSettleType())) {
            sb.append("<br>结算方式不能为空;");
        }
        if (IntUtil.isNotZero(demanderAutoConfigDto.getSettleDay()) &&
                (demanderAutoConfigDto.getSettleDay() < 1 || demanderAutoConfigDto.getSettleDay() > 31)) {
            sb.append("<br>结算日期在1~31之间");
        }
        return sb.toString();
    }

}
