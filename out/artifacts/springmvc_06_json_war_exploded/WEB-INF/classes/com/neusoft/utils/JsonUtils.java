package com.neusoft.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.text.SimpleDateFormat;

public class JsonUtils {

    public static String getJson(Object object){
        return getJson(object, "yyyy-MM-dd HH:mm:ss");
    }

    // 静态方法，不需要创建对象即可使用
    public static String getJson(Object object, String dateFormat) {
        // ObjectMapper,时间解析后默认格式为 Timestamp 时间戳
        ObjectMapper mapper = new ObjectMapper();
        // 自定义日期格式，
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            // 将默认时间戳方式设置为false
            mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
            mapper.setDateFormat(sdf);
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return e.toString();
        }
    }
}
