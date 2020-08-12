package com.zjft.usp.anyfix.corp.branch.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.corp.branch.dto.DeviceBranchDto;
import com.zjft.usp.anyfix.corp.branch.filter.DeviceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 设备网点表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-09-24
 */
public interface DeviceBranchMapper extends BaseMapper<DeviceBranch> {

    /**
     * 查询网点信息
     *
     * @param corpIdList
     * @return
     */
    List<DeviceBranchDto> selectDeviceBranchByCorpIdList(List<String> corpIdList);

    /**
     * 模糊查询设备网点
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 10:24
     **/
    List<DeviceBranchDto> matchDeviceBranch(DeviceBranchFilter deviceBranchFilter);

    /**
     * 模糊查询相关企业的设备网点
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/2/14 16:20
     */
    List<DeviceBranchDto> matchRelateDeviceBranch(DeviceBranchFilter deviceBranchFilter);

    /**
     * 分页查询设备网点
     *
     * @param page
     * @param deviceBranchFilter
     * @return
     */
    List<DeviceBranch> queryDeviceBranch(Page page,
                                         @Param("deviceBranchFilter") DeviceBranchFilter deviceBranchFilter);

    /**
     * 查询上级网点列表
     * @param deviceBranchFilter
     * @return
     */
    List<DeviceBranch> selectUpperBranchList(DeviceBranchFilter deviceBranchFilter);

    /**
     * 查询服务商的设备网点
     * @param deviceBranchFilter
     * @return
     */
    List<DeviceBranchDto> queryDeviceBranchForService(Page page,
                                                      @Param("deviceBranchFilter")DeviceBranchFilter deviceBranchFilter);
}
