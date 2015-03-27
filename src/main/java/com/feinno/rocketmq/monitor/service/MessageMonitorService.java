/**
 * Project Name:feinno-mq-servlet
 * File Name:MessageMonitorService.java
 * Package Name:com.feinno.rocketmq.monitor.service
 * Date:Mar 27, 201510:44:12 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
*/

package com.feinno.rocketmq.monitor.service;

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
 * ClassName:MessageMonitorService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     Mar 27, 2015 10:44:12 AM <br/>
 * @author   honghao
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Path("/message")
public class MessageMonitorService extends AbstractMonitorService {
    
    private final static Logger LOGGER = LoggerFactory.getLogger(MessageMonitorService.class);
    @GET
    @Path("/id/{namesrvaddr}/{msgid}/")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String queryMsgById(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("msgid") String msgid) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            Map<String, String> map = WMQAdminExtHelper.queryById(defaultMQAdminExt, msgid);
            if (map.size() > 0) {
                return JSON.toJSONString(map);
            } else {
                return "";
            }
            
        } catch (Exception ex) {
            LOGGER.error("MessageMonitorService.queryMsgById error :{}", ex);
            return "";
        } finally {
            defaultMQAdminExt.shutdown();
        }
    }
    
    @GET
    @Path("/key/{namesrvaddr}/{key}/{topic}/")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String queryMsgByKey(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("key") String key, @PathParam("topic") String topic) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            List<Map<String, String>> list = WMQAdminExtHelper.queryByKey(defaultMQAdminExt, key, topic);
            if (list.size() > 0) {
                return JSON.toJSONString(list);
            } else {
                return "";
            }
            
        } catch (Exception ex) {
            LOGGER.error("MessageMonitorService.queryMsgByKey error :{}", ex);
            return "";
        } finally {
            defaultMQAdminExt.shutdown();
        }
    }
    
    @GET
    @Path("/offset/{namesrvaddr}/{brokername}/{topic}/{queueid}/{offset}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String queryMsgByOffset(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("brokername") String brokername, @PathParam("topic") String topic, @PathParam("queueid") String queueid, @PathParam("offset") String offset) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            Map<String, String> map = WMQAdminExtHelper.queryByOffset(defaultMQAdminExt, brokername, topic, queueid, offset);
            if (map.size() > 0) {
                return JSON.toJSONString(map); 
            } else {
                return "";
            }
            
        } catch (Exception ex) {
            LOGGER.error("MessageMonitorService.queryMsgByOffset error :{}", ex);
            return "";
        } finally {
            defaultMQAdminExt.shutdown();
        }
    }
}

