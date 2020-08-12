package com.zjft.usp.uas.wallet.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.wallet.dto.WalDetailDto;
import com.zjft.usp.uas.wallet.mapper.WalDetailMapper;
import com.zjft.usp.uas.wallet.model.WalDetail;
import com.zjft.usp.uas.wallet.service.WalDetailService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 服务实现类
 * @author chenxiaod
 * @date 2019/8/6 16:57
 */
@Service
public class WalDetailServiceImpl extends ServiceImpl<WalDetailMapper, WalDetail> implements WalDetailService {

    @Resource
    private WalDetailMapper walDetailMapper;

    @Override
    public List<WalDetailDto> walDetailQuery(long userId){
        List<WalDetail> list = walDetailMapper.walDetailQuery(userId);
        List<WalDetailDto> walDetailDtolist = new ArrayList<>();
        for (WalDetail walDetail:list) {
            WalDetailDto walDetailDto = new WalDetailDto();
            BeanUtils.copyProperties(walDetail,walDetailDto);
            walDetailDtolist.add(walDetailDto);
        }
        return walDetailDtolist;
    }
}
