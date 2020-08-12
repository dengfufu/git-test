package com.zjft.usp.anyfix.work.booking.service;

import com.zjft.usp.anyfix.work.booking.dto.WorkBookingDto;
import com.zjft.usp.anyfix.work.booking.model.WorkBooking;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

/**
 * <p>
 * 工单预约表 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface WorkBookingService extends IService<WorkBooking> {

    /**
     * 工程师修改预约时间
     *
     * @param workBookingDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/11/5 09:04
     **/
    void changeBookingTime(WorkBookingDto workBookingDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改服务模式为远程处理
     * 修改工单状态为待签到
     *
     * @author zgpi
     * @date 2019/10/14 2:27 下午
     * @param workId
     * @param userInfo
     * @param reqParam
     * @return
     **/
    void modServiceModeToRemote(Long workId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改服务模式为现场处理
     * 修改工单状态为待服务
     *
     * @author zgpi
     * @date 2019/10/14 2:27 下午
     * @param workId
     * @param userInfo
     * @param reqParam
     * @return
     **/
    void modServiceModeToLocale(Long workId, UserInfo userInfo, ReqParam reqParam);

}
