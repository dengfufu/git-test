package com.zjft.usp.pos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.service.PosLastService;
import com.zjft.usp.pos.service.PosService;
import com.zjft.usp.pos.service.PosTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 定位服务类
 *
 * @author Qiugm
 * @date 2019-08-15 15:43
 * @version 1.0.0
 **/
@Transactional(rollbackFor = Exception.class)
@Service
public class PosServiceImpl implements PosService {
    @Autowired
    private PosLastService posLastService;
    @Autowired
    private PosTrackService posTrackService;

    /**
     * 添加用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param positionDto
     * @param userInfo
     * @return int
     * @throws Exception
     */
    @Override
    public int uploadLocation(PositionDto positionDto, UserInfo userInfo) throws Exception {
        positionDto.setUserId(userInfo.getUserId());
        positionDto.setPosTime(DateUtil.parse(DateUtil.now()));
        int updateFlag = posLastService.updateById(positionDto);
        if (updateFlag != 1) {
            posLastService.insertPosInfo(positionDto);
        }
        posTrackService.insertPosInfo(positionDto);
        return 1;
    }
}
