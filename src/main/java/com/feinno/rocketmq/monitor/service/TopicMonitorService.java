/**
 * Project Name:feinno-mq-servlet
 * File Name:TopicService.java
 * Package Name:com.feinno.rocketmq.service
 * Date:Feb 26, 20154:42:04 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
*/

package com.feinno.rocketmq.monitor.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.feinno.rocketmq.monitor.common.WMQAdminExtHelper;

/**
 * ClassName:TopicService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     Feb 26, 2015 4:42:04 PM <br/>
 * @author   honghao
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Path("/topic")
public class TopicMonitorService extends AbstractMonitorService {
    private final static Logger LOGGER = LoggerFactory.getLogger(TopicMonitorService.class);
    @GET
    @Path("/list/{namesrvaddr}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String queryClusterList(@PathParam("namesrvaddr") String namesrvaddr) throws Exception {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            WMQAdminExtHelper.queryTopicList(list, defaultMQAdminExt);
            return JSON.toJSONString(list);
        }
        catch (Exception ex) {
            LOGGER.error("TopicMonitorService.queryClusterList error :{}", ex);
            throw ex;
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
    
    @GET
    @Path("/route/{namesrvaddr}/{topicname}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String queryTopicRoute(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("topicname") String topicname) throws Exception {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            String json = WMQAdminExtHelper.queryTopicRoute(topicname, defaultMQAdminExt);
            return json;
        }
        catch (Exception ex) {
            LOGGER.error("TopicMonitorService.queryTopicRoute error :{}", ex);
            throw ex;
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }

}

