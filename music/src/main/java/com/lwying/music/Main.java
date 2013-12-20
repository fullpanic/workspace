package com.lwying.music;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lwying.common.ApplicationContextUtils;
import com.lwying.common.Consts;
import com.lwying.common.HttpsqsClient;
import com.lwying.music.fetch.Controller;

/**
 * main 
 * @author fullpanic
 *
 */
public class Main {
    
    private static final Logger LGO_LOGGER = Logger.getLogger(Main.class);
    
    private static final String CONF_PATH = "classpath:applicationContext.xml";
    
    private static Controller controller = null;
    
    private static ExecutorService executor = null;
    
    public static void main(String[] args) {
        ApplicationContextUtils.init(CONF_PATH);
        controller = new Controller(ApplicationContextUtils.getBean("httpsqsClient", HttpsqsClient.class));
        
        String period = ApplicationContextUtils.getProperty("config", "period");
        int time = 500;
        if (StringUtils.isNumeric(period)) {
            time = Integer.parseInt(period);
        }
        
        int size = 10;
        period = ApplicationContextUtils.getProperty("config", "size");
        if (StringUtils.isEmpty(period)) {
            size = Integer.parseInt(period);
        }
        executor = Executors.newFixedThreadPool(size);
        
        while (true) {
            try {
                JSONObject job = controller.getJob(Consts.JOB_QUEUE);
                if (job != null) {
                    WorkerThread workerThread = new WorkerThread(job, controller);
                    executor.submit(workerThread);
                    Thread.sleep(time);
                }
                else {
                    Thread.sleep(30 * 1000L);
                }
            }
            catch (Exception e) {
                LGO_LOGGER.error("run failed:", e);
            }
        }
    }
}
