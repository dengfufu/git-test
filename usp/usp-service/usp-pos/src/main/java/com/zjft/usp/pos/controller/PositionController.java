package com.zjft.usp.pos.controller;

import cn.hutool.core.date.DateUtil;
import com.zjft.usp.common.annotation.LoginUser;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.pos.dto.PosJobDto;
import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.model.PosLast;
import com.zjft.usp.pos.model.PosTrack;
import com.zjft.usp.pos.service.PosLastService;
import com.zjft.usp.pos.service.PosService;
import com.zjft.usp.pos.service.PosTrackService;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 定位管理控制层
 *
 * @author Qiugm
 * @date 2019-08-13 16:37
 * @version 1.0.0
 **/
@RequestMapping(value = "/position")
@RestController
public class PositionController {
    @Autowired
    private PosService posService;

    @Autowired
    private PosLastService posLastService;

    @Autowired
    private PosTrackService posTrackService;

    /**
     * 上传用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @param positionDto
     * @return java.lang.String
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public Result<Object> uploadLocation(@RequestBody PositionDto positionDto, @LoginUser UserInfo userInfo) throws Exception {
        posService.uploadLocation(positionDto, userInfo);
        return Result.succeed("上传定位成功！");
    }

    /**
     * 上传用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @return java.lang.String
     */
    @RequestMapping(value = "/findPosByUserId", method = RequestMethod.POST)
    public Result<PosLast> findPosByUserId(@LoginUser UserInfo userInfo) {
        PosLast posLast = posLastService.findPosByUserId(userInfo.getUserId());
        return Result.succeed(posLast);
    }

    /**
     * 查询用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @return
     */
    @RequestMapping(value = "/listPosTrack", method = RequestMethod.POST)
    public Result<List<PosTrack>> listPosTrack(@RequestBody PositionDto positionDto) {
        List<PosTrack> posTrackList = posTrackService.listPosTrack(positionDto);
        return Result.succeed(posTrackList);
    }

    /**
     * 查询用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-13
     * @return
     */
    @RequestMapping(value = "/initQueryPosTrackData", method = RequestMethod.POST)
    public Result<Map> initQueryPosTrackData() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        resultMap.put("posStartTime", DateUtil.format(DateUtils.addHours(new Date(), -3), "yyyy-MM-dd HH:mm:ss"));
        resultMap.put("posEndTime", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss"));
        return Result.succeed(resultMap);
    }

    /**
     * 根据定位信息查找附近的用户
     * @param distance
     * @param lon
     * @param lat
     * @return
     */
    @RequestMapping(value = "/feign/findNearUsers", method = RequestMethod.POST)
    public Result<List<Long>> findNearEngineers(@RequestParam("distance") Integer distance,
                                         @RequestParam("lon") Double lon,
                                         @RequestParam("lat") Double lat) {
        List<Long> listId = this.posLastService.findNearUsers(distance, lon, lat);
        return Result.succeed(listId);
    }

    /**
     * 定时创建位置表
     * @date 2020/1/20
     * @param posJobDto dto
     * @return com.zjft.usp.common.model.Result<java.util.List<com.zjft.usp.pos.model.PosTrack>>
     */
    @RequestMapping(value = "/createPos", method = RequestMethod.POST)
    public Result createPos(@RequestBody PosJobDto posJobDto) {
        posTrackService.createTableByTask(posJobDto.getStartDateTime(),posJobDto.getDays());
        return Result.succeed();
    }
}
