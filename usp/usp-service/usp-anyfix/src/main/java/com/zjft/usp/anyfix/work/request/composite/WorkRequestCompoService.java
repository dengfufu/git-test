package com.zjft.usp.anyfix.work.request.composite;

import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.model.WxTemplateMessage;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * 工单请求聚合服务类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/10 08:42
 */
public interface WorkRequestCompoService {

    /**
     * 根据条件分页查询工单
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/10 7:09 下午
     **/
    ListWrapper<WorkDto> queryWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 工单导出查询全部工单(不分页)
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.anyfix.work.request.dto.WorkDto>
     * @date 2020/3/17
     */
    List<WorkDto> exportWork(WorkFilter workFilter,
                             UserInfo userInfo,
                             ReqParam reqParam);

    /**
     * 创建工单
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<Long> addWorkRequest(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改工单
     *
     * @param workRequestDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/26 19:31
     */
    Long modWorkRequest(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得工单详情
     *
     * @param workId
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/10/11 10:15 上午
     **/
    WorkDto findWorkDetail(Long workId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询当前用户的工单列表
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/2/16 14:14
     */
    ListWrapper<WorkDto> queryUserWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 分页查询当前用户待办工单列表
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/5/18 13:37
     **/
    ListWrapper<WorkDto> queryUserTodoWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询工单记录
     *
     * @param workFilter
     * @return
     */
    ListWrapper<WorkDto> queryDetailDto(WorkFilter workFilter);

    /**
     * 委托商确认服务分页查询工单
     *
     * @param workFilter
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/1 13:43
     **/
    ListWrapper<WorkDto> queryServiceConfirm(WorkFilter workFilter, Long corpId);

    /**
     * 委托商确认费用分页查询工单
     *
     * @param workFilter
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/1 13:43
     **/
    ListWrapper<WorkDto> queryFeeConfirm(WorkFilter workFilter, Long corpId);

    /**
     * 审核服务查询工单记录
     *
     * @param workFilter
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/9 15:38
     **/
    ListWrapper<WorkDto> queryServiceCheck(WorkFilter workFilter, Long userId, Long corpId);

    /**
     * 审核费用查询工单记录
     *
     * @param workFilter
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/9 15:38
     **/
    ListWrapper<WorkDto> queryFeeCheck(WorkFilter workFilter, Long userId, Long corpId);

    /**
     * 扫一扫建单时添加消息队列
     *
     * @param workIdList
     * @return
     * @author zgpi
     * @date 2020/2/26 20:28
     */
    void addMessageQueueByScanCreate(List<Long> workIdList);

    /**
     * 建单时添加消息队列
     *
     * @param workIdList
     * @return
     * @author zgpi
     * @date 2020/2/26 20:28
     */
    void addMessageQueueByCreate(List<Long> workIdList);

    /**
     * 修改工单时添加消息队列
     *
     * @param workId
     * @return
     * @author zgpi
     * @date 2020/2/26 20:28
     */
    void addMessageQueueByMod(Long workId);

    List<Long> checkDeviceInfoByAdd(WorkRequestDto workRequestDto);

    Long addDemanderCustom(WorkRequestDto workRequestDto, UserInfo userInfo);

    Long addDeviceBranch(WorkRequestDto workRequestDto, UserInfo userInfo, ReqParam reqParam);

    int getReplenishCount(@LoginUser UserInfo userInfo,
                          @CommonReqParam ReqParam reqParam);

    /**
     * 根据条件分页查询工单预警
     *
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-05-09
     */
    ListWrapper<WorkDto> queryRemindWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);


    /**
     * 获取微信用户新建的工单
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    ListWrapper<WorkDto> queryWXWork(WorkFilter workFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 发送微信建单通知消息
     * @param workId
     */
    WxTemplateMessage buildWxMessage(String workId);
}
