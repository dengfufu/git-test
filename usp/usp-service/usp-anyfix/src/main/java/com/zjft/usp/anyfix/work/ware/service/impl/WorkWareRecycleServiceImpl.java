package com.zjft.usp.anyfix.work.ware.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.work.ware.WareFilter;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import com.zjft.usp.anyfix.work.ware.model.WorkWareRecycle;
import com.zjft.usp.anyfix.work.ware.mapper.WorkWareRecycleMapper;
import com.zjft.usp.anyfix.work.ware.service.WorkWareRecycleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 工单回收物品表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkWareRecycleServiceImpl extends ServiceImpl<WorkWareRecycleMapper, WorkWareRecycle> implements WorkWareRecycleService {

    /**
     * 添加回收物品列表
     *
     * @param wareDtoList
     * @param userId
     * @param workId
     * @return
     * @author zgpi
     * @date 2019/10/14 2:50 下午
     **/
    @Override
    public void addWorkRecycleList(List<WareDto> wareDtoList, Long userId, Long workId) {
        if (wareDtoList != null) {
            WorkWareRecycle workWareRecycle;
            Date currentDate = DateUtil.date().toTimestamp();
            for (WareDto wareDto : wareDtoList) {
                workWareRecycle = new WorkWareRecycle();
                BeanUtils.copyProperties(wareDto, workWareRecycle);
                workWareRecycle.setRecycleId(KeyUtil.getId());
                workWareRecycle.setWorkId(workId);
                workWareRecycle.setOperator(userId);
                workWareRecycle.setOperateTime(currentDate);
                this.save(workWareRecycle);
            }
        }
    }

    @Override
    public List<WareDto> listByWorkId(Long workId) {
        List<WareDto> dtoList = new ArrayList<>();
        if (LongUtil.isZero(workId)) {
            return dtoList;
        }
        List<WorkWareRecycle> list = this.list(new QueryWrapper<WorkWareRecycle>().eq("work_id", workId));
        if (list != null && list.size() > 0) {
            for (WorkWareRecycle workWareRecycle : list) {
                WareDto wareDto = new WareDto();
                BeanUtils.copyProperties(workWareRecycle, wareDto);
                dtoList.add(wareDto);
            }
        }
        return dtoList;
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
        String checkNote = this.checkData(wareDto);
        if (checkNote != null && checkNote.length() > 0) {
            throw new AppException(checkNote);
        }
        WorkWareRecycle workWareRecycle = new WorkWareRecycle();
        BeanUtils.copyProperties(wareDto, workWareRecycle);
        workWareRecycle.setRecycleId(KeyUtil.getId());
        workWareRecycle.setOperator(curUserId);
        workWareRecycle.setOperateTime(DateTime.now());
        this.save(workWareRecycle);
    }

    /**
     * 更新
     *
     * @param wareDto
     * @param curUserId
     * @author canlei
     */
    @Override
    public void update(WareDto wareDto, Long curUserId) {
        String checkNote = this.checkData(wareDto);
        if (checkNote != null && checkNote.length() > 0) {
            throw new AppException(checkNote);
        }
        WorkWareRecycle workWareRecycle = this.getById(wareDto.getRecycleId());
        if (workWareRecycle == null) {
            throw new AppException("回收部件不存在，请检查！");
        }
        BeanUtils.copyProperties(wareDto, workWareRecycle);
        workWareRecycle.setOperator(curUserId);
        workWareRecycle.setOperateTime(DateTime.now());
        this.updateById(workWareRecycle);
    }

    /**
     * 模糊查询部件分类
     *
     * @param wareFilter
     * @return
     */
    @Override
    public List<WareDto> listCatalog(WareFilter wareFilter) {
        List<WareDto> list = new ArrayList<>();
        if (wareFilter == null || LongUtil.isZero(wareFilter.getCorpId())) {
            return list;
        }
        list = this.baseMapper.listCatalog(wareFilter);
        return list;
    }

    /**
     * 模糊查询部件品牌
     *
     * @param wareFilter
     * @return
     */
    @Override
    public List<WareDto> listBrand(WareFilter wareFilter) {
        List<WareDto> list = new ArrayList<>();
        if (wareFilter == null) {
            return list;
        }
        list = this.baseMapper.listBrand(wareFilter);
        return list;
    }

    /**
     * 模糊查询部件型号
     *
     * @param wareFilter
     * @return
     */
    @Override
    public List<WareDto> listModel(WareFilter wareFilter) {
        List<WareDto> list = new ArrayList<>();
        if (wareFilter == null) {
            return list;
        }
        list = this.baseMapper.listModel(wareFilter);
        return list;
    }

    /**
     * 根据workId删除
     * @param workId
     */
    @Override
    public void deleteByWorkId(Long workId) {
        if (LongUtil.isZero(workId)) {
            return;
        }
        this.remove(new UpdateWrapper<WorkWareRecycle>().eq("work_id", workId));
    }

    /**
     * 校验
     *
     * @param wareDto
     * @return
     * @author canlei
     */
    private String checkData(WareDto wareDto) {
        StringBuilder stringBuilder = new StringBuilder(16);
        Assert.notNull(wareDto, "表单不能为空");
        if (LongUtil.isZero(wareDto.getWorkId())) {
            stringBuilder.append("工单编号不能为空");
        }
        if (StringUtil.isNullOrEmpty(wareDto.getModelName())) {
            stringBuilder.append("<br>物品型号不能为空");
        }
        if (IntUtil.isZero(wareDto.getQuantity())) {
            stringBuilder.append("<br>数量不能为0");
        }
        return stringBuilder.toString();
    }

}
