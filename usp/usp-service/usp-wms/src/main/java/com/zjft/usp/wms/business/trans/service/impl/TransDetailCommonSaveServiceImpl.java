package com.zjft.usp.wms.business.trans.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.business.trans.model.TransDetailCommonSave;
import com.zjft.usp.wms.business.trans.mapper.TransDetailCommonSaveMapper;
import com.zjft.usp.wms.business.trans.service.TransDetailCommonSaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 调拨明细信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class TransDetailCommonSaveServiceImpl extends ServiceImpl<TransDetailCommonSaveMapper, TransDetailCommonSave> implements TransDetailCommonSaveService {

    @Override
    public void removeByTransMainId(Long transMainId) {
        if (LongUtil.isZero(transMainId)) {
            throw new AppException("暂存单传值错误，无法删除！");
        }
        this.remove(new QueryWrapper<TransDetailCommonSave>().eq("trans_id", transMainId));
    }

    @Override
    public List<TransDetailCommonSave> listAllSortBy(Long transMainId) {
        QueryWrapper<TransDetailCommonSave> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("trans_id", transMainId);
        queryWrapper.orderByAsc("id");
        List<TransDetailCommonSave> list = this.list(queryWrapper);
        return list;
    }


}
