package com.zjft.usp.wms.business.trans.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.model.TransMainCommonSave;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 调拨基本信息共用表 Mapper 接口
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface TransMainCommonSaveMapper extends BaseMapper<TransMainCommonSave> {

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
    List<TransWareCommonDto> queryByPage(Page page, @Param("curUserId") Long curUserId, @Param("corpId") Long corpId);

}
