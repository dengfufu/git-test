package com.zjft.usp.anyfix.common.service.impl;

import com.zjft.usp.anyfix.common.service.DataScopeCompoService;
import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据范围 聚合服务实现类
 *
 * @author zgpi
 * @date 2020/3/13 16:36
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DataScopeCompoServiceImpl implements DataScopeCompoService {

    @Resource
    private UasFeignService uasFeignService;

    /**
     * 获得人员范围权限列表
     *
     * @param corpId
     * @param userId
     * @param rightId
     * @return
     * @author zgpi
     * @date 2020/6/5 10:33
     **/
    @Override
    public List<RightScopeDto> listUserRightScope(Long corpId, Long userId, Long rightId) {
        List<RightScopeDto> rightScopeDtoList = new ArrayList<>();
        String json = JsonUtil.toJsonString("userId", userId,
                "corpId", corpId, "rightId", rightId);
        Result scopeResult = uasFeignService.listUserRightScope(json);
        if (Result.isSucceed(scopeResult)) {
            String data = JsonUtil.toJson(scopeResult.getData());
            rightScopeDtoList = JsonUtil.parseArray(data, RightScopeDto.class);
        }
        return rightScopeDtoList;
    }
}
