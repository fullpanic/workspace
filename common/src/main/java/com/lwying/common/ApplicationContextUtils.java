package com.lwying.common;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author fullpanic
 *
 */
public class ApplicationContextUtils {
    
    private static ApplicationContext ac;
    
    public static synchronized void init(String path) {
        ac = new ClassPathXmlApplicationContext(path);
    }
    
    public static String getProperty(String config, String name) {
        if (ac != null) {
            Properties properties = (Properties)ac.getBean(config);
            String value = properties.getProperty(name);
            if (StringUtils.isEmpty(value)) {
                return null;
            }
            return value.trim();
        }
        return null;
    }
    
    public static <T> T getBean(String name, Class<T> clazz) {
        if (ac != null) {
            return ac.getBean(name, clazz);
        }
        return null;
    }
}
