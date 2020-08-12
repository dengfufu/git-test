package com.zjft.usp.uas.right.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.mapper.SysRightExtraCorpMapper;
import com.zjft.usp.uas.right.model.SysRightExtraCorp;
import com.zjft.usp.uas.right.service.SysRightExtraCorpService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 权限点的具体企业配置，与租户性质互补
 * @author: CK
 * @create: 2020-04-10 15:18
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRightExtraCorpMapperImpl extends ServiceImpl<SysRightExtraCorpMapper, SysRightExtraCorp> implements SysRightExtraCorpService {
}
