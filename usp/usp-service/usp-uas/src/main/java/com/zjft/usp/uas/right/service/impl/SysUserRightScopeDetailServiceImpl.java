package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.mapper.SysUserRightScopeDetailMapper;
import com.zjft.usp.uas.right.model.SysUserRightScope;
import com.zjft.usp.uas.right.model.SysUserRightScopeDetail;
import com.zjft.usp.uas.right.service.SysUserRightScopeDetailService;
import com.zjft.usp.uas.right.service.SysUserRightScopeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
public class SysUserRightScopeDetailServiceImpl extends ServiceImpl<SysUserRightScopeDetailMapper, SysUserRightScopeDetail> implements SysUserRightScopeDetailService {

    @Autowired
    private SysUserRightScopeService sysUserRightScopeService;

    /**
     * 删除人员范围权限
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/6/4 16:03
     **/
    @Override
    public void delUserRightScopeDetail(Long userId, Long corpId) {
        List<SysUserRightScope> sysUserRightScopeList = sysUserRightScopeService.list(
                new QueryWrapper<SysUserRightScope>().eq("user_id", userId)
                        .eq("corp_id", corpId)
        );
        List<Long> idList = sysUserRightScopeList.stream().map(e -> e.getId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(idList)) {
            this.removeByIds(idList);
        }
    }
}
