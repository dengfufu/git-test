package com.zjft.usp.device.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Date;

/**
 * date类型转换为long
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/17 3:34 下午
 **/
@Configuration
public class DateToLongSerializer extends JsonSerializer<Date> {

    @Override
    public void serialize(Date date, JsonGenerator jsonGenerator,
                          SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(date.getTime());
    }
}