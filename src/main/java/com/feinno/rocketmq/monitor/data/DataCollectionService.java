/**
 * Project Name:feinno-mq-servlet
 * File Name:DataCollectionOper.java
 * Package Name:com.feinno.rocketmq.monitor.common
 * Date:Mar 5, 20155:20:24 PM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.data;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.common.protocol.body.KVTable;
import com.alibaba.rocketmq.common.protocol.route.BrokerData;
import com.alibaba.rocketmq.remoting.exception.RemotingConnectException;
import com.alibaba.rocketmq.remoting.exception.RemotingSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemotingTimeoutException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.feinno.rocketmq.monitor.database.Database;
import com.feinno.rocketmq.monitor.database.DatabaseManager;


/**
 * ClassName:DataCollectionOper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Mar 5, 2015 5:20:24 PM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class DataCollectionService {
    private final static Timer timer = new Timer("MQDataCollection_Thread", true);


    public static void start() {
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    totalStatistics();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 24 * 60 * 1000);

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                try {
                    tpsStatistics();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 0, 10 * 1000);

    }


    private static void tpsStatistics() throws Exception {

        String namesrvaddrs = getNameSrvAddr();
        for (String namesrv : namesrvaddrs.split(";")) {
            List<MQTps> list = getTps(namesrv);
            for (MQTps tps : list) {
                String sql =
                        "insert into MQTpsByBroker (Date, NameServerAddr, ClusterName, BrokerID, InTPS, OutTPS) values(?, ?, ?, ?, ?, ?)";
                Database db = DatabaseManager.getDatabase();
                int ret =
                        db.executeNonQuery(sql, tps.getDate(), tps.getNameServerAddr(), tps.getClusterName(),
                            tps.getBrokerId(), tps.getInTps(), tps.getOutTps());
                System.out.println("insert tps ret :" + ret);
            }
        }

    }


    private static List<MQTps> getTps(String namesrv) throws RemotingConnectException,
            RemotingTimeoutException, RemotingSendRequestException, InterruptedException, MQBrokerException, MQClientException {
        List<MQTps> list = new ArrayList<MQTps>();
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        try {
            defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
            defaultMQAdminExt.setNamesrvAddr(namesrv);
            defaultMQAdminExt.start();
            ClusterInfo clusterInfoSerializeWrapper = defaultMQAdminExt.examineBrokerClusterInfo();
            Iterator<Map.Entry<String, Set<String>>> itCluster =
                    clusterInfoSerializeWrapper.getClusterAddrTable().entrySet().iterator();
            while (itCluster.hasNext()) {
                Map.Entry<String, Set<String>> next = itCluster.next();
                String clusterName = next.getKey();
                TreeSet<String> brokerNameSet = new TreeSet<String>();
                brokerNameSet.addAll(next.getValue());
                for (String brokerName : brokerNameSet) {
                    BrokerData brokerData = clusterInfoSerializeWrapper.getBrokerAddrTable().get(brokerName);
                    if (brokerData != null) {
                        Iterator<Map.Entry<Long, String>> itAddr =
                                brokerData.getBrokerAddrs().entrySet().iterator();
                        while (itAddr.hasNext()) {
                            Map.Entry<Long, String> next1 = itAddr.next();
                            double in = 0;
                            double out = 0;
                            KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(next1.getValue());
                            String putTps = kvTable.getTable().get("putTps");
                            String getTransferedTps = kvTable.getTable().get("getTransferedTps");
                            {
                                String[] tpss = putTps.split(" ");
                                if (tpss != null && tpss.length > 0) {
                                    in = Double.parseDouble(tpss[0]);
                                }
                            }

                            {
                                String[] tpss = getTransferedTps.split(" ");
                                if (tpss != null && tpss.length > 0) {
                                    out = Double.parseDouble(tpss[0]);
                                }
                            }
                            MQTps tps = new MQTps();
                            tps.setBrokerId((int) next1.getKey().longValue());
                            tps.setNameServerAddr(namesrv);
                            tps.setClusterName(clusterName);
                            tps.setDate(new Date());
                            tps.setInTps((long) in);
                            tps.setOutTps((long) out);
                            list.add(tps);
                        }
                    }
                }
            }
            return list;
        } catch (Exception ex) {
            throw ex;
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }


    private synchronized static void totalStatistics() throws Exception {

        String namesrvaddrs = getNameSrvAddr();
        for (String namesrv : namesrvaddrs.split(";")) {
            List<MQTotal> list = getTotal(namesrv);
            for (MQTotal total : list) {
                String sql =
                        "insert into MQTotalByBroker (Date, NameServerAddr, ClusterName, BrokerID, InTotal, OutTotal) values(?, ?, ?, ?, ?, ?)";
                Database db = DatabaseManager.getDatabase();
                int ret =
                        db.executeNonQuery(sql, total.getDate(), total.getNameServerAddr(),
                            total.getClusterName(), total.getBrokerId(), total.getInTotal(),
                            total.getOutTotal());
                System.out.println("insert total ret :" + ret);
            }
        }
    }


    private static String getNameSrvAddr() throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        BufferedInputStream in = new BufferedInputStream(new FileInputStream("NameServerAddr.properties"));
        prop.load(in);
        String namesrvaddrs = prop.getProperty("namesrvaddrs");
        return namesrvaddrs;
    }


    private synchronized static List<MQTotal> getTotal(String namesrv) throws RemotingConnectException,
            RemotingTimeoutException, RemotingSendRequestException, InterruptedException, MQBrokerException, MQClientException {
        List<MQTotal> list = new ArrayList<MQTotal>();
        DefaultMQAdminExt defaultMQAdminExt = new DefaultMQAdminExt();
        defaultMQAdminExt.setInstanceName(Long.toString(System.currentTimeMillis()));
        defaultMQAdminExt.setNamesrvAddr(namesrv);
        defaultMQAdminExt.start();
        try {
            ClusterInfo clusterInfoSerializeWrapper = defaultMQAdminExt.examineBrokerClusterInfo();
            Iterator<Map.Entry<String, Set<String>>> itCluster =
                    clusterInfoSerializeWrapper.getClusterAddrTable().entrySet().iterator();
            while (itCluster.hasNext()) {
                Map.Entry<String, Set<String>> next = itCluster.next();
                String clusterName = next.getKey();
                TreeSet<String> brokerNameSet = new TreeSet<String>();
                brokerNameSet.addAll(next.getValue());
                for (String brokerName : brokerNameSet) {
                    BrokerData brokerData = clusterInfoSerializeWrapper.getBrokerAddrTable().get(brokerName);
                    if (brokerData != null) {

                        Iterator<Map.Entry<Long, String>> itAddr =
                                brokerData.getBrokerAddrs().entrySet().iterator();
                        while (itAddr.hasNext()) {
                            Map.Entry<Long, String> next1 = itAddr.next();
                            long InTotalYest = 0;
                            long OutTotalYest = 0;
                            KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(next1.getValue());
                            String msgPutTotalYesterdayMorning =
                                    kvTable.getTable().get("msgPutTotalYesterdayMorning");
                            String msgPutTotalTodayMorning =
                                    kvTable.getTable().get("msgPutTotalTodayMorning");
                            String msgGetTotalYesterdayMorning =
                                    kvTable.getTable().get("msgGetTotalYesterdayMorning");
                            String msgGetTotalTodayMorning =
                                    kvTable.getTable().get("msgGetTotalTodayMorning");
                            InTotalYest =
                                    Long.parseLong(msgPutTotalTodayMorning)
                                            - Long.parseLong(msgPutTotalYesterdayMorning);
                            OutTotalYest =
                                    Long.parseLong(msgGetTotalTodayMorning)
                                            - Long.parseLong(msgGetTotalYesterdayMorning);
                            MQTotal total = new MQTotal();
                            total.setBrokerId((int) next1.getKey().longValue());
                            total.setClusterName(clusterName);
                            Calendar calendar = Calendar.getInstance();
                            Date date = new Date();
                            calendar.setTime(date);
                            calendar.add(Calendar.DAY_OF_MONTH, -1);
                            date = calendar.getTime();
                            total.setDate(date);
                            total.setInTotal(InTotalYest);
                            total.setNameServerAddr(namesrv);
                            total.setOutTotal(OutTotalYest);
                            list.add(total);
                        }
                    }
                }
            }
            return list;
        }
        finally {
            defaultMQAdminExt.shutdown();
        }
    }
}