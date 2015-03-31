/**
 * Project Name:feinno-mq-servlet
 * File Name:WMQAdminExtHelper.java
 * Package Name:com.feinno.rocketmq.monitor.common
 * Date:Feb 28, 20159:48:58 AM
 * Copyright (c) 2015, honghao@feinno.com All Rights Reserved.
 *
 */

package com.feinno.rocketmq.monitor.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.QueryResult;
import com.alibaba.rocketmq.client.consumer.DefaultMQPullConsumer;
import com.alibaba.rocketmq.client.consumer.PullResult;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.MQVersion;
import com.alibaba.rocketmq.common.MixAll;
import com.alibaba.rocketmq.common.TopicConfig;
import com.alibaba.rocketmq.common.TopicFilterType;
import com.alibaba.rocketmq.common.UtilAll;
import com.alibaba.rocketmq.common.admin.ConsumeStats;
import com.alibaba.rocketmq.common.admin.OffsetWrapper;
import com.alibaba.rocketmq.common.constant.PermName;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.body.ClusterInfo;
import com.alibaba.rocketmq.common.protocol.body.Connection;
import com.alibaba.rocketmq.common.protocol.body.ConsumerConnection;
import com.alibaba.rocketmq.common.protocol.body.GroupList;
import com.alibaba.rocketmq.common.protocol.body.KVTable;
import com.alibaba.rocketmq.common.protocol.body.ProducerConnection;
import com.alibaba.rocketmq.common.protocol.body.TopicList;
import com.alibaba.rocketmq.common.protocol.route.BrokerData;
import com.alibaba.rocketmq.common.protocol.route.TopicRouteData;
import com.alibaba.rocketmq.common.subscription.SubscriptionGroupConfig;
import com.alibaba.rocketmq.remoting.common.RemotingHelper;
import com.alibaba.rocketmq.remoting.exception.RemotingConnectException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.alibaba.rocketmq.remoting.exception.RemotingSendRequestException;
import com.alibaba.rocketmq.remoting.exception.RemotingTimeoutException;
import com.alibaba.rocketmq.tools.admin.DefaultMQAdminExt;
import com.alibaba.rocketmq.tools.command.CommandUtil;
import com.alibaba.rocketmq.tools.command.topic.DeleteTopicSubCommand;
import com.feinno.rocketmq.monitor.cluster.bean.WClusterListKeys;
import com.feinno.rocketmq.monitor.consumer.bean.WConsumerProgressKeys;
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
            String brokeraddr) throws RemotingConnectException, RemotingSendRequestException,
            RemotingTimeoutException, InterruptedException, MQBrokerException {
        KVTable kvTable = defaultMQAdminExt.fetchBrokerRuntimeStats(brokeraddr);
        // 为了排序
        map.putAll(kvTable.getTable());
    }


    public static String queryTopicRoute(String topic, DefaultMQAdminExt defaultMQAdminExt)
            throws RemotingException, MQClientException, InterruptedException {

        TopicRouteData topicRouteData = defaultMQAdminExt.examineTopicRouteInfo(topic);
        String json = topicRouteData.toJson(true);
        return json;
    }


    public static void consumerProgress(String group, DefaultMQAdminExt defaultMQAdminExt,
            List<Map<String, String>> list) throws RemotingException, MQClientException,
            InterruptedException, MQBrokerException {
        // 查询特定consumer
        ConsumeStats consumeStats = defaultMQAdminExt.examineConsumeStats(group);

        List<MessageQueue> mqList = new LinkedList<MessageQueue>();
        mqList.addAll(consumeStats.getOffsetTable().keySet());
        Collections.sort(mqList);

        for (MessageQueue mq : mqList) {
            OffsetWrapper offsetWrapper = consumeStats.getOffsetTable().get(mq);

            long diff = offsetWrapper.getBrokerOffset() - offsetWrapper.getConsumerOffset();
            Map<String, String> map = new HashMap<String, String>();
            map.put(WConsumerProgressKeys.TOPIC, UtilAll.frontStringAtLeast(mq.getTopic(), 32));
            map.put(WConsumerProgressKeys.BROKER_NAME, UtilAll.frontStringAtLeast(mq.getBrokerName(), 32));
            map.put(WConsumerProgressKeys.QUID, String.valueOf(mq.getQueueId()));
            map.put(WConsumerProgressKeys.BROKER_OFFSET, String.valueOf(offsetWrapper.getBrokerOffset()));
            map.put(WConsumerProgressKeys.CONSUMER_OFFSET, String.valueOf(offsetWrapper.getConsumerOffset()));
            map.put(WConsumerProgressKeys.DIFF, String.valueOf(diff));
            list.add(map);
        }
    }


    public static Map<String, String> createTopic(DefaultMQAdminExt defaultMQAdminExt, String clustername,
            String topicname) throws InterruptedException, MQBrokerException, RemotingException,
            MQClientException {
        TopicConfig topicConfig = new TopicConfig();
        topicConfig.setReadQueueNums(8);
        topicConfig.setWriteQueueNums(8);
        topicConfig.setTopicName(topicname);
        topicConfig.setPerm(6);
        topicConfig.setTopicSysFlag(0);
        topicConfig.setOrder(false);
        topicConfig.setTopicFilterType(TopicFilterType.SINGLE_TAG);
        Set<String> masterSet = CommandUtil.fetchMasterAddrByClusterName(defaultMQAdminExt, clustername);
        for (String addr : masterSet) {
            defaultMQAdminExt.createAndUpdateTopicConfig(addr, topicConfig);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("create topic to %s success."), addr);
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("topicName", topicConfig.getTopicName());
        map.put("readQueueNums", String.valueOf(topicConfig.getReadQueueNums()));
        map.put("writeQueueNums", String.valueOf(topicConfig.getWriteQueueNums()));
        map.put("perm", PermName.perm2String(topicConfig.getPerm()));
        map.put("topicFilterType", topicConfig.getTopicFilterType().toString());
        map.put("topicSysFlag", String.valueOf(topicConfig.getTopicSysFlag()));
        map.put("order", String.valueOf(topicConfig.isOrder()));
        return map;
    }


    public static Map<String, String> createSubGroup(DefaultMQAdminExt defaultMQAdminExt, String clustername,
            String groupname) throws RemotingException, MQBrokerException, InterruptedException,
            MQClientException {
        SubscriptionGroupConfig subscriptionGroupConfig = new SubscriptionGroupConfig();
        subscriptionGroupConfig.setConsumeBroadcastEnable(false);
        subscriptionGroupConfig.setConsumeFromMinEnable(false);
        // groupName
        subscriptionGroupConfig.setGroupName(groupname);
        subscriptionGroupConfig.setRetryQueueNums(1);
        subscriptionGroupConfig.setRetryMaxTimes(16);
        subscriptionGroupConfig.setBrokerId(MixAll.MASTER_ID);
        subscriptionGroupConfig.setWhichBrokerWhenConsumeSlowly(1);
        subscriptionGroupConfig.setConsumeEnable(true);
        Set<String> masterSet = CommandUtil.fetchMasterAddrByClusterName(defaultMQAdminExt, clustername);
        for (String addr : masterSet) {
            defaultMQAdminExt.createAndUpdateSubscriptionGroupConfig(addr, subscriptionGroupConfig);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("create subscription group to %s success", addr));
            }
        }
        Map<String, String> map = new HashMap<String, String>();
        map.put("groupName", subscriptionGroupConfig.getGroupName());
        map.put("consumeEnable", String.valueOf(subscriptionGroupConfig.isConsumeEnable()));
        map.put("consumeFromMinEnable", String.valueOf(subscriptionGroupConfig.isConsumeFromMinEnable()));
        map.put("consumeBroadcastEnable", String.valueOf(subscriptionGroupConfig.isConsumeBroadcastEnable()));
        map.put("retryQueueNums", String.valueOf(subscriptionGroupConfig.getRetryQueueNums()));
        map.put("retryMaxTimes", String.valueOf(subscriptionGroupConfig.getRetryMaxTimes()));
        map.put("brokerId", String.valueOf(subscriptionGroupConfig.getBrokerId()));
        map.put("whichBrokerWhenConsumeSlowly",
            String.valueOf(subscriptionGroupConfig.getWhichBrokerWhenConsumeSlowly()));
        return map;
    }


    public static void updateBrokerConfig(DefaultMQAdminExt defaultMQAdminExt, String clustername,
            String key, String value) throws RemotingConnectException, RemotingSendRequestException,
            RemotingTimeoutException, UnsupportedEncodingException, InterruptedException, MQBrokerException {

        Properties properties = new Properties();
        properties.put(key, value);
        Set<String> masterSet = CommandUtil.fetchMasterAddrByClusterName(defaultMQAdminExt, clustername);
        for (String brokerAddr : masterSet) {
            defaultMQAdminExt.updateBrokerConfig(brokerAddr, properties);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format("update broker config success, %s", brokerAddr));
            }
        }
        return;

    }


    public static void examineProducerConnectionInfo(DefaultMQAdminExt defaultMQAdminExt, String topic,
            String group) throws RemotingException, MQClientException, InterruptedException,
            MQBrokerException {

        ProducerConnection pc = defaultMQAdminExt.examineProducerConnectionInfo(group, topic);
        int i = 1;
        for (Connection conn : pc.getConnectionSet()) {
            System.out.printf("%04d  %-32s %-22s %-8s %s\n",//
                i++,//
                conn.getClientId(),//
                conn.getClientAddr(),//
                conn.getLanguage(),//
                MQVersion.getVersionDesc(conn.getVersion())//
                );
        }

    }


    public static List<Map<String, String>> examineConsumerConnectionInfo(
            DefaultMQAdminExt defaultMQAdminExt, String group) throws InterruptedException,
            MQBrokerException, RemotingException, MQClientException {

        ConsumerConnection cc = defaultMQAdminExt.examineConsumerConnectionInfo(group);
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        // 打印连接
        int i = 1;
        for (Connection conn : cc.getConnectionSet()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("No.", String.valueOf(i++));
            map.put("ClientId", conn.getClientId());
            map.put("ClientAddr", conn.getClientAddr());
            map.put("Language", conn.getLanguage().toString());
            map.put("MQVersion", MQVersion.getVersionDesc(conn.getVersion()));
            list.add(map);
        }
        return list;
    }


    public static Map<String, String> queryById(DefaultMQAdminExt defaultMQAdminExt, String msgid)
            throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
        Map<String, String> map = new HashMap<String, String>();
        MessageExt msg = defaultMQAdminExt.viewMessage(msgid);
        map.put("Topic", msg.getTopic());
        map.put("Tags", msg.getTags());
        map.put("Keys", msg.getKeys());
        map.put("QueueID", String.valueOf(msg.getQueueId()));
        map.put("QueueOffset", String.valueOf(msg.getQueueOffset()));
        map.put("CommitLogOffset:", String.valueOf(msg.getCommitLogOffset()));
        map.put("ReconsumeTimes", String.valueOf(msg.getReconsumeTimes()));
        map.put("BornTimestamp", UtilAll.timeMillisToHumanString2(msg.getBornTimestamp()));
        map.put("StoreTimestamp", UtilAll.timeMillisToHumanString2(msg.getStoreTimestamp()));
        map.put("BornHost", RemotingHelper.parseSocketAddressAddr(msg.getBornHost()));
        map.put("StoreHost", RemotingHelper.parseSocketAddressAddr(msg.getStoreHost()));
        map.put("SystemFlag", String.valueOf(msg.getSysFlag()));
        map.put("Properties", msg.getProperties() != null ? msg.getProperties().toString() : "");
        return map;
    }


    public static List<Map<String, String>> queryByKey(DefaultMQAdminExt defaultMQAdminExt, String key,
            String topic) throws MQClientException, InterruptedException {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        QueryResult queryResult = defaultMQAdminExt.queryMessage(topic, key, 64, 0, Long.MAX_VALUE);
        for (MessageExt msg : queryResult.getMessageList()) {
            Map<String, String> map = new HashMap<String, String>();
            map.put("MessageID", String.valueOf(msg.getMsgId()));
            map.put("QID", String.valueOf(msg.getQueueId()));
            map.put("Offset", String.valueOf(msg.getQueueOffset()));
            map.put("StoreTimestamp", UtilAll.timeMillisToHumanString2(msg.getStoreTimestamp()));
            list.add(map);
        }
        return list;
    }


    public static Map<String, String> queryByOffset(DefaultMQAdminExt defaultMQAdminExt, String brokername,
            String topic, String queueid, String offset) throws MQClientException, RemotingException,
            MQBrokerException, InterruptedException, IOException {
        DefaultMQPullConsumer defaultMQPullConsumer = new DefaultMQPullConsumer(MixAll.TOOLS_CONSUMER_GROUP);
        Map<String, String> map = null;
        try {
            MessageQueue mq = new MessageQueue();
            mq.setTopic(topic);
            mq.setBrokerName(brokername);
            mq.setQueueId(Integer.parseInt(queueid));
            defaultMQPullConsumer.start();
            PullResult pullResult = defaultMQPullConsumer.pull(mq, "*", Long.parseLong(offset), 1);
            if (pullResult != null) {
                switch (pullResult.getPullStatus()) {
                case FOUND:
                    map = queryById(defaultMQAdminExt, pullResult.getMsgFoundList().get(0).getMsgId());
                    break;
                case NO_MATCHED_MSG:
                case NO_NEW_MSG:
                case OFFSET_ILLEGAL:
                default:
                    break;
                }
            }
        }
        finally {
            defaultMQPullConsumer.shutdown();
        }
        return map;
    }


    public static void deleteTopic(DefaultMQAdminExt defaultMQAdminExt, String clustername, String topicname)
            throws RemotingException, MQBrokerException, InterruptedException, MQClientException {
        // 删除 broker 上的 topic 信息
        Set<String> masterSet = CommandUtil.fetchMasterAddrByClusterName(defaultMQAdminExt, clustername);
        defaultMQAdminExt.deleteTopicInBroker(masterSet, topicname);
        System.out.printf("delete topic [%s] from cluster [%s] success.\n", topicname, clustername);
        // 删除 NameServer 上的 topic 信息
        Set<String> nameServerSet = null;
        if (defaultMQAdminExt.getNamesrvAddr() != null) {
            String[] ns = defaultMQAdminExt.getNamesrvAddr().trim().split(";");
            nameServerSet = new HashSet<String>(Arrays.asList(ns));
        }
        // 删除 NameServer 上的 topic 信息
        defaultMQAdminExt.deleteTopicInNameServer(nameServerSet, topicname);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info(String.format("delete topic [%s] from NameServer success.", topicname));
        }
    }


    public static void deleteSubGroup(DefaultMQAdminExt defaultMQAdminExt, String clustername,
            String groupname) throws RemotingException, MQBrokerException, InterruptedException,
            MQClientException {

        Set<String> masterSet = CommandUtil.fetchMasterAddrByClusterName(defaultMQAdminExt, clustername);
        for (String master : masterSet) {
            defaultMQAdminExt.deleteSubscriptionGroup(master, clustername);
            if (LOGGER.isInfoEnabled()) {
                LOGGER.info(String.format(
                    "delete subscription group [%s] from broker [%s] in cluster [%s] success.\n", groupname,
                    master, clustername));
            }
        }
        // 删除%RETRY%打头的Topic
        try {
            DeleteTopicSubCommand.deleteTopic(defaultMQAdminExt, clustername, MixAll.RETRY_GROUP_TOPIC_PREFIX
                    + groupname);
            DeleteTopicSubCommand.deleteTopic(defaultMQAdminExt, clustername, MixAll.DLQ_GROUP_TOPIC_PREFIX
                    + groupname);
        }
        catch (Exception e) {
        }
        return;
    }

}
