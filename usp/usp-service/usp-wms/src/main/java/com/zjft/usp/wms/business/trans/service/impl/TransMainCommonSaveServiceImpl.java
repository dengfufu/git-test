package com.zjft.usp.wms.business.trans.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.wms.business.common.enums.SaveStatusEnum;
import com.zjft.usp.wms.business.trans.dto.TransWareCommonDto;
import com.zjft.usp.wms.business.trans.model.TransMainCommonSave;
import com.zjft.usp.wms.business.trans.mapper.TransMainCommonSaveMapper;
import com.zjft.usp.wms.business.trans.service.TransMainCommonSaveService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 调拨基本信息共用表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class TransMainCommonSaveServiceImpl extends ServiceImpl<TransMainCommonSaveMapper, TransMainCommonSave> implements TransMainCommonSaveService {

    @Override
    public void updateSaveStatusToSubmit(Long transMainId) {
        TransMainCommonSave transMainCommonSave = new TransMainCommonSave();
        transMainCommonSave.setId(transMainId);
        transMainCommonSave.setSaveStatus(SaveStatusEnum.SUBMIT.getCode());
        super.updateById(transMainCommonSave);
    }

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
    @Override
    public List<TransWareCommonDto> queryByPage(Page page, Long curUserId, Long corpId) {
        return this.baseMapper.queryByPage(page, curUserId, corpId);
    }
}
