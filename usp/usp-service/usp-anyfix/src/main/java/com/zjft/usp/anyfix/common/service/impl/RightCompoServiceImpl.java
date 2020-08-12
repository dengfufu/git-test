package com.zjft.usp.anyfix.common.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.anyfix.common.service.RightCompoService;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.common.model.Right;
import com.zjft.usp.redis.template.RedisRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 权限聚合实现类
 *
 * @author zgpi
 * @date 2020/6/17 09:56
 */
@Service
public class RightCompoServiceImpl implements RightCompoService {

    @Resource
    private RedisRepository redisRepository;

    /**
     * 是否有权限
     *
     * @param corpId
     * @param userId
     * @param rightId
     * @return
     * @author zgpi
     * @date 2020/6/17 09:56
     **/
    @Override
    public boolean hasRight(Long corpId, Long userId, Long rightId) {
        // 系统管理员有所有权限
        List<Long> userIdList = (List<Long>) redisRepository.get(RedisRightConstants.getCorpSysRoleUserKey(corpId));
        if (CollectionUtil.isNotEmpty(userIdList) && userIdList.contains(userId)) {
            return true;
        }
        List<Long> roleIdList = (List<Long>) redisRepository.get(RedisRightConstants.getUserRoleKey(userId, corpId));
        if (CollectionUtil.isNotEmpty(roleIdList)) {
            for (Long roleId : roleIdList) {
                Map<String, List<Right>> rightMap = (Map<String, List<Right>>)
                        redisRepository.get(RedisRightConstants.getRoleRightKey(roleId));
                if (CollectionUtil.isNotEmpty(rightMap)) {
                    for (Map.Entry<String, List<Right>> entry : rightMap.entrySet()) {
                        List<Right> rightList = entry.getValue();
                        if (CollectionUtil.isNotEmpty(rightList)) {
                            List<Long> rightIdList = rightList.stream().map(e -> e.getRightId()).collect(Collectors.toList());
                            if (rightIdList.contains(rightId)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
