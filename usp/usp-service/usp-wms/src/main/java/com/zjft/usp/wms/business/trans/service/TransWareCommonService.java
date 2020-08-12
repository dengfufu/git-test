package com.zjft.usp.wms.business.trans.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.trans.dto.TransStatCountDto;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.filter.TransFilter;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 调拨信息共用表服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
public interface TransWareCommonService extends IService<TransWareCommon> {

    /**
     * 修改调拨单状态
     *
     * @param transWareCommon
     * @return void
     * @author Qiugm
     * @date 2019-11-22
     */
    void updateTransStatus(TransWareCommon transWareCommon);

    /**
     * 获取id和object的map
     *
     * @param idList
     * @return java.util.Map<java.lang.Long, com.zjft.usp.wms.business.trans.model.TransWareCommon>
     * @throws
     * @author zphu
     * @date 2019/12/6 16:31
     **/
    Map<Long, TransWareCommon> mapIdAndObject(List<Long> idList);

    /**
     * 分页查询已提交的调拨单
     *
     * @param page
     * @param transFilter
     * @return
     * @author canlei
     * @date 2019-12-09
     */
    List<TransWareCommonDto> queryByPage(Page page, TransFilter transFilter);


    /**
     * 查询调拨单各状态对应的数量
     * @param transFilter
     * @return
     */
    List<TransStatCountDto> countByWareStatus(TransFilter transFilter);

}
