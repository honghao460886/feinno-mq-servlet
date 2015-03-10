/**
 * Project Name:feinno-mq-servlet
 * File Name:WMQAdminExtHelper.java
 * Package Name:com.feinno.rocketmq.monitor.common
 * Date:Feb 28, 20159:48:58 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.common.UtilAll;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.common.protocol.body.GroupList;
import com.alibaba.rocketmq.common.protocol.body.KVTable;
import com.alibaba.rocketmq.common.protocol.body.TopicList;
import com.alibaba.rocketmq.common.protocol.route.BrokerData;
import com.alibaba.rocketmq.common.protocol.route.TopicRouteData;
import com.alibaba.rocketmq.remoting.exception.RemotingConnectException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.remoting.exception.RemotingSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemotingTimeoutException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.feinno.rocketmq.monitor.cluster.bean.WClusterListKeys;
import com.feinno.rocketmq.monitor.topic.bean.WTopicListKeys;


/**
 * ClassName:WMQAdminExtHelper <br/>
 * Function: TODO ADD FUNCTION. <br/>
 * Reason: TODO ADD REASON. <br/>
 * Date: Feb 28, 2015 9:48:58 AM <br/>
 * 
 * @author honghao
 * @version
 * @since JDK 1.7
 * @see
 */
public class WMQAdminExtHelper {

