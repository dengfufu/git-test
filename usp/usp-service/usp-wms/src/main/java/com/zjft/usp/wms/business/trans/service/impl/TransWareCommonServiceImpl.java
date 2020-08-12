package com.zjft.usp.wms.business.trans.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.business.trans.dto.TransStatCountDto;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.filter.TransFilter;
import com.zjft.usp.wms.business.trans.mapper.TransWareCommonMapper;
import com.zjft.usp.wms.business.trans.model.TransWareCommon;
import com.zjft.usp.wms.business.trans.service.TransWareCommonService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 调拨信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-13
 */
@Service
public class TransWareCommonServiceImpl extends ServiceImpl<TransWareCommonMapper, TransWareCommon> implements TransWareCommonService {

    /**
     * 修改调拨单状态
     *
     * @param transWareCommon
     * @return void
     * @author Qiugm
     * @date 2019-11-22
     */
    @Override
    public void updateTransStatus(TransWareCommon transWareCommon) {
        this.updateById(transWareCommon);
    }

    @Override
    public Map<Long, TransWareCommon> mapIdAndObject(List<Long> idList) {
        Map<Long, TransWareCommon> map = new HashMap<>();
        if (CollectionUtil.isEmpty(idList)) {
            return map;
        }
        QueryWrapper<TransWareCommon> queryWrapper = new QueryWrapper<TransWareCommon>().in("id", idList);
        List<TransWareCommon> list = this.list(queryWrapper);
        list.forEach(transWareCommon -> {
            map.put(transWareCommon.getId(), transWareCommon);
        });
        return map;
    }

    /**
     * 分页查询已提交的调拨单
     *
     * @param page
     * @param transFilter
     * @return
     * @author canlei
     * @date 2019-12-09
     */
    @Override
    public List<TransWareCommonDto> queryByPage(Page page, TransFilter transFilter) {
        return this.baseMapper.queryByPage(page, transFilter);
    }

    @Override
    public List<TransStatCountDto> countByWareStatus(TransFilter transFilter) {
        return this.baseMapper.countByWareStatus(transFilter);
    }

}
