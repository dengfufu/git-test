package com.zjft.usp.uas.wallet.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.uas.wallet.model.WalDetail;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description: 钱包明细Mapper 接口
 * @author chenxiaod
 * @date 2019/8/6 16:20
 */
public interface WalDetailMapper extends BaseMapper<WalDetail> {

    /**
     * description: 查询钱包明细
     *
     * @param userId 用户id
     * @return com.zjft.usp.uas.wallet.model.WalDetail
     */
    List<WalDetail> walDetailQuery(@Param("userId")long userId);
}
