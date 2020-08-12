package com.zjft.usp.zj.work.repair.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.CaseDto;
import com.zjft.usp.zj.work.repair.dto.BxImgDto;
import com.zjft.usp.zj.work.repair.dto.BxSendCreateFailDto;
import com.zjft.usp.zj.work.repair.dto.RepairDto;
import com.zjft.usp.zj.work.repair.filter.RepairFilter;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 老平台报修聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:47
 **/
public interface RepairCompoService {
    /**
     * 根据条件分页查询报修单
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    ListWrapper<RepairDto> listRepair(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据条件分页查询未关闭的报修单
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    ListWrapper<RepairDto> listUnCloseRepair(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看报修单详情
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    Map findRepairDetail(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入转处理页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    Map turnHandle(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 转处理提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    void turnHandleSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入电话处理页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    Map phoneHandle(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 电话处理提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    void phoneHandleSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入退单页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    Map returnHandle(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 退单提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    void returnHandleSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入关联CASE页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    Map associateCase(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 关联CASE提交
     *
     * @param repairDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    void associateCaseSubmit(RepairDto repairDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询补录失败的记录
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    ListWrapper<BxSendCreateFailDto> listBxSendCreateFail(RepairFilter repairFilter, UserInfo userInfo,
                                                          ReqParam reqParam);

    /**
     * 进入补录失败修改页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    Map modBxSendCreateFail(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 补录失败修改提交
     *
     * @param bxSendCreateFailDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    void modBxSendCreateFailSubmit(BxSendCreateFailDto bxSendCreateFailDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取银行编号与名称映射Map
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-23
     */
    Map findBankMap(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取服务站和网点编号与名称映射Map
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    Map findBureauAndBranchMap(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据交易号获取工行对接报修图片
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    List<BxImgDto> listFaultRepairPic(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看工行对接报修图片
     *
     * @param fileId
     * @param serviceId
     * @param userInfo
     * @param reqParam
     * @param response
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    void viewFaultRepairPic(String fileId, String serviceId, UserInfo userInfo, ReqParam reqParam,
            HttpServletResponse response);

    /**
     * 进入报修单查询页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-26
     */
    Map queryRepair(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入未关闭的报修单查询页面
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-26
     */
    Map queryUnCloseRepair(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);


    /**
     * 根据报修单的ID找到与之对应的最新执行人处理的CASE
     *
     * @author Qiugm
     * @date 2020-03-31
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<CaseDto> pickRelevanceCaseId(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据新平台工单状态查询报修信息
     *
     * @param repairFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-04-01
     */
    ListWrapper<RepairDto> listRepairByWorkStatus(RepairFilter repairFilter, UserInfo userInfo, ReqParam reqParam);
}
