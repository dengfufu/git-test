package com.zjft.usp.anyfix.corp.branch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.corp.branch.filter.ServiceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务网点表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
public interface ServiceBranchMapper extends BaseMapper<ServiceBranch> {

    /**
     * 模糊查询服务网点
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 10:24
     **/
    List<ServiceBranch> matchServiceBranch(ServiceBranchFilter serviceBranchFilter);

    /**
     * 获得所有上级网点列表（包括自己）
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2020/3/9 15:59
     */
    List<ServiceBranch> listAllUpperServiceBranch(Long branchId);

    /**
     * 分页查询服务网点下级列表
     *
     * @param page
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:34
     */
    List<ServiceBranch> queryAllLowerServiceBranch(Page page,
                                                   @Param("serviceBranchFilter") ServiceBranchFilter serviceBranchFilter);

    /**
     * 分页查询服务网点同级列表
     *
     * @param page
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/10 17:09
     */
    List<ServiceBranch> querySameServiceBranch(Page page,
                                               @Param("serviceBranchFilter") ServiceBranchFilter serviceBranchFilter);


    List<ServiceBranch> selectBranchByUserId(Long userId);
}
