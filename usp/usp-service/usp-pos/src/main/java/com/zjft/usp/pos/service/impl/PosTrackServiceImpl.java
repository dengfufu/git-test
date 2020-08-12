package com.zjft.usp.pos.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.pos.dto.PositionDto;
import com.zjft.usp.pos.mapper.PosTrackMapper;
import com.zjft.usp.pos.model.PosTrack;
import com.zjft.usp.pos.service.PosTrackService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 用户定位信息服务类
 *
 * @author Qiugm
 * @date 2019-08-13 16:40
 * @version 1.0.0
 **/
@Transactional(rollbackFor = Exception.class)
@Service
public class PosTrackServiceImpl implements PosTrackService {
    @Resource
    private PosTrackMapper posTrackMapper;

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
        Calendar calendar = Calendar.getInstance();
        int posHours = calendar.get(Calendar.HOUR_OF_DAY);
        positionDto.setPosHour(posHours);
        positionDto.setPosTime(new Timestamp(System.currentTimeMillis()));
        positionDto.setPosTrackTableName(getTableName());
        try {
            return this.posTrackMapper.insertPosInfo(positionDto);
        } catch (Exception e) {
            if (e.getMessage().contains("pos_track_") && e.getMessage().contains("doesn't exist")) {
                this.createTableByTask(null, 7);
                return this.posTrackMapper.insertPosInfo(positionDto);
            } else {
                throw e;
            }
        }
    }

    /**
     * 查询用户定位信息
     *
     * @author Qiugm
     * @date 2019-08-15
     * @param positionDto
     * @return
     */
    @Override
    public List<PosTrack> listPosTrack(PositionDto positionDto) {
        positionDto.setPosTrackTableName(getTableName());
        Calendar calendar = Calendar.getInstance();
        if (StrUtil.isBlank(positionDto.getPosStartTime()) || StrUtil.isBlank(positionDto.getPosEndTime())) {
            positionDto.setPosStartTime(DateUtil.today());
            positionDto.setPosEndTime(DateUtil.today());
        }
        Date posStartDateTime = DateUtil.parse(positionDto.getPosStartTime(), "yyyy-MM-dd");
        calendar.setTime(posStartDateTime);
        int posHourStart = calendar.get(Calendar.HOUR_OF_DAY);
        positionDto.setPosHourStart(String.valueOf(posHourStart));

        Date posEndDateTime = DateUtil.parse(positionDto.getPosEndTime(), "yyyy-MM-dd");
        calendar.setTime(posEndDateTime);
        int posHourEnd = calendar.get(Calendar.HOUR_OF_DAY);
        positionDto.setPosHourEnd(String.valueOf(posHourEnd));
        return this.posTrackMapper.listPosTrack(positionDto);
    }

    /***
     * 定时创建表
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param days
     * @return void
     */
    @Override
    public void createTableByTask(String startDateTime, int days) {
        Date startDate = new Date();
        if (!StringUtils.isEmpty(startDateTime)) {
            startDate = DateUtil.parseDate(startDateTime);
        }
        for (int i = 0; i < days; i++) {
            String sortNo = DateUtil.format(DateUtils.addDays(startDate, i), "yyMMdd");
            String tableName = "pos_track_" + sortNo;
            String userIdIdx = "pos_track_" + sortNo + "_userid_IDX";
            String posTimeIdx = "pos_track_" + sortNo + "_postime_IDX";
            if (!this.existTable(tableName)) {
                this.posTrackMapper.createTable(tableName, userIdIdx, posTimeIdx);
            }
        }
    }

    /***
     * 定时删除表
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param startDateTime
     * @param days
     * @return void
     */
    @Override
    public void dropTableByTask(String startDateTime, int days, int retenDays) {
        Date startDate = new Date();
        if (!StringUtils.isEmpty(startDateTime)) {
            startDate = DateUtil.parseDate(startDateTime);
        }
        if (retenDays <= 0) {
            retenDays = 15;
        }
        //默认保留最近15天
        startDate = DateUtils.addDays(startDate, -retenDays);
        for (int i = 0; i <= days; i++) {
            String sortNo = DateUtil.format(DateUtils.addDays(startDate, -i), "yyMMdd");
            String tableName = "pos_track_" + sortNo;
            this.posTrackMapper.dropTable(tableName);
        }
    }

    /***
     * 判断表是否存在
     *
     * @author Qiugm
     * @date 2019/9/2
     * @param tableName
     * @return boolean
     */
    private boolean existTable(String tableName) {
        if (this.posTrackMapper.existTable(tableName) > 0) {
            return true;
        }
        return false;
    }

    /***
     * 获取表名
     *
     * @author Qiugm
     * @date 2019-08-15
     * @return java.lang.String
     */
    private String getTableName() {
        String tableIndex = DateUtil.format(new Date(), "yyMMdd");
        String tableName = "pos_track_" + tableIndex;
        return tableName;
    }

}
