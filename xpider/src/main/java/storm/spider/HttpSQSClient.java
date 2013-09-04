package storm.spider;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

/**
 * httpsqs client, using for get/put data with queue
 * @author lwying
 *
 * Sep 3, 2013
 */
public class HttpSQSClient {
    private static Logger logger = Logger.getLogger(HttpSQSClient.class);
    
    /**
     * String format, opt as 'get' or 'put'
     */
    private String base_url = "&name=%s&opt=%s";
    
    private static HttpSQSClient client = null;
    
    private HttpSQSClient() {
        //do nothing
    }
    
    private void init(String host, String p, String pwd) {
        base_url = "http://" + host + ":" + p + "/?auth=" + pwd + base_url;
        logger.debug("init SQS[" + base_url + "]");
    }
    
    /**
     * get client instance by singleton
     * @param sqs like authz@192.168.3.123:1218
     * @return
     */
    public static HttpSQSClient getClient(String sqs) {
        if (client == null) {
            synchronized (HttpSQSClient.class) {
                if (client == null) {
                    client = new HttpSQSClient();
                    String[] s = sqs.split("@");
                    String[] ss = s[1].split(":");
                    client.init(ss[0], ss[1], s[0]);
                }
            }
        }
        return client;
    }
    
    /**
     * get data from SQS by queueName
     * @param queueName queue name
     * @return data
     */
    public String get(String queueName) {
        URLConnection conn = null;
        BufferedReader in = null;
        StringBuffer result = new StringBuffer("");
        try {
            conn = new URL(String.format(base_url, queueName, "get")).openConnection();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String s = null;
            while ((s = in.readLine()) != null) {
                result.append(s);
            }
            in.close();
        }
        catch (Exception e) {
            logger.error("put data to SQS failed:", e);
        }
        finally {
            IOUtils.closeQuietly(in);
        }
        return result.toString();
    }
    
    /**
     * put data to queue
     * @param queueName queue name
     * @param data data
     */
    public void put(String queueName, String data) {
        String url = String.format(base_url, queueName, "put");
        logger.debug("put [" + data + "] to queue [" + url + "]");
        URLConnection conn = null;
        OutputStreamWriter os = null;
        //put data
        try {
            data = URLEncoder.encode(data, ConfigConsts.SYS_ENCODE);
            conn = new URL(url).openConnection();
            conn.setDoOutput(true);
            os = new OutputStreamWriter(conn.getOutputStream());
            os.write(data);
            os.flush();
            os.close();
        }
        catch (Exception e) {
            logger.error("put data [" + data + "] to SQS [" + queueName + "] failed:", e);
        }
        finally {
            IOUtils.closeQuietly(os);
        }
        //get response, must do it!!! otherwise can't put successfully!
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                logger.debug(line);
            }
        }
        catch (Exception e) {
            logger.error("put data [" + data + "] to SQS [" + queueName + "] failed:", e);
        }
        finally {
            IOUtils.closeQuietly(reader);
        }
    }
}
