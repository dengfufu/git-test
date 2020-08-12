package com.zjft.usp.anyfix.utils;

import cn.hutool.core.collection.CollectionUtil;
import com.zjft.usp.common.constant.RedisRightConstants;
import com.zjft.usp.redis.template.RedisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author zgpi
 * @version 1.0
 * @date 2019/12/17 08:58
 */
@Component
public class RightUtil {

    @Autowired
    private RedisRepository redisRepository;

    /**
     * 是否有权限
     *
     * @param userId
     * @param corpId
     * @param rightId
     * @return
     * @author zgpi
     * @date 2019/12/17 08:59
     **/
    public boolean hasUserRight(Long userId, Long corpId, Long rightId) {
        List<Long> rightIdList = (List<Long>) this.redisRepository
                .get(RedisRightConstants.getRightUserRightId(corpId, userId));
        if (CollectionUtil.isEmpty(rightIdList)) {
            return false;
        }
        if (rightIdList.contains(rightId)) {
            return true;
        }
        return false;
    }
}
