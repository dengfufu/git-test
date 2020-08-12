package com.zjft.usp.pos.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.CoordinateUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.mapper.PosLastMapper;
import com.zjft.usp.pos.model.PosLast;
import com.zjft.usp.pos.service.PosLastService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用户最新定位服务类
 *
 * @author Qiugm
 * @date 2019-08-13 16:40
 * @version 1.0.0
 **/

@Transactional(rollbackFor = Exception.class)
@Service
public class PosLastServiceImpl extends ServiceImpl<PosLastMapper, PosLast>  implements PosLastService {
    @Resource
    private PosLastMapper posLastMapper;

    /**
     * 根据用户Id查询最新位置信息
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param userId
     * @return com.zjft.usp.pos.model.PosLast
     */
    @Override
    public PosLast findPosByUserId(Long userId) {
        return this.posLastMapper.findPosByUserId(userId);
    }

    /**
     * 添加用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param positionDto
     * @return int
     */
    @Override
    public int insertPosInfo(PositionDto positionDto) {
        return this.posLastMapper.insertPosInfo(positionDto);
    }

    /***
     * 根据用户Id删除记录
     *
     * @author Qiugm
     * @date 2019-08-22
     * @param userId
     * @return void
     */
    @Override
    public void deleteById(Long userId) {
        this.posLastMapper.deleteById(userId);
    }

    /***
     * 更新用户信息
     *
     * @author Qiugm
     * @date 2019/8/26
     * @param positionDto
     * @return int
     */
    @Override
    public int updateById(PositionDto positionDto) {
        String jsonStr = JsonUtil.toJson(positionDto);
        PosLast posLast = JsonUtil.parseObject(jsonStr, PosLast.class);
        return this.posLastMapper.updateById(posLast);
    }

    /**
     * 根据定位信息查找附近的用户
     * @param distance
     * @param lon
     * @param lat
     * @return
     */
    @Override
    public List<Long> findNearUsers(Integer distance, Double lon, Double lat) {

        List<Long> listId = new ArrayList<Long>();
        if (lon == null || lon == 0 || lat == null || lat == 0) {
            return listId;
        }
        // 算出区块范围的四角坐标
        double deltaLat = distance / 111200d;
        double deltaLon = distance / (111200d * Math.cos(lat * Math.PI / 180));

        double minLon = lon - deltaLon;
        double maxLon = lon + deltaLon;
        double minLat = lat - deltaLat;
        double maxLat = lat + deltaLat;
        // 30分钟内有定位的用户
        Date minPosTime = DateUtil.offsetMinute(DateUtil.date(), -30);

        List<PosLast> list =  this.list(new QueryWrapper<PosLast>().ge("posTime", minPosTime)
                .ge("lon", minLon).le("lon", maxLon)
                .ge("lat", minLat).le("lat", maxLat));

        if (list != null) {
            for (PosLast pos : list) {
                // 重新计算一下距离（获得圆形区域内的用户）
                double dst = CoordinateUtil.getDistance(lat, lon, pos.getLat(), pos.getLon());

                if (dst <= distance) {
                    listId.add(pos.getUserId());
                }
            }
        }
        return listId;
    }

}
