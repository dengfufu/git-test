package com.zjft.usp.zj.work.cases.atmcase.composite;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcPartReplaceFilter;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcPartReplaceDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcPartReplaceListDto;

/**
 * 工行对接维修登记聚合接口
 *
 * @author JFZOU
 * @version 1.0
 * @date 2020-03-13 16:43
 **/
public interface IcbcPartReplaceCompoService {

    /**
     * 查询工行维修登记列表页面
     *
     * @param caseId
     * @param userInfo
     * @param reqParam
     * @return
     */
    IcbcPartReplaceListDto listBy(String caseId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入添加工行维修登记页面
     *
     * @param icbcPartReplaceFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    IcbcPartReplaceDto addIcbcReplace(IcbcPartReplaceFilter icbcPartReplaceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 处理添加工行维修登记请求
     *
     * @param userInfo
     * @param reqParam
     * @param icbcPartReplaceDto
     * @return
     */
    int addIcbcReplaceSubmit(UserInfo userInfo,
                                    ReqParam reqParam,
                                    IcbcPartReplaceDto icbcPartReplaceDto);

    /**
     * 进入修改工行维修登记页面
     *
     * @param icbcPartReplaceFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    IcbcPartReplaceDto modIcbcReplace(IcbcPartReplaceFilter icbcPartReplaceFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 处理添加工行维修登记请求
     *
     * @param icbcPartReplaceDto
     * @param userInfo
     * @param reqParam
     * @return
     */
    int modIcbcReplaceSubmit(
            IcbcPartReplaceDto icbcPartReplaceDto,
            UserInfo userInfo,
            ReqParam reqParam);

    /**
     * 处理删除工行维修登记请求
     *
     * @param replaceId
     * @param userInfo
     * @param reqParam
     * @return
     */
    int deleteIcbcReplaceSubmit(
            Long replaceId,
            UserInfo userInfo,
            ReqParam reqParam);
}
