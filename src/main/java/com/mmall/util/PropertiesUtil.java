package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @Author daweizai
 * @Date 20:54 2020/6/10
 * @ClassName PropertiesUtil
 * @Version 1.0
 **/
@Slf4j
public class PropertiesUtil {

    private static Properties properties;

    static {

        //获取配置文件中的信息
        String fileName = "mmall.properties";
        properties = new Properties();
        try {
            properties.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName), "utf-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常", e);
            e.printStackTrace();
        }
    }


    public static String getProperty(String key) {
        String value = properties.getProperty(key.trim());
        if (StringUtils.isBlank(value)) {
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key, String defaultValue) {
        String value = properties.getProperty(key);
        if (StringUtils.isBlank(value)) {
            return defaultValue;
        }
        return value.trim();
    }
}
