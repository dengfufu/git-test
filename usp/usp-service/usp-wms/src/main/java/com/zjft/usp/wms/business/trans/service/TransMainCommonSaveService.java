package com.zjft.usp.wms.business.trans.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.model.TransMainCommonSave;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 调拨基本信息共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface TransMainCommonSaveService extends IService<TransMainCommonSave> {

    /**
     * 修改保存状态为已提交
     *
     * @param transMainId
     */
    void updateSaveStatusToSubmit(Long transMainId);

    /**
     * 分页查询保存的调拨单
     *
     * @author canlei
     * @date 2019-12-09
     * @param page
     * @param curUserId
     * @param corpId
     * @return
     */
    List<TransWareCommonDto> queryByPage(Page page, Long curUserId, Long corpId);
}
