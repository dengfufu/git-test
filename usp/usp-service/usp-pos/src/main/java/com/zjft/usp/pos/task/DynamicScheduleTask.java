package com.zjft.usp.pos.task;

import com.alibaba.druid.util.StringUtils;
import com.zjft.usp.pos.service.PosTrackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

/**
 * 定时任务
 *
 * @author Qiugm
 * @date 2019/9/2 10:18
 * @version 1.0.0
 **/
@Component
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class DynamicScheduleTask implements SchedulingConfigurer {
    @Autowired
    PosTrackService posTrackService;

    /*@Mapper
    public interface CronMapper {
        @Select("select cron from cron limit 1")
        public String getCron();
    }

    @Autowired      //注入mapper
    @SuppressWarnings("all")
    CronMapper cronMapper;*/

    /**
     * 执行定时任务.
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> posTrackService.createTableByTask(null, 31),
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    // String cron = cronMapper.getCron();
                    //String cron = "* 19 11 02 09 ?";
                    String cron1 = "* 35 14 02 09 ?";
                    //2.2 合法性校验.
                    if (StringUtils.isEmpty(cron1)) {
                        // Omitted Code ..
                    }
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(cron1).nextExecutionTime(triggerContext);
                }
        );
    }
}
