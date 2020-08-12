package com.zjft.usp.anyfix.work.sign.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.work.sign.dto.WorkSignDto;
import com.zjft.usp.anyfix.work.sign.filter.WorkSignFilter;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 工单签到表 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-23
 */
public interface WorkSignService extends IService<WorkSign> {

    /**
     * 工程师签到
     *
     * @author zgpi
     * @date 2019/10/14 9:46 上午
     * @param workSignDto
     * @param userInfo
     * @param reqParam
     * @return
     **/
    Long signWork(WorkSignDto workSignDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询签到记录(OA报销专用)
     * @param workSignFilter
     * @return
     */
    List<WorkSign> queryWorkSignForCost(WorkSignFilter workSignFilter);

    /**
     * 查询工单的签到记录
     *
     * @param workId
     * @return
     */
    List<WorkSign> listByWorkId(Long workId);

    /**
     * 查询工单的签到记录
     * @date 2020/5/15
     * @return com.zjft.usp.anyfix.work.sign.dto.WorkSignDto
     */
    WorkSignDto querySign(Long signId);

}
