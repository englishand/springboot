package com.zhy.kafka.java;

//import kafka.consumer.Consumer;
//import kafka.consumer.ConsumerConfig;
//import kafka.consumer.ConsumerIterator;
//import kafka.consumer.KafkaStream;
//import kafka.javaapi.consumer.ConsumerConnector;
//import kafka.message.MessageAndMetadata;
//
//import java.util.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//public class KafkaConsumer {
//
//    private final ConsumerConnector consumer;
//    private final String topic;
//    public KafkaConsumer(String topic){
//        consumer = Consumer.createJavaConsumerConnector(createConsumerConfig());
//        this.topic = topic;
//    }
//
//
//    public void run() {
//        //设置Topic=>Thread Num映射关系, 构建具体的流
//        Map<String,Integer> topicCountMap = new HashMap<>();
//        //指定多少个线程来消费topic
//        topicCountMap.put(topic,new Integer(6));
//        Map<String, List<KafkaStream<byte[],byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
//        ExecutorService executor = Executors.newFixedThreadPool(6);
//        try {
//
//                //启动线程池去消费对应的信息
//                List<KafkaStream<byte[],byte[]>> streams= consumerMap.get(topic);
//
//                for (final KafkaStream<byte[],byte[]> stream:streams){
//                    executor.submit(new Runnable() {
//                        @Override
//                        public void run() {
//                            ConsumerIterator<byte[],byte[]> it = stream.iterator();
//                            while (it.hasNext()){
//                                MessageAndMetadata<byte[],byte[]> mam = it.next();
//                                Date date = new Date();
//                                System.out.println(
//                                        String.format("%tF %tT | %s | thread_id[%d] | partition[%s] | offset[%s] | %s",
//                                                date,date,Thread.currentThread().getName(),Thread.currentThread().getId(),
//                                                mam.partition(),mam.offset(), new String(mam.message()))
//                                );
//                            }
//                        }
//                    });
//                }
//        }catch (Exception e){
//            System.out.println("异常："+e.getMessage());
//        }finally {
//            executor.shutdown();
//        }
//
//    }
//
//    private static ConsumerConfig createConsumerConfig(){
//        Properties props = new Properties();
//        props.put("zookeeper.connect", KafkaProperties.zkConnect);
//        props.put("group.id", KafkaProperties.groupId);
//        //zookeeper的最大超时时间，即心跳时间，用于检测消费者是否挂掉
//        props.put("zookeeper.session.timeout.ms","400");
//        //指定多久消费者更新offset到zookeeper中。
//        props.put("zookeeper.sync.time.ms","200");
//        // kafka的group 消费记录是保存在zookeeper上的, 但这个信息在zookeeper上不是实时更新的, 需要有个间隔时间更新
//        props.put("auto.commit.interval.ms","1000");
//        props.put("auto.offset.reset","smallest");
//        return new ConsumerConfig(props);
//    }
//
//    public static void main(String[] args) {
//        com.zhy.kafka.java.KafkaConsumer consumer = new com.zhy.kafka.java.KafkaConsumer(KafkaProperties.topic);
//        consumer.run();
//    }
//
//}
