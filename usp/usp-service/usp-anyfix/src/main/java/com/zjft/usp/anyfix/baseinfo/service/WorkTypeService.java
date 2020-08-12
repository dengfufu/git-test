package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.dto.WorkTypeDto;
import com.zjft.usp.anyfix.baseinfo.filter.WorkTypeFilter;
import com.zjft.usp.anyfix.baseinfo.model.WorkType;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工单类型表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface WorkTypeService extends IService<WorkType> {

    /**
     * 获得工单类型列表
     * @author zgpi
     * @date 2019/10/11 9:01 上午
     * @param workTypeFilter
     * @return
     **/
    List<WorkType> listWorkType(WorkTypeFilter workTypeFilter);

    /**
     * 获得工单类型映射
     * @author zgpi
     * @date 2019/10/12 3:40 下午
     * @return
     **/
    Map<Integer, String> mapWorkType();
    
    /**
     * 获得客户的工单类型映射
     * @author zgpi
     * @date 2019/10/12 3:40 下午
     * @param customCorp
     * @return
     **/
    Map<Integer, String> mapWorkType(Long customCorp);

    /**
     * 获得客户的工单类型映射
     * @author zgpi
     * @date 2019/10/29 5:22 下午
     * @param customCorp
     * @return
     **/
    Map<Integer, WorkType> mapWorkTypeObj(Long customCorp);

    /**
     * 根据企业编号列表获取工单类型映射
     * @param corpIdList
     * @return
     */
    Map<Integer, String> mapWorkTypeByCorpIdList(List<Long> corpIdList);

    /**
     * 分页查询
     * @param workTypeFilter
     * @return
     */
    ListWrapper<WorkTypeDto> query(WorkTypeFilter workTypeFilter);



    /**
     * 保存工单类型
     * @param workType
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(WorkType workType, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改工单类型
     * @param workType
     * @param userInfo
     * @return
     */
    void update(WorkType workType, UserInfo userInfo);
}
