package com.zjft.usp.anyfix.work.ware.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import com.zjft.usp.anyfix.work.ware.model.WorkWareUsed;
import com.zjft.usp.anyfix.work.ware.mapper.WorkWareUsedMapper;
import com.zjft.usp.anyfix.work.ware.service.WorkWareUsedService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工单使用物品表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkWareUsedServiceImpl extends ServiceImpl<WorkWareUsedMapper, WorkWareUsed> implements WorkWareUsedService {

    @Autowired
    private WorkFeeService workFeeService;

    /**
     * 添加使用物品列表
     *
     * @param wareDtoList
     * @param userId
     * @param workId
     * @return
     * @author zgpi
     * @date 2019/10/14 2:51 下午
     **/
    @Override
    public void addWorkUsedList(List<WareDto> wareDtoList, Long userId, Long workId) {
        if (wareDtoList != null) {
            WorkWareUsed workWareUsed;
            Date currentDate = DateUtil.date().toTimestamp();
            for (WareDto wareDto : wareDtoList) {
                workWareUsed = new WorkWareUsed();
                BeanUtils.copyProperties(wareDto, workWareUsed);
                workWareUsed.setUsedId(KeyUtil.getId());
                workWareUsed.setWorkId(workId);
                workWareUsed.setOperator(userId);
                workWareUsed.setOperateTime(currentDate);
                this.save(workWareUsed);
            }
        }
    }

    @Override
    public List<WareDto> listByWorkId(Long workId) {
        List<WareDto> dtoList = new ArrayList<>();
        if (LongUtil.isZero(workId)) {
            return dtoList;
        }
        List<WorkWareUsed> list = this.list(new QueryWrapper<WorkWareUsed>().eq("work_id", workId));
        if (list != null && list.size() > 0) {
            for (WorkWareUsed workWareUsed : list) {
                WareDto wareDto = new WareDto();
                BeanUtils.copyProperties(workWareUsed, wareDto);
                dtoList.add(wareDto);
            }
        }
        return dtoList;
    }

    /**
     * 更新
     *
     * @param wareDto
     * @author canlei
     */
    @Override
    public void update(WareDto wareDto, Long curUserId) {
        if (wareDto == null || LongUtil.isZero(wareDto.getUsedId())) {
            throw new AppException("回收编号不能为空！");
        }
        WorkWareUsed workWareUsed = this.getById(wareDto.getUsedId());
        if (workWareUsed == null) {
            throw new AppException("使用备件不存在，请检查");
        }

        WorkFee workFee = this.workFeeService.getById(workWareUsed.getWorkId());
        if (workFee != null) {
            // 更新工单费用
            BigDecimal wareUsedFee = workFee.getWareUseFee();
            wareUsedFee = wareUsedFee.subtract(workWareUsed.getUnitPrice().multiply(BigDecimal.valueOf(workWareUsed.getQuantity())))
                    .add(wareDto.getUnitPrice().multiply(BigDecimal.valueOf(wareDto.getQuantity())));
            workFee.setWareUseFee(wareUsedFee);
            this.workFeeService.updateById(workFee);
        }
        BeanUtils.copyProperties(wareDto, workWareUsed);
        this.updateById(workWareUsed);
    }

    /**
     * 插入
     *
     * @param wareDto
     * @param curUserId
     * @author canlei
     */
    @Override
    public void add(WareDto wareDto, Long curUserId) {
        Assert.notNull(wareDto, "表单不能为空");
        if (LongUtil.isZero(wareDto.getWareId()) && StringUtil.isNullOrEmpty(wareDto.getModelName())) {
            throw new AppException("部件型号不能为空");
        }
        if (LongUtil.isZero(wareDto.getWorkId())) {
            throw new AppException("工单编号不能为空");
        }
        WorkWareUsed workWareUsed = new WorkWareUsed();
        BeanUtils.copyProperties(wareDto, workWareUsed);
        workWareUsed.setUsedId(KeyUtil.getId());
        workWareUsed.setOperator(curUserId);
        workWareUsed.setOperateTime(DateTime.now());
        this.save(workWareUsed);
        // 更新工单费用
        WorkFee workFee = this.workFeeService.getById(wareDto.getWorkId());
        if (workFee != null) {
            BigDecimal wareUseFee = wareDto.getUnitPrice().multiply(BigDecimal.valueOf(wareDto.getQuantity()));
            workFee.setWareUseFee(workFee.getWareUseFee().add(wareUseFee));
            this.workFeeService.updateById(workFee);
        }
    }

    /**
     * 删除
     *
     * @param usedId
     * @author canlei
     */
    @Override
    public void delete(Long usedId) {
        if (LongUtil.isZero(usedId)) {
            throw new AppException("主键不能为空");
        }
        WorkWareUsed workWareUsed = this.getById(usedId);
        if (workWareUsed == null) {
            throw new AppException("使用部件不存在，请检查");
        }
        // 更新工单费用
        WorkFee workFee = this.workFeeService.getById(workWareUsed.getWorkId());
        if (workFee != null) {
            BigDecimal wareUseFee = workWareUsed.getUnitPrice().multiply(BigDecimal.valueOf(workWareUsed.getQuantity()));
            workFee.setWareUseFee(workFee.getWareUseFee().subtract(wareUseFee));
            this.workFeeService.updateById(workFee);
        }
        this.removeById(usedId);
    }

    /**
     * 根据workId删除
     *
     * @param workId
     */
    @Override
    public void deleteByWorkId(Long workId) {
        if (LongUtil.isZero(workId)) {
            return;
        }
        this.remove(new UpdateWrapper<WorkWareUsed>().eq("work_id", workId));
    }

}
