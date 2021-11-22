package com.zhy.kafka.spring;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.DescribeTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.listener.*;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * 用于消息监听的类，当名为”topic1"的topic接收到消息之后，这个listen方法就会调用
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SimpleConsumerListener {

    private final static Logger logger = LoggerFactory.getLogger(SimpleConsumerListener.class);
    private final CountDownLatch latch = new CountDownLatch(1);

    @KafkaListener(id = "group1",topics = "topic1",errorHandler = "consumerAwareErrorHandler",
            topicPartitions = {@TopicPartition(topic = "topic1",partitionOffsets =
                    { @PartitionOffset(partition = "1", initialOffset = "112")
                    ,@PartitionOffset(partition = "0",initialOffset = "302")})})
    public void listen(String records, ConsumerRecord<Integer,String> record){
        //dosomething here
        logger.info("group1 received: "+record.toString());
        this.latch.countDown();
        throw new RuntimeException("fail");
    }


    @Autowired
    private AdminClient client;

    /**
     * 创建主题
     * @throws InterruptedException
     */
    @Test
    public void createTopic() throws InterruptedException{
        NewTopic topic = new NewTopic("topic4",3, (short) 1);
        client.createTopics(Arrays.asList(topic));
        //为了避免在创建过程中程序关闭导致创建失败，所以加了一秒的休眠
        Thread.sleep(1000);
    }

    /**
     * 查看某个主题的分区信息
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Test
    public void describeTopicInfo() throws ExecutionException,InterruptedException{
        DescribeTopicsResult result = client.describeTopics(Arrays.asList("topic1"));
        result.all().get().forEach((k,v)-> logger.info("k: "+k+",v: "+v.toString()+"\n"));
    }
}
