package com.lwying.music.fetch;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lwying.common.Consts;
import com.lwying.common.HttpsqsClient;

public class Controller {
    
    private static Logger logger = Logger.getLogger(Controller.class);
    
    private HttpsqsClient client;
    
    private Map<String, HttpContext> users = new HashMap<String, HttpContext>();
    
    public Controller(HttpsqsClient sqs) {
        this.client = sqs;
    }
    
    public JSONObject getJob(String queue) {
        String data = client.get(queue);
        JSONObject object = null;
        try {
            if (!client.isEnd(data)) {
                object = JSON.parseObject(data);
            }
            else {
                logger.debug("queue:" + queue + " is get over!");
            }
        }
        catch (Exception e) {
            logger.error("get job failed, data[" + data + "]:", e);
        }
        return object;
    }
    
    public void putData(JSONObject obj) {
        if (obj != null) {
            String data = obj.toJSONString();
            client.put(Consts.CRAWL_QUEUE, data);
        }
    }
    
    public synchronized void putUser(String user, HttpContext context) {
        this.users.put(user, context);
    }
    
    public synchronized HttpContext checkUser(String user) {
        return this.users.get(user);
    }
    
}
