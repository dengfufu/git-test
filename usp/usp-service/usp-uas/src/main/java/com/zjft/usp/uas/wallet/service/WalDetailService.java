package com.zjft.usp.uas.wallet.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.uas.wallet.dto.WalDetailDto;
import com.zjft.usp.uas.wallet.model.WalDetail;

import java.util.List;

/**
 * @description: 服务类
 * @author chenxiaod
 * @date 2019/8/6 16:35
 */
public interface WalDetailService extends IService<WalDetail> {

    /**
     * description: 查询钱包明细
     *
     * @param userId 用户id
     * @return com.zjft.usp.uas.wallet.model.WalDetail
     */
    List<WalDetailDto> walDetailQuery(long userId);
}
