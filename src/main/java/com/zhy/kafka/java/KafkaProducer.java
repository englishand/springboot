package com.zhy.kafka.java;

//import kafka.javaapi.producer.Producer;
//import kafka.producer.KeyedMessage;
//import kafka.producer.ProducerConfig;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class KafkaProducer extends Thread{

//    private final Producer<String,String> producer;
//    private final String topic;
//    private final Properties props = new Properties();
//    public KafkaProducer(String topic){
//        props.put("zookeeper.connect","192.168.110.182:2181");
//        // serializer.class为消息的序列化类
//        props.put("serializer.class","kafka.serializer.StringEncoder");
//        //指定Kafka节点列表，用于获取metadata（元数据）, 为了高可用, 最好配两个broker实例
//        props.put("metadata.broker.list","192.168.110.182:9092");
//        // 设置Partition类, 对队列进行合理的划分
////        props.put("partitioner.class","com.zhy.kafka.SimplePartitioner");
//        props.put("producer.type","sync");
//        // 是否压缩，默认0表示不压缩，1表示用gzip压缩，2表示用snappy压缩。压缩后消息中会有头来指明消息压缩类型，故在消费者端消息解压是透明的无需指定。
//        props.put("compression.codec", "1");
//        producer = new Producer<String, String>(new ProducerConfig(props));
//        this.topic = topic;
//    }

//    @Override
//    public void run() {
//        while (true){
//            Date date =new Date();
//            String messageStr = date.toString()+":_消息";
//            //如果直接使用keyedMessage，则默认使用单个分区，分区号为0
////            producer.send(new KeyedMessage<Integer, String>(topic,messageStr));
//            //使用多个分区，这里就可以省略while(true)的使用
//            for (int i=1;i<=2;i++){
//                List<KeyedMessage<String,String>> messageList = new ArrayList<KeyedMessage<String, String>>();
//                //每个分区3条信息
//                for (int j=1;j<4;j++){
//                    System.out.println(String.format("time: %tF %tT,partition: %s, sendMsg: %s",date,date,"partition["+i+"]",messageStr+j));
//                    messageList.add(new KeyedMessage<String, String>(topic,i+"","message["+messageStr+j+"]"));
//                }
//                producer.send(messageList);
//            }
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    public static void main(String[] args) {
//        com.zhy.kafka.java.KafkaProducer producer = new com.zhy.kafka.java.KafkaProducer(KafkaProperties.topic2);
//        producer.start();
    }
}
