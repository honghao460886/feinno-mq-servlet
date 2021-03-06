/**
 * Project Name:feinno-mq-servlet
 * File Name:DataCollectionService.java
 * Package Name:com.feinno.rocketmq.monitor.service
 * Date:Mar 9, 20153:33:39 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.feinno.rocketmq.monitor.data.MQTotalRet;
import com.feinno.rocketmq.monitor.data.MQTps;
import com.feinno.rocketmq.monitor.database.DataRow;
import com.feinno.rocketmq.monitor.database.DataTable;
import com.feinno.rocketmq.monitor.database.DatabaseManager;


/**
 * ClassName:DataCollectionService <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 9, 2015 3:33:39 PM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
@Path("/data")
public class DataCollectionMonitorService extends AbstractMonitorService {

    private final static Logger LOGGER = LoggerFactory.getLogger(DataCollectionMonitorService.class);
    @Context
    HttpServletRequest request;
    
    @GET
    @Path("/total/{clustername}/{brokerid}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getTotal(@PathParam("clustername") String clustername, @PathParam("brokerid") String brokerid) {
        try {
            Date dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("dateBegin"));
            Date dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("dateEnd"));
            String sql = "SELECT BrokerID, ClusterName, Date, InTotal, NameServerAddr, OutTotal FROM MQTotalByBroker WHERE ClusterName = ? AND BrokerID = ? AND Date BETWEEN ? AND ?";
            DataTable datatable = DatabaseManager.getDatabase().executeTable(sql, clustername, brokerid, dateBegin, dateEnd);
            List<MQTotalRet> list = new ArrayList<MQTotalRet>();
            for (DataRow row : datatable.getRows()) {
                MQTotalRet total = new MQTotalRet();
                total.setBrokerId(row.getInt("BrokerID"));
                total.setClusterName(row.getString("ClusterName"));
                total.setDate(row.getDateTime("Date"));
                total.setInTotal(row.getLong("InTotal"));
                total.setNameServerAddr(row.getString("NameServerAddr"));
                total.setOutTotal(row.getLong("OutTotal"));
                list.add(total);
            }
            return JSON.toJSONString(list);
        } catch (Exception ex) {
            LOGGER.error("DataCollectionMonitorService.getTotal error :{}", ex);
            return "";
        }
    }
    
    @GET
    @Path("/tps/{clustername}/{brokerid}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getTps(@PathParam("clustername") String clustername, @PathParam("brokerid") String brokerid) {
        try {
            Date dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("dateBegin"));
            Date dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("dateEnd"));
            String sql = "SELECT BrokerID, ClusterName, Date, InTPS, NameServerAddr, OutTPS FROM MQTpsByBroker WHERE ClusterName = ? AND BrokerID = ? AND Date BETWEEN ? AND ?";
            DataTable datatable = DatabaseManager.getDatabase().executeTable(sql, clustername, brokerid, dateBegin, dateEnd);
            List<MQTps> list = new ArrayList<MQTps>();
            for (DataRow row : datatable.getRows()) {
                MQTps tps = new MQTps();
                tps.setBrokerId(row.getInt("BrokerID"));
                tps.setClusterName(row.getString("ClusterName"));
                tps.setDate(row.getDateTime("Date"));
                tps.setInTps(row.getLong("InTPS"));
                tps.setNameServerAddr(row.getString("NameServerAddr"));
                tps.setOutTps(row.getLong("OutTPS"));
                list.add(tps);
            }
            return JSON.toJSONString(list);
        } catch (Exception ex) {
            LOGGER.error("DataCollectionMonitorService.getTps error :{}", ex);
            return "";
        }
    }
    
    @GET
    @Path("/total/details/{clustername}/{brokerid}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public String getTotalDetails(@PathParam("clustername") String clustername, @PathParam("brokerid") String brokerid) {
        try {
            Date dateBegin = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("dateBegin"));
            Date dateEnd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(request.getParameter("dateEnd"));
            String sql = "SELECT BrokerID, ClusterName, Date, InTotal, NameServerAddr, OutTotal FROM MQTotalRealTimeByBroker WHERE ClusterName = ? AND BrokerID = ? AND Date BETWEEN ? AND ?";
            DataTable datatable = DatabaseManager.getDatabase().executeTable(sql, clustername, brokerid, dateBegin, dateEnd);
            List<MQTotalRet> list = new ArrayList<MQTotalRet>();
            for (DataRow row : datatable.getRows()) {
                MQTotalRet total = new MQTotalRet();
                total.setBrokerId(row.getInt("BrokerID"));
                total.setClusterName(row.getString("ClusterName"));
                total.setDate(row.getDateTime("Date"));
                total.setInTotal(row.getLong("InTotal"));
                total.setNameServerAddr(row.getString("NameServerAddr"));
                total.setOutTotal(row.getLong("OutTotal"));
                list.add(total);
            }
            return JSON.toJSONString(list);
        } catch (Exception ex) {
            LOGGER.error("DataCollectionMonitorService.getTotal error :{}", ex);
            return "";
        }
    }
}
