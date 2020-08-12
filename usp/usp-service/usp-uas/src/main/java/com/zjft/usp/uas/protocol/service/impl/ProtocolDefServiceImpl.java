package com.zjft.usp.uas.protocol.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.uas.protocol.model.ProtocolDef;
import com.zjft.usp.uas.protocol.mapper.ProtocolDefMapper;
import com.zjft.usp.uas.protocol.service.ProtocolDefService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 协议元数据  服务实现类
 * </p>
 *
 * @author CK
 * @since 2020-06-18
 */
@Transactional
@Service
public class ProtocolDefServiceImpl extends ServiceImpl<ProtocolDefMapper, ProtocolDef> implements ProtocolDefService {

    @Override public List<ProtocolDef> listByModule(String module) {
        QueryWrapper<ProtocolDef> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("module", module);
        return this.list(queryWrapper);
    }
}
