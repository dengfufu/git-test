package com.zjft.usp.zj.work.cases.atmcase.composite;

import com.alibaba.fastjson.JSONObject;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.zj.work.cases.atmcase.dto.partreplace.*;
import com.zjft.usp.zj.work.cases.atmcase.filter.PriStockPartFilter;
import com.zjft.usp.zj.work.cases.atmcase.filter.VendorPartFilter;

import java.util.List;

/**
 * ATM机CASE备件更换聚合接口
 *
 * @author Qiugm
 * @version 1.0
 * @date 2020-03-13 16:43
 **/
public interface ZiJinPartReplaceCompoService {

    /**
     * 备件更换列表
     *
     * @param workCode CASE号
     * @param userInfo 当前用户
     * @param reqParam 公共参数
     * @return 备件更换列表
     * @author zgpi
     * @date 2020/3/31 14:17
     */
    PartReplaceListDto listPartReplace(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入添加备件更换页面
     *
     * @param workCode CASE号
     * @param userInfo 当前用户
     * @param reqParam 公共参数
     * @return 备件更换页面dto
     * @author Qiugm
     * @date 2020-03-17
     */
    PartReplaceAddPageDto addPartReplace(String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 添加备件更换提交
     *
     * @param partReplaceAddDto 备件更换对象
     * @param userInfo          当前用户
     * @param reqParam          公共参数
     * @return 空
     * @author zgpi
     * @date 2020/4/4 15:13
     */
    JSONObject addPartReplaceSubmit(PartReplaceAddDto partReplaceAddDto, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查备件更换是否存在
     *
     * @param partReplaceId 更换ID
     * @param userInfo      当前用户
     * @param reqParam      公共参数
     * @return 空
     * @author zgpi
     * @date 2020/4/5 16:07
     */
    JSONObject checkPartReplaceExist(String partReplaceId, UserInfo userInfo, ReqParam reqParam);

    /**
     * 进入修改备件更换页面
     *
     * @param partReplaceId 更换ID
     * @param workCode      CASE号
     * @param userInfo      当前用户
     * @param reqParam      公共参数
     * @return 备件更换页面dto
     * @author zgpi
     * @date 2020/4/5 16:23
     */
    PartReplaceModPageDto modPartReplace(String partReplaceId, String workCode,
                                         UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改备件更换提交
     *
     * @param partReplaceModDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/6 10:29
     */
    JSONObject modPartReplaceSubmit(PartReplaceModDto partReplaceModDto,
                                    UserInfo userInfo, ReqParam reqParam);

    /**
     * 删除备件更换
     *
     * @param id       更换ID
     * @param userInfo 当前用户
     * @param reqParam 公共参数
     * @return 空
     * @author zgpi
     * @date 2020/4/5 14:16
     */
    JSONObject delPartReplace(String id, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得个人备件列表
     *
     * @param priStockPartFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/2 19:48
     */
    List<PriStockPartDto> listPriStockPart(PriStockPartFilter priStockPartFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得CASE的换上备件列表
     *
     * @param partCode
     * @param workCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/2 20:22
     */
    List<PartReplaceDto> listUpGhPart(String partCode, String workCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 判断是否厂商备件
     *
     * @param vendorPartFilter
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/3 15:10
     */
    VendorPartDto isVendorPart(VendorPartFilter vendorPartFilter, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得车牌号列表
     *
     * @param serviceBranch
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/3 15:54
     */
    List<String> listCarNo(String serviceBranch, UserInfo userInfo, ReqParam reqParam);

    /**
     * 获得备件状态
     *
     * @param partCode
     * @param newPartId
     * @param newBarCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/3 16:20
     */
    Integer findPartStatus(String partCode, String newPartId, String newBarCode,
                           UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查UR
     *
     * @param partCode
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/4 14:22
     */
    Integer checkUR(String partCode, UserInfo userInfo, ReqParam reqParam);

    /**
     * 检查换上的专用备件是否已经被使用
     *
     * @param workCode
     * @param partCode
     * @param newPartId
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/4 14:37
     */
    JSONObject checkPartId(String workCode, String partCode, String newPartId,
                           UserInfo userInfo, ReqParam reqParam);

    /**
     * 是否强制使用厂商备件
     *
     * @param partReplaceDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2020/4/4 15:01
     */
    String mandatoryUseVendorPart(PartReplaceDto partReplaceDto, UserInfo userInfo, ReqParam reqParam);
}
