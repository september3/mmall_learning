package com.mmall.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * @author sunlele
 * @className PropertiesUtil
 * @date 2019/6/23 16:29
 **/
@Slf4j
public class PropertiesUtil {

    private static Properties props;

    /**
     * 要在Tomcat启动时进行配置读取，使用静态块来处理
     * 静态代码块 > 普通代码块 > 构造代码块
     * 静态代码块:在类被加载的时候执行，且只能执行一次----可用来初始化静态变量
     * */
    static {
        String fileName = "mmall.properties";
        props = new Properties();
        try {
            props.load(new InputStreamReader(PropertiesUtil.class.getClassLoader().getResourceAsStream(fileName),"UTF-8"));
        } catch (IOException e) {
            log.error("配置文件读取异常",e);
        }
    }

    public static String getProperty(String key){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            return null;
        }
        return value.trim();
    }

    public static String getProperty(String key,String defaultValue){
        String value = props.getProperty(key.trim());
        if(StringUtils.isBlank(value)){
            value = defaultValue;
        }
        return value.trim();
    }
}
