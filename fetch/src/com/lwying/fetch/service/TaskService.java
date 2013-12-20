package com.lwying.fetch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSONObject;
import com.lwying.common.CommonUtils;
import com.lwying.common.Consts;
import com.lwying.common.HttpsqsClient;
import com.lwying.common.SQLiteDBUtils;

/**
 * @author fullpanic
 *
 */
@Service
public class TaskService {
    
    @Autowired
    private SQLiteDBUtils dbUtil;
    
    @Autowired
    private HttpsqsClient httpsqsClient;
    
    public boolean checkUser(String username, String password) {
        boolean rs = false;
        if (!CommonUtils.userMap.containsKey(username)) {
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            String sql = "select password from tab_tasks where username=?";
            list = SQLiteDBUtils.query(sql, username);
            if (!CollectionUtils.isEmpty(list)) {
                String passwd = (String)list.get(0).get("password");
                passwd = Base64.encodeBase64String(passwd.getBytes());
                passwd = CommonUtils.MD5(passwd);
                rs = StringUtils.equalsIgnoreCase(password, passwd);
            }
        }
        else {
            rs = CommonUtils.userMap.get(username);
        }
        return rs;
    }
    
    public List<Map<String, Object>> getAllTasks() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "select * from tab_tasks where type=?";
        list = SQLiteDBUtils.query(sql, Consts.TaskType.LIST);
        return list;
    }
    
    public boolean sendJob(String id) {
        boolean rs = false;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        String sql = "select * from tab_tasks where id=?";
        list = SQLiteDBUtils.query(sql, id);
        if (!CollectionUtils.isEmpty(list)) {
            Map<String, Object> ob = list.get(0);
            JSONObject job = new JSONObject(ob);
            String line = httpsqsClient.put(Consts.JOB_QUEUE, job.toJSONString());
            rs = StringUtils.isNotEmpty(line);
        }
        return rs;
    }
}
