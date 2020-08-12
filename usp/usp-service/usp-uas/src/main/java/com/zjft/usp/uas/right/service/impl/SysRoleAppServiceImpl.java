package com.zjft.usp.uas.right.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.uas.right.mapper.SysRoleAppMapper;
import com.zjft.usp.uas.right.model.SysRoleApp;
import com.zjft.usp.uas.right.service.SysRoleAppService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 角色应用表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRoleAppServiceImpl extends ServiceImpl<SysRoleAppMapper, SysRoleApp> implements SysRoleAppService {

    /**
     * 删除角色应用
     *
     * @param roleId
     * @return
     * @author zgpi
     * @date 2019/12/14 13:54
     **/
    @Override
    public void deleteByRoleId(Long roleId) {
        UpdateWrapper<SysRoleApp> updateWrapper = new UpdateWrapper<SysRoleApp>().eq("role_id", roleId);
        this.remove(updateWrapper);
    }

    /**
     * 获得企业角色与应用映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/14 14:12
     **/
    @Override
    public Map<Long, String> mapRoleAndApps(Long corpId) {
        List<Map<String, Object>> list = this.baseMapper.listRoleAndAppsMap(corpId);
        Map<Long, String> roleAndAppsMap = new HashMap<>();
        if (list != null && !list.isEmpty()) {
            for (Map<String, Object> map : list) {
                Long key = null;
                String value = null;
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    if ("role_id".equals(entry.getKey())) {
                        key = (Long) (entry.getValue());
                    } else if ("app_ids".equals(entry.getKey())) {
                        value = (String) entry.getValue();
                    }
                }
                roleAndAppsMap.put(key, value);
            }
        }
        return roleAndAppsMap;
    }
}
