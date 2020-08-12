package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.dto.CustomReasonDto;
import com.zjft.usp.anyfix.baseinfo.filter.CustomReasonFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomReason;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 客户原因表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface CustomReasonService extends IService<CustomReason> {

    /**
     * 根据客户企业编号和原因类型查询列表
     * @param customCorp
     * @param reasonType
     * @return
     */
    List<CustomReason> selectByCorpAndType(Long  customCorp, Integer reasonType);

    /**
     * 分页查询客户原因列表
     *
     * @param customReasonFilter
     * @return
     * @author zgpi
     * @date 2019/11/18 11:21
     **/
    ListWrapper<CustomReasonDto> query(CustomReasonFilter customReasonFilter);

    /**
     * 保存客户原因
     * @param customReason
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(CustomReason customReason, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改客户端原因
     * @param customReason
     * @param userInfo
     * @return
     */
    void update(CustomReason customReason, UserInfo userInfo);
}
