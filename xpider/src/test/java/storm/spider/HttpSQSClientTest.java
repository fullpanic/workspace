package storm.spider;

import java.util.Map;

import org.junit.Test;

import storm.spider.topology.SpiderTopology;
import storm.spider.utils.HttpSQSClient;

/**
 * httpsqs test
 * @author lwying
 *
 * Sep 3, 2013
 */
public class HttpSQSClientTest {
    
    Map<String, Object> confMap = SpiderTopology.loadFromXML();
    
    HttpSQSClient client = HttpSQSClient.getClient(confMap);
    
    @Test
    public void test() {
        client.put("aaa", "a");
    }
    
    @Test
    public void testGet() {
        String string = client.get(ConfigConsts.Queue.LIST);
        System.out.println(string);
    }
}
