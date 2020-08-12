package com.zjft.usp.uas.right.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.mapper.SysUserRightScopeMapper;
import com.zjft.usp.uas.right.model.SysUserRightScope;
import com.zjft.usp.uas.right.service.SysUserRightScopeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 人员范围权限表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-06-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysUserRightScopeServiceImpl extends ServiceImpl<SysUserRightScopeMapper, SysUserRightScope> implements SysUserRightScopeService {

    /**
     * 删除人员范围权限
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/4 15:59
     **/
    @Override
    public void delUserRightScopeByUserId(Long userId, Long corpId) {
        this.remove(new UpdateWrapper<SysUserRightScope>().eq("corp_id", corpId)
                .eq("user_id", userId));
    }
}
