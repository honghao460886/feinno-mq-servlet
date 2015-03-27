/**
 * Project Name:feinno-mq-servlet
 * File Name:ProducerMonitorService.java
 * Package Name:com.feinno.rocketmq.monitor.service
 * Date:Feb 28, 20158:21:56 PM
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
 * ClassName:ProducerMonitorService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason:	 TODO ADD REASON. <br/>
 * Date:     Feb 28, 2015 8:21:56 PM <br/>
 * @author   honghao
 * @version  
 * @since    JDK 1.7
 * @see 	 
 */
@Path("/consumer")
public class ConsumerMonitorService extends AbstractMonitorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConsumerMonitorService.class);
    @GET
    @Path("/progress/{namesrvaddr}/{group}/")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String consumerProgress(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("group") String group) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            WMQAdminExtHelper.consumerProgress(group, defaultMQAdminExt, list);
            return JSON.toJSONString(list);
        } catch (Exception ex) {
            LOGGER.error("ConsumerMonitorService.consumerProgress error :{}", ex);
            return "";
        } finally {
            defaultMQAdminExt.shutdown();
        }
    }
    
    @GET
    @Path("/addsubgroup/{namesrvaddr}/{clustername}/{groupname}/")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String createSubGroup(@PathParam("namesrvaddr") String namesrvaddr, @PathParam("clustername") String clustername, @PathParam("groupname") String groupname) {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            Map<String, String> map = WMQAdminExtHelper.createSubGroup(defaultMQAdminExt, clustername, groupname);
            return JSON.toJSONString(map);
        } catch (Exception ex) {
            LOGGER.error("ConsumerMonitorService.createSubGroup error :{}", ex);
            return "";
        } finally {
            defaultMQAdminExt.shutdown();
        }
    }
}

