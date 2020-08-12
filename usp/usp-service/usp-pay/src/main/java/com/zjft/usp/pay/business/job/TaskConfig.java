package com.zjft.usp.pay.business.job;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.pay.business.compo.PayCompoService;
import com.zjft.usp.pay.business.enums.PaymentApplyEnum;
import com.zjft.usp.pay.business.enums.TradeConfirmEnum;
import com.zjft.usp.pay.business.model.TradeConfirm;
import com.zjft.usp.pay.business.service.TradeConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: CK
 * @create: 2020-06-02 09:19
 */
@Component
@EnableScheduling
public class TaskConfig {

    @Autowired
    PayCompoService payCompoService;

    @Autowired
    TradeConfirmService tradeConfirmService;

    public static final int POOL_SIZE = 10;

    int cpuNums = Runtime.getRuntime().availableProcessors();

    //获取当前系统的CPU 数目
    ExecutorService tradeConfirmThreadPool = Executors.newFixedThreadPool(cpuNums * POOL_SIZE, new ThreadFactory() {
        final AtomicInteger threadNumber = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            // 命名线程名称
            Thread thread = new Thread(
                    Thread.currentThread().getThreadGroup(),
                    runnable,
                    "trade-confirm-pool-" + threadNumber.getAndIncrement(),
                    0);
            // Make workers daemon threads.
            thread.setDaemon(true);
            if (thread.getPriority() != Thread.NORM_PRIORITY) {
                thread.setPriority(Thread.NORM_PRIORITY);
            }
            return thread;
        }
    });

    @Scheduled(cron = "0 0/1 * * * ?")
    public void work() {
        System.out.print("一分钟执行一次");
        List<TradeConfirm> tradeConfirmList = tradeConfirmService.list(new QueryWrapper<TradeConfirm>().eq("freq", "m"));
        tradeConfirmList.forEach((TradeConfirm tradeConfirm) -> {
            tradeConfirmThreadPool.execute(() -> {
                payCompoService.queryJob(tradeConfirm);
            });
        });
    }
}
