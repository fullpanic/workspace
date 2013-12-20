package com.lwying.music;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.protocol.HttpContext;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.lwying.common.ApplicationContextUtils;
import com.lwying.music.fetch.Controller;
import com.lwying.music.fetch.Fetch;

/**
 * @author fullpanic
 *
 */
public class WorkerThread implements Runnable {
    
    private static Logger logger = Logger.getLogger(WorkerThread.class);
    
    private JSONObject conf;
    
    private Controller controller;
    
    public WorkerThread(JSONObject config, Controller conl) {
        this.conf = config;
        this.controller = conl;
    }
    
    public void run() {
        String url = conf.getString("url");
        if (StringUtils.isNotEmpty(url)) {
            String username = conf.getString("username");
            String password = conf.getString("password");
            username =
                StringUtils.isEmpty(username) ? ApplicationContextUtils.getProperty("config", "username") : username;
            
            password =
                StringUtils.isEmpty(password) ? ApplicationContextUtils.getProperty("config", "password") : password;
            //check login
            HttpContext context = controller.checkUser(username);
            Fetch fetch = new Fetch(username, password, context);
            boolean rs = false;
            //need login
            if (context == null) {
                rs = fetch.login();
            }
            else {
                //check cookie validate
                CookieStore cookieStore = (CookieStore)context.getAttribute(ClientContext.COOKIE_STORE);
                if (cookieStore != null) {
                    List<Cookie> list = cookieStore.getCookies();
                    if (list != null) {
                        //check first
                        Cookie cookie = list.get(0);
                        Date date = cookie.getExpiryDate();
                        //check add one day
                        if (date.getTime() < System.currentTimeMillis() + 86400 * 1000L) {
                            //relogin
                            logger.info(username + " is expiried, relogin!");
                            rs = fetch.login();
                        }
                    }
                }
                logger.debug(username + " skip login!");
            }
            if (rs) {
                //check if replied
                Map<String, Object> map = fetch.getHiden(url);
                if (MapUtils.isEmpty(map)) {
                    rs = fetch.reply(url);
                    if (!rs) {
                        logger.error(username + " reply failed!");
                        return;
                    }
                }
                JSONObject object = new JSONObject(map);
                controller.putData(object);
            }
            else {
                logger.error(username + " login failed!");
            }
        }
    }
}
