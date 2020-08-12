package com.zjft.usp.common.utils;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author: CK
 * @create: 2020-01-20 09:05
 */
@Slf4j
public class EasyExcelUtil {

    /**
     * 指定阈值
     *
     * @param consumer
     * @param threshold
     * @param <T>
     * @return
     */
    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer, int threshold) {
        return new AnalysisEventListener<T>() {

            public LinkedList<T> linkedList = new LinkedList<T>();

            @Override
            public void invoke(T t, AnalysisContext analysisContext) {
                try {
                    log.info("解析到一条数据:{}", new ObjectMapper().writeValueAsString(t));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                linkedList.add(t);
                if (linkedList.size() == threshold) {
                    consumer.accept(linkedList);
                    linkedList.clear();
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                log.info("所有数据解析完成！");
                if (linkedList.size() > 0) {
                    consumer.accept(linkedList);
                }
            }
        };
    }

    /**
     * 默认阈值
     *
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T> AnalysisEventListener<T> getListener(Consumer<List<T>> consumer) {
        return getListener(consumer, 100);
    }

}
