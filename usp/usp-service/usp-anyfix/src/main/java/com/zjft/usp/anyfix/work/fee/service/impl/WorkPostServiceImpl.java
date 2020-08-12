package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.work.fee.dto.WorkPostDto;
import com.zjft.usp.anyfix.work.fee.enums.PostWayEnum;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.model.WorkPost;
import com.zjft.usp.anyfix.work.fee.mapper.WorkPostMapper;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.fee.service.WorkPostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 工单邮寄费 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkPostServiceImpl extends ServiceImpl<WorkPostMapper, WorkPost> implements WorkPostService {

    @Autowired
    private WorkFeeService workFeeService;

    /**
     * 根据工单编号查询
     *
     * @param workId
     * @return
     * @author canlei
     */
    @Override
    public List<WorkPostDto> listByWorkId(Long workId) {
        List<WorkPostDto> dtoList = new ArrayList<>();
        if (LongUtil.isZero(workId)) {
            return dtoList;
        }
        List<WorkPost> list = this.list(new QueryWrapper<WorkPost>().eq("work_id", workId));
        if (CollectionUtil.isNotEmpty(list)) {
            dtoList = list.stream().map(workPost -> {
                WorkPostDto workPostDto = new WorkPostDto();
                BeanUtils.copyProperties(workPost, workPostDto);
                workPostDto.setPostWayName(PostWayEnum.getNameByCode(workPost.getPostWay()));
                return workPostDto;
            }).collect(Collectors.toList());
        }
        return dtoList;
    }

    /**
     * 添加
     *
     * @author canlei
     * @param workPostDto
     * @param curUserId
     */
    @Override
    public void add(WorkPostDto workPostDto, Long curUserId) {
        if (workPostDto == null || LongUtil.isZero(workPostDto.getWorkId())) {
            throw new AppException("工单编号不能为空");
        }
        WorkPost workPost = new WorkPost();
        BeanUtils.copyProperties(workPostDto, workPost);
        workPost.setPostId(KeyUtil.getId());
        workPost.setOperator(curUserId);
        workPost.setOperateTime(DateTime.now());
        this.save(workPost);
        // 更新工单费用
//        WorkFee workFee = this.workFeeService.getById(workPostDto.getWorkId());
//        if (workFee != null) {
//            workFee.setWarePostFee(workFee.getWarePostFee().add(workPostDto.getPostage()));
//            this.workFeeService.updateById(workFee);
//        }
    }

    /**
     * 更新
     *
     * @author canlei
     * @param workPostDto
     * @param curUserId
     */
    @Override
    public void update(WorkPostDto workPostDto, Long curUserId) {
        if (workPostDto == null || LongUtil.isZero(workPostDto.getPostId())) {
            throw new AppException("主键不能为空");
        }
        WorkPost workPost = this.getById(workPostDto.getPostId());
        if (workPost == null) {
            throw new AppException("邮寄单不存在，请检查");
        }
//        WorkFee workFee = this.workFeeService.getById(workPost.getWorkId());
//        if (workFee != null) {
//            BigDecimal postFee = workFee.getWarePostFee();
//            postFee = postFee.subtract(workPost.getPostage()).add(workPostDto.getPostage());
//            workFee.setWarePostFee(postFee);
//            this.workFeeService.updateById(workFee);
//        }
        BeanUtils.copyProperties(workPostDto, workPost);
        this.updateById(workPost);
    }

    /**
     * 删除
     *
     * @author canlei
     * @param postId
     */
    @Override
    public void delete(Long postId) {
        if (LongUtil.isZero(postId)) {
            throw new AppException("主键不能为空");
        }
        WorkPost workPost = this.getById(postId);
        if (workPost == null) {
            throw new AppException("工单邮寄单不存在，请检查");
        }
        // 更新工单费用
//        WorkFee workFee = this.workFeeService.getById(workPost.getWorkId());
//        if (workFee != null) {
//            workFee.setWarePostFee(workFee.getWarePostFee().subtract(workPost.getPostage()));
//            this.workFeeService.updateById(workFee);
//        }
        this.removeById(postId);
    }

    /**
     * 模糊匹配邮寄公司
     *
     * @param workPostDto
     * @return
     */
    @Override
    public List<WorkPostDto> matchPostCorp(WorkPostDto workPostDto) {
        List<WorkPostDto> list = new ArrayList<>();
        if (workPostDto == null || LongUtil.isZero(workPostDto.getCorpId())) {
            return list;
        }
        list = this.baseMapper.matchPostCorp(workPostDto);
        return list;
    }

    /**
     * 根据工单编号删除
     *
     * @param workId
     */
    @Override
    public void deleteByWorkId(Long workId) {
        this.remove(new UpdateWrapper<WorkPost>().eq("work_id", workId));
    }

}
