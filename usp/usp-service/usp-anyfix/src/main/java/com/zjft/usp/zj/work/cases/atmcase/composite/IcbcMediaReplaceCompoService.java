package com.zjft.usp.zj.work.cases.atmcase.composite;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.common.dto.WoResult;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcMediaDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.icbc.IcbcMediaListDto;
import com.zjft.usp.zj.work.cases.atmcase.dto.atmcase.OldCaseDto;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaAddFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaCloseFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaDeleteFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.IcbcMediaQueryFilter;


/**
 * 工行对接介质更换聚合接口
 *
 * @author JFZOU
 * @version 1.0
 * @date 2020-04-01 10:00
 **/
public interface IcbcMediaReplaceCompoService {


    /**
     * 查询工行介质交接列表
     * @param userInfo
     * @param reqParam
     * @param icbcMediaQueryFilter
     * @return
     */
    IcbcMediaListDto listMediaReplace(UserInfo userInfo, ReqParam reqParam,
                                      IcbcMediaQueryFilter icbcMediaQueryFilter);

    /**
     * 进入工行介质交接添加页面
     *
     * @param userInfo
     * @param reqParam
     * @param icbcMediaAddFilter
     * @return
     */
    OldCaseDto addMediaReplace(UserInfo userInfo, ReqParam reqParam, IcbcMediaAddFilter icbcMediaAddFilter);

    /**
     * 提交工行介质交接添加请求
     * @param userInfo
     * @param reqParam
     * @param icbcMediaDto
     * @return
     */
    int addMediaReplaceSubmit(UserInfo userInfo, ReqParam reqParam, IcbcMediaDto icbcMediaDto);

    /**
     * 进入修改工行介质交接页面
     *
     * @param userInfo
     * @param reqParam
     * @param replaceId
     * @return
     */
    IcbcMediaDto modMediaReplace(UserInfo userInfo, ReqParam reqParam, Long replaceId);

    /**
     * 处理修改工行介质交接请求
     *
     * @param userInfo
     * @param reqParam
     * @param icbcMediaDto
     * @return
     */
    int modMediaReplaceSubmit(UserInfo userInfo, ReqParam reqParam, IcbcMediaDto icbcMediaDto);

    /**
     * 处理删除工行介质交接记录请求
     *
     * @param userInfo
     * @param reqParam
     * @param icbcMediaDeleteFilter
     * @return
     */
    int delMediaReplaceSubmit(UserInfo userInfo, ReqParam reqParam, IcbcMediaDeleteFilter icbcMediaDeleteFilter);


    /**
     * 处理工行介质交接记录状态检查
     *
     * @param userInfo
     * @param reqParam
     * @param icbcMediaCloseFilter
     * @return
     */
    WoResult checkMediaByClose(UserInfo userInfo, ReqParam reqParam, IcbcMediaCloseFilter icbcMediaCloseFilter);
}
