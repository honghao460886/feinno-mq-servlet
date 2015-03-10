/**
 * Project Name:feinno-mq-servlet
 * File Name:ClusterService.java
 * Package Name:com.feinno.rocketmq.service
 * Date:Feb 26, 20154:42:17 PM
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
 * ClassName:ClusterService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Feb 26, 2015 4:42:17 PM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
@Path("/cluster")
public class ClusterMonitorService extends AbstractMonitorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(ClusterMonitorService.class);
    @GET
    @Path("/list/{namesrvaddr}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getClusterList(@PathParam("namesrvaddr") String namesrvaddr) throws Exception {
        DefaultMQAdminExt defaultMQAdminExt = getDefaultMQAdminExt();
        try {
            defaultMQAdminExt.setNamesrvAddr(namesrvaddr);
            defaultMQAdminExt.start();
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            WMQAdminExtHelper.queryClusterInfo(list, defaultMQAdminExt);
            return JSON.toJSONString(list);
        }
        catch (Exception ex) {
            LOGGER.error("ClusterMonitorService.getClusterList error :{}", ex);
            throw ex;
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }

}
