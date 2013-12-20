package com.lwying.fetch.control;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.lwying.common.CommonUtils;
import com.lwying.fetch.service.TaskService;

/**
 * @author fullpanic
 *
 */
@Controller
public class TaskController {
    
    private static Logger logger = Logger.getLogger(TaskController.class);
    
    @Autowired
    private TaskService service;
    
    @RequestMapping(value = "login.do", produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public ModelAndView checkUser(@RequestParam(required = true) String username,
        @RequestParam(required = true) String password) {
        logger.debug("username:" + username + ",passwd:" + password + " start login.");
        boolean rs = service.checkUser(username, password);
        if (!rs) {
            logger.info(username + " login failed.");
            return new ModelAndView("error", "info", "login failed!");
        }
        else {
            logger.info(username + " login successfully.");
            //store
            CommonUtils.userMap.put(username, true);
            return new ModelAndView("task", "list", service.getAllTasks());
        }
    }
    
    @RequestMapping(value = "send.do", produces = {"text/plain;charset=UTF-8"})
    @ResponseBody
    public String send(@RequestParam(required = true) String id) {
        logger.debug("send task:" + id);
        boolean rs = service.sendJob(id);
        return String.valueOf(rs);
    }
}
