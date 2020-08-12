package com.zjft.usp.anyfix.work.fee.service;

import com.zjft.usp.anyfix.work.fee.dto.WorkPostDto;
import com.zjft.usp.anyfix.work.fee.model.WorkPost;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工单邮寄费 服务类
 * </p>
 *
 * @author canlei
 * @since 2020-01-07
 */
public interface WorkPostService extends IService<WorkPost> {

    /**
     * 根据工单编号查询
     *
     * @author canlei
     * @param workId
     * @return
     */
    List<WorkPostDto> listByWorkId(Long workId);

    /**
     * 添加
     *
     * @author canlei
     * @param workPostDto
     * @param curUserId
     */
    void add(WorkPostDto workPostDto, Long curUserId);

    /**
     * 更新
     *
     * @author canlei
     * @param workPostDto
     * @param curUserId
     */
    void update(WorkPostDto workPostDto, Long curUserId);

    /**
     * 删除
     *
     * @author canlei
     * @param postId
     */
    void delete(Long postId);

    /**
     * 模糊匹配邮寄公司
     *
     * @param workPostDto
     * @return
     */
    List<WorkPostDto> matchPostCorp(WorkPostDto workPostDto);

    /**
     * 根据工单编号删除
     *
     * @param workId
     */
    void deleteByWorkId(Long workId);

}
