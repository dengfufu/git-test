package com.zjft.usp.anyfix.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.SettleBranchDto;
import com.zjft.usp.anyfix.settle.filter.SettleBranchFilter;
import com.zjft.usp.anyfix.settle.model.SettleBranch;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;

/**
 * <p>
 * 网点结算单 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleBranchService extends IService<SettleBranch> {

    /**
     * 添加服务商网点结算单
     * @param settleBranchDto
     * @param curUserId
     * @return
     */
    Result batchAddBranchSettle(SettleBranchDto settleBranchDto, Long curUserId);

    /**
     * 根据filter分页查询服务商网点结算单
     * @param filter
     * @return
     */
    ListWrapper<SettleBranchDto> pageByFilter(SettleBranchFilter filter);

    /**
     * 删除网点结算单
     * @param settleId
     */
    void deleteById(Long settleId);

}
