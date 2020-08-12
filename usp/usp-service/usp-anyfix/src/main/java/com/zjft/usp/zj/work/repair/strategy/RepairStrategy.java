package com.zjft.usp.zj.work.repair.strategy;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletResponse;

/**
 * 报修策略接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2019-11-13 20:33
 **/
public interface RepairStrategy {
    /**
     * 查看报修单详情
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String findRepairDetail(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入转处理页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String turnHandle(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 转处理提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String turnHandleSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入电话处理页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String phoneHandle(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 电话处理提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String phoneHandleSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入退单页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String returnHandle(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 退单提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String returnHandleSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入关联CASE页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String associateCase(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 关联CASE提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-18
     */
    String associateCaseSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询补录失败的记录
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String listBxSendCreateFail(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入补录失败修改页面
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String modBxSendCreateFail(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 补录失败修改提交
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-19
     */
    String modBxSendCreateFailSubmit(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获取银行编号与名称映射Map
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-23
     */
    String findBankMap(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);


    /**
     * 获取服务站和网点编号与名称映射Map
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    String findBureauAndBranchMap(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 根据交易号获取工行对接报修图片
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    String listFaultRepairPic(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查看工行对接报修图片
     *
     * @param paramMap
     * @param userInfo
     * @param reqParam
     * @param response
     * @return
     * @author Qiugm
     * @date 2020-03-25
     */
    void viewFaultRepairPic(MultiValueMap<String, Object> paramMap, UserInfo userInfo, ReqParam reqParam, HttpServletResponse response);
}

