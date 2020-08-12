package com.zjft.usp.anyfix.settle.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.settle.dto.SettleStaffRecordDto;
import com.zjft.usp.anyfix.settle.filter.SettleStaffRecordFilter;
import com.zjft.usp.anyfix.settle.model.SettleStaffRecord;
import com.zjft.usp.common.model.ListWrapper;

/**
 * <p>
 * 员工结算记录 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-09-25
 */
public interface SettleStaffRecordService extends IService<SettleStaffRecord> {

    /**
     * 分页查询员工结算记录
     * @param settleStaffRecordFilter
     * @return
     */
    ListWrapper<SettleStaffRecordDto> pageByFilter(SettleStaffRecordFilter settleStaffRecordFilter);

    /**
     * 添加员工结算记录
     * @param settleStaffRecordDto
     * @param curUserId
     */
    void add(SettleStaffRecordDto settleStaffRecordDto, Long curUserId);

    /**
     * 根据记录编号删除
     * @param recordId
     */
    void deleteById(Long recordId);

}