    private final static Logger LOGGER = LoggerFactory.getLogger(WMQAdminExtHelper.class);
    /**
     * queryClusterInfo:(这里用一句话描述这个方法的作用). <br/>
     * TODO(这里描述这个方法适用条件 – 可选).<br/>
     * TODO(这里描述这个方法的执行流程 – 可选).<br/>
     * TODO(这里描述这个方法的使用方法 – 可选).<br/>
     * TODO(这里描述这个方法的注意事项 – 可选).<br/>
     *
     * @author honghao
     * @param list
     * @param defaultMQAdminExt
     * @throws RemotingConnectException
     * @throws RemotingTimeoutException
     * @throws RemotingSendRequestException
     * @throws InterruptedException
     * @throws MQBrokerException
     * @since JDK 1.7
     */
    public static void queryClusterInfo(List<Map<String, String>> list, DefaultMQAdminExt defaultMQAdminExt)
            throws RemotingConnectException, RemotingTimeoutException, RemotingSendRequestException,
            InterruptedException, MQBrokerException {
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
                        String version = "";
                        long InTotalYest = 0;
                        long OutTotalYest = 0;
                        long InTotalToday = 0;
                        long OutTotalToday = 0;

                        try {
                            KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(next1.getValue());
                            String putTps = kvTable.getTable().get("putTps");
                            String getTransferedTps = kvTable.getTable().get("getTransferedTps");
                            version = kvTable.getTable().get("brokerVersionDesc");
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
                            String msgPutTotalYesterdayMorning =
                                    kvTable.getTable().get("msgPutTotalYesterdayMorning");
                            String msgPutTotalTodayMorning =
                                    kvTable.getTable().get("msgPutTotalTodayMorning");
                            String msgPutTotalTodayNow = kvTable.getTable().get("msgPutTotalTodayNow");
                            String msgGetTotalYesterdayMorning =
                                    kvTable.getTable().get("msgGetTotalYesterdayMorning");
                            String msgGetTotalTodayMorning =
                                    kvTable.getTable().get("msgGetTotalTodayMorning");
                            String msgGetTotalTodayNow = kvTable.getTable().get("msgGetTotalTodayNow");

                            InTotalYest =
                                    Long.parseLong(msgPutTotalTodayMorning)
                                            - Long.parseLong(msgPutTotalYesterdayMorning);
                            OutTotalYest =
                                    Long.parseLong(msgGetTotalTodayMorning)
                                            - Long.parseLong(msgGetTotalYesterdayMorning);

                            InTotalToday =
                                    Long.parseLong(msgPutTotalTodayNow)
                                            - Long.parseLong(msgPutTotalTodayMorning);
                            OutTotalToday =
                                    Long.parseLong(msgGetTotalTodayNow)
                                            - Long.parseLong(msgGetTotalTodayMorning);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put(WClusterListKeys.CLUSTER_NAME, clusterName);
                            map.put(WClusterListKeys.BROKER_NAME, brokerName);
                            map.put(WClusterListKeys.BROKER_ID, String.valueOf(next1.getKey().longValue()));
                            map.put(WClusterListKeys.BROKER_ADDR, next1.getValue());
                            map.put(WClusterListKeys.VERTION, version);
                            map.put(WClusterListKeys.IN_TPS, String.valueOf((int) in));
                            map.put(WClusterListKeys.OUT_TPS, String.valueOf((int) out));
                            map.put(WClusterListKeys.IN_TOTAL_YEST, String.valueOf(InTotalYest));
                            map.put(WClusterListKeys.OUT_TOTAL_YEST, String.valueOf(OutTotalYest));
                            map.put(WClusterListKeys.IN_TOTAL_TODAY, String.valueOf(InTotalToday));
                            map.put(WClusterListKeys.OUT_TOTAL_TODAY, String.valueOf(OutTotalToday));
                            list.add(map);
                        }
                        catch (Exception e) {
                            LOGGER.error("query cluster info fetchBrokerRuntimeStats error :{}", e);
                        }
                    }
                }
            }
        }
    }


    public static void queryTopicList(List<Map<String, String>> list, DefaultMQAdminExt defaultMQAdminExt)
            throws RemotingException, MQClientException, InterruptedException, MQBrokerException {

        ClusterInfo clusterInfo = defaultMQAdminExt.examineBrokerClusterInfo();
        TopicList topicList = defaultMQAdminExt.fetchAllTopicList();
        for (String topic : topicList.getTopicList()) {
            if (topic.startsWith(MixAll.RETRY_GROUP_TOPIC_PREFIX)
                    || topic.startsWith(MixAll.DLQ_GROUP_TOPIC_PREFIX)) {
                continue;
            }

            String clusterName = "";
            GroupList groupList = new GroupList();

            try {
                clusterName = findTopicBelongToWhichCluster(topic, clusterInfo, defaultMQAdminExt);
                groupList = defaultMQAdminExt.queryTopicConsumeByWho(topic);
            }
            catch (Exception e) {
                LOGGER.error("queryTopicList's findTopicBelongToWhichCluster error :{}", e);
            }

            if (null == groupList || groupList.getGroupList().isEmpty()) {
                groupList = new GroupList();
                groupList.getGroupList().add("");
            }

            for (String group : groupList.getGroupList()) {
                Map<String, String> map = new HashMap<String, String>();
                map.put(WTopicListKeys.CLUSTER_NAME, UtilAll.frontStringAtLeast(clusterName, 20));
                map.put(WTopicListKeys.TOPIC, UtilAll.frontStringAtLeast(topic, 48));
                map.put(WTopicListKeys.CONSUMER_GROUP, UtilAll.frontStringAtLeast(group, 48));
                list.add(map);
            }
        }

    }


    private static String findTopicBelongToWhichCluster(final String topic, final ClusterInfo clusterInfo,
            final DefaultMQAdminExt defaultMQAdminExt) throws RemotingException, MQClientException,
            InterruptedException {
        TopicRouteData topicRouteData = defaultMQAdminExt.examineTopicRouteInfo(topic);

        BrokerData brokerData = topicRouteData.getBrokerDatas().get(0);

        String brokerName = brokerData.getBrokerName();

        Iterator<Entry<String, Set<String>>> it = clusterInfo.getClusterAddrTable().entrySet().iterator();
        while (it.hasNext()) {
            Entry<String, Set<String>> next = it.next();
            if (next.getValue().contains(brokerName)) {
                return next.getKey();
            }
        }
        return null;
    }


    public static void queryBrokerStatus(TreeMap<String, String> map, DefaultMQAdminExt defaultMQAdminExt,
            String brokeraddr) throws RemotingConnectException, RemotingSendRequestException, RemotingTimeoutException, InterruptedException, MQBrokerException {
        KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(brokeraddr);
        // 为了排序
        map.putAll(kvTable.getTable());
    }


    public static String queryTopicRoute(String topic, DefaultMQAdminExt defaultMQAdminExt) throws RemotingException, MQClientException, InterruptedException {
        
        TopicRouteData topicRouteData = defaultMQAdminExt.examineTopicRouteInfo(topic);
        String json = topicRouteData.toJson(true);
        return json;
    }
}
