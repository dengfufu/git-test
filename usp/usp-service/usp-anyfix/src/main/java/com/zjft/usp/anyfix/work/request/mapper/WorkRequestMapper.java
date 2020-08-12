package com.zjft.usp.anyfix.work.request.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.settle.dto.SettleBranchDetailDto;
import com.zjft.usp.anyfix.settle.dto.SettleCustomDetailDto;
import com.zjft.usp.anyfix.settle.dto.SettleStaffDetailDto;
import com.zjft.usp.anyfix.settle.filter.SettleBranchDetailFilter;
import com.zjft.usp.anyfix.settle.filter.SettleCustomDetailFilter;
import com.zjft.usp.anyfix.settle.filter.SettleStaffDetailFilter;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.dto.WorkForAssignDto;
import com.zjft.usp.anyfix.work.request.dto.WorkStatAreaDto;
import com.zjft.usp.anyfix.work.request.dto.WorkStatusCountDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.model.WorkRequest;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单服务请求表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-23
 */
public interface WorkRequestMapper extends BaseMapper<WorkRequest> {

    /**
     * 查询工单
     *
     * @param page
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2019/10/12 4:12 下午
     **/
    List<WorkDto> queryWork(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 委托商分页查询服务确认工单
     *
     * @param page
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/6/1 13:45
     **/
    List<WorkDto> queryServiceConfirm(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 获取可以自动确认服务的工单
     *
     * @param serviceCorp
     * @return
     */
    List<WorkDeal> listAutoServiceConfirm(@Param("serviceCorp") Long serviceCorp);

    /**
     * 委托商分页查询费用确认工单
     *
     * @param page
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/6/1 13:46
     **/
    List<WorkDto> queryFeeConfirm(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 获取可以自动确认费用的工单
     *
     * @param serviceCorp
     * @return
     */
    List<WorkDeal> listAutoFeeConfirm(@Param("serviceCorp") Long serviceCorp);

    /**
     * 按状态统计工单
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2019/10/23 11:14 上午
     **/
    List<Map<String, Object>> countWorkStatus(@Param("workFilter") WorkFilter workFilter);

    /**
     * 按状态统计当前用户的工单
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/2/16 20:41
     */
    List<Map<String, Object>> countUserWorkStatus(@Param("workFilter") WorkFilter workFilter);

    /**
     * 统计当前用户的待录入费用工单数
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/5/17 11:54
     **/
    Integer countUserWorkFee(@Param("workFilter") WorkFilter workFilter);

    /**
     * 统计当前用户审核不通过的工单数
     *
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/5/19 10:12
     **/
    Integer countUserReject(@Param("workFilter") WorkFilter workFilter);

    /**
     * 统计区域工单
     *
     * @param workFilter
     * @return
     **/
    List<WorkStatAreaDto> countWorkArea(@Param("workFilter") WorkFilter workFilter);


    /**
     * 查询处理占比数量
     *
     * @param workFilter
     * @return
     */
    List<WorkStatusCountDto> countWorkDeal(@Param("workFilter") WorkFilter workFilter);

    /**
     * 查询客户结算单明细
     *
     * @param page
     * @param settleCustomDetailFilter
     * @return
     */
    List<SettleCustomDetailDto> querySettleCustomDetailDto(Page page, @Param("settleCustomDetailFilter") SettleCustomDetailFilter settleCustomDetailFilter);

    /**
     * 查询网点结算单明细
     *
     * @param page
     * @param settleBranchDetailFilter
     * @return
     */
    List<SettleBranchDetailDto> querySettleBranchDetailDto(Page page, @Param("settleBranchDetailFilter") SettleBranchDetailFilter settleBranchDetailFilter);

    /**
     * 查询网点结算单明细
     *
     * @param page
     * @param settleStaffDetailFilter
     * @return
     */
    List<SettleStaffDetailDto> querySettleStaffDetailDto(Page page, @Param("settleStaffDetailFilter") SettleStaffDetailFilter settleStaffDetailFilter);

    /**
     * 分页查询当前用户的工单列表
     *
     * @param page
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/2/16 14:16
     */
    List<WorkDto> queryUserWork(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 分页查询当前用户待办工单列表
     *
     * @param page
     * @param workFilter
     * @return
     * @author zgpi
     * @date 2020/5/18 13:37
     **/
    List<WorkDto> queryUserTodoWork(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 查询客户的工单记录
     *
     * @param page
     * @param customCorp
     * @param demanderCorp
     * @return
     */
    List<WorkDto> queryWorkDetailDto(Page page, @Param("customCorp") Long customCorp,
                                     @Param("demanderCorp") Long demanderCorp);

    /**
     * 查询工单待接单工程师
     *
     * @param workFilter
     * @return
     * @author jfzou
     * @date 2020/02/16 22:40 下午
     **/
    List<WorkForAssignDto> queryForAssignEngineersDto(@Param("workFilter") WorkFilter workFilter);

    /**
     * 查询工程师报销工单
     *
     * @param workFilter
     * @return
     */
    List<WorkDto> queryEngineerWorkCost(@Param("workFilter") WorkFilter workFilter);

    /**
     * 查询工程师绩效工单
     *
     * @param workFilter
     * @return
     */
    List<WorkDto> queryEngineerWorkAppraise(@Param("workFilter") WorkFilter workFilter);

    /**
     * 审核服务查询工单
     *
     * @param page
     * @param workFilter
     * @return
     */
    List<WorkDto> queryServiceCheck(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 审核费用查询工单
     *
     * @param page
     * @param workFilter
     * @return
     */
    List<WorkDto> queryFeeCheck(Page page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 获取某日所有工单
     *
     * @param serviceTime
     * @return
     */
    List<WorkDto> listWorkCodeByDay(@Param("serviceTime") String serviceTime);

    int getReplenishCount( @Param("signatureStatus") String signatureStatus,
                          @Param("filesStatus") String fileStatus,
                          @Param("userId") Long userId,
                          @Param("serviceCorp") Long serviceCorp);
    /**
     * 根据条件分页查询工单预警
     *
     * @param page
     * @param workFilter
     * @return
     * @author Qiugm
     * @date 2020-05-12
     */
    List<WorkDto> queryRemindWork(Page<WorkDto> page, @Param("workFilter") WorkFilter workFilter);

    /**
     * 微信工单
     * @param page
     * @param workFilter
     * @return
     */
    List<WorkDto> queryWXWork(Page page, @Param("workFilter") WorkFilter workFilter);

}
