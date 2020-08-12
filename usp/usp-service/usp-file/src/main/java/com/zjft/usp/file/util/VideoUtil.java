package com.zjft.usp.file.util;

import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.MultimediaInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @description: 获取视频时长
 * @author chenxiaod
 * @date 2019/8/13 20:01
 */
@Slf4j
@Component
public class VideoUtil {

    /**
     * @Description: 获取视频时长(时分秒)
     * @date: 2019/7/30 8:35
     * @params: [file]
     */
    public static long readVideoTimeMs(File file) {
        Encoder encoder = new Encoder();
        long ms = 0;
        try {
            MultimediaInfo m = encoder.getInfo(file);
            ms = m.getDuration();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int ss = 1000;
        long second = ms / ss;
        return second;
    }
}

