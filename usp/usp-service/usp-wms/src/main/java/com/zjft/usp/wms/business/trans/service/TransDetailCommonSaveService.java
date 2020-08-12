package com.zjft.usp.wms.business.trans.service;

import com.zjft.usp.wms.business.trans.model.TransDetailCommonSave;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 调拨明细信息共用表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface TransDetailCommonSaveService extends IService<TransDetailCommonSave> {

    /**
     * 删除暂存明细
     *
     * @param transMainId
     */
    void removeByTransMainId(Long transMainId);

    /**
     * 获得暂存明细
     *
     * @param transMainId
     * @return
     */
    List<TransDetailCommonSave> listAllSortBy(Long transMainId);
}
