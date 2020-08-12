package com.zjft.usp.uas.right.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.mapper.SysRightScopeTypeMapper;
import com.zjft.usp.uas.right.model.SysRightScopeType;
import com.zjft.usp.uas.right.service.SysRightScopeTypeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 权限范围类型表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-3-11
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRightScopeTypeServiceImpl extends ServiceImpl<SysRightScopeTypeMapper, SysRightScopeType> implements SysRightScopeTypeService {
}
