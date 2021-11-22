package com.zhy.kafka.java;


public interface KafkaProperties {

    final static String zkConnect = "127.0.0.1:2181";
    final static String groupId = "group1";
    final static String groupId2 = "group2";
    final static String groupId3 = "group3";
    final static String topic = "topic1";
    final static String kafkaServerURL = "127.0.0.1";
    final static int kafkaServerPort = 9092;
    final static int kafkaProducerBuffersize = 64*1024;
    final static int ConnectionTimeOut = 20000;
    final static int reconnectInterval = 10000;
    final static String topic2 = "topic2";
    final static String topic3 = "topic3";
    final static String clientId = "SimpleConsumerDemoClient";
}
