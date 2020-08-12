package com.zjft.usp.wms.business.trans.composite;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.filter.TransFilter;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;


/**
 * 物料调拨聚合服务接口
 * @Author: JFZOU
 * @Date: 2019-11-13 15:07
 */
public interface TransCompoService {

    /**
     * 保存调拨单申请信息
     *  @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    void save(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 添加调拨单申请信息并自动拆单
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     */
    void add(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 审批申请单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void audit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询已提交的调拨单
     *
     * @author canlei
     * @date 2019-12-09
     * @param transFilter
     * @return
     */
    ListWrapper<TransWareCommonDto> queryTrans(TransFilter transFilter);

    /**
     * 查询调拨单详情
     *
     * @param transId
     * @return com.zjft.usp.wms.business.trans.dto.TransWareCommonDto
     * @author zphu
     * @date 2019/12/18 11:17
     * @throws
    **/
    TransWareCommonDto queryDetail(Long transId,ReqParam reqParam);

    /**
     * 查询保存的调拨单
     * @param transFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    ListWrapper<TransWareCommonDto> querySavedTrans(TransFilter transFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 查询单个调拨单信息
     *
     * @param transId
     * @param reqParam
     * @return
     */
    TransWareCommonDto viewTrans(Long transId, ReqParam reqParam);



    /**
     * 批量审批多个调拨单
     *
     * @author Qiugm
     * @date 2019-11-21
     * @param transWareCommonDto
     * @param userInfo
     * @param reqParam
     * @return void
     */
    void batchAudit(TransWareCommonDto transWareCommonDto, UserInfo userInfo, ReqParam reqParam,Integer status);


    /**
     * 统计调拨单对应的各个状态数量
     * @author zerlin
     * @param transFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    Map<Integer,Long> countByWareStatus(TransFilter transFilter, UserInfo userInfo, ReqParam reqParam);

}
