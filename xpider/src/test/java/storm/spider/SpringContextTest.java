package storm.spider;

import java.util.Properties;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * test spring config
 * @author lwying
 *
 * Sep 6, 2013
 */
public class SpringContextTest {
    
    ApplicationContext ac;
    
    @Test
    public void test() {
        ac = new ClassPathXmlApplicationContext("classpath:conf/application_config.xml");
        Properties properties = (Properties)ac.getBean("config");
        properties.get("sleep");
    }
}
