package storm.spider;

import org.junit.Test;

/**
 * httpsqs test
 * @author lwying
 *
 * Sep 3, 2013
 */
public class HttpSQSClientTest {
    
    HttpSQSClient client = HttpSQSClient.getClient("123456@192.168.3.245:1218");
    
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
