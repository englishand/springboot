package com.zhy.kafka.spring;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.transaction.KafkaTransactionManager;
import org.springframework.messaging.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息发送的可靠性
 * props.put(“acks”,”all”) ，这个acks作为消息发送的可靠性重要的参数，
 * acks=0 生产者不会等待broker的任何确认，消息会被立即添加到缓冲区并被认为已经发送
 * acks=1，在leader服务器的副本收到消息的同一时间，生产者会接收到broker的确认。默认值
 * acks=all（或-1），一旦所有的同步副本接收到消息，生产者才会接收到broker的确认。这是最安全的模式
 *
 * <1> 若指定Partition ID,则ProducerRecord被发送至指定Partition
 * <2> 若未指定Partition ID,但指定了Key, ProducerRecord会按照hasy(key)发送至对应Partition
 * <3> 若既未指定Partition ID也没指定Key，ProducerRecord会按照round-robin模式发送到每个Partition
 * <4> 若同时指定了Partition ID和Key, ProducerRecord只会发送到指定的Partition (Key不起作用，代码逻辑决定)
 */
@Configuration
@EnableKafka
public class KafkaConfig {

    //配置topic开始
    @Bean
    public KafkaAdmin admin(){
        Map<String,Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.110.182:9092");
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic initialTopic(){
        //可修改分区数量，进而修改某个主题的分区数，重启项目即可。
        return new NewTopic("topic1",2, (short) 1);
    }

    @Bean
    public AdminClient client(){
        return AdminClient.create(admin().getConfig());
    }
    //配置topic结束

    //配置生产者Factory以及Template
    @Bean
    public ProducerFactory<Integer,String> producerFactory(){
        DefaultKafkaProducerFactory<Integer, String> factory = new DefaultKafkaProducerFactory<>(producerConfigs());
        //Kafka开启事务方式
//        factory.transactionCapable();
//        factory.setTransactionIdPrefix("tran-");//配置事务transactional.id的前缀
        //配置事务结束
        return factory;
    }


//    @Bean //kafka开启事务方式1：配置事务管理类,但实验未通过,因为在方法中使用@Transactional注解
//    public KafkaTransactionManager transactionManager(){
//        KafkaTransactionManager manager = new KafkaTransactionManager(producerFactory());
//        return manager;
//    }

    @Bean
    public Map<String,Object> producerConfigs(){
        Map<String,Object>  props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.110.182:9092");
        props.put(ProducerConfig.ACKS_CONFIG,"all");
        props.put(ProducerConfig.RETRIES_CONFIG,1);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG,16384);//控制批处理大小，单位为字节
        props.put(ProducerConfig.LINGER_MS_CONFIG,1);//批量发送，延迟为1毫秒，启用该功能能有效减少生产者发送消息次数，从而提高并发量
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG,1024000);//生产者可以使用的总内存字节来缓冲等待发送到服务器的记录
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class);
        return props;
    }
    @Bean
    public KafkaTemplate<Integer,String> kafkaTemplate(){
        return new KafkaTemplate<Integer, String>(producerFactory());
    }
    //配置生产者Factory和Template结束

    //配置消息订阅者开始
    @Bean
    public ConcurrentKafkaListenerContainerFactory<Integer,String> kafkaListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<Integer,String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        //设置并发量（即消费线程数），小于或等于topic的分区数
        factory.setConcurrency(2);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
    @Bean
    public ConsumerFactory<Integer,String> consumerFactory(){
        return new DefaultKafkaConsumerFactory<Integer,String>(consumerConfigs());
    }
    @Bean
    public Map<String,Object> consumerConfigs(){
        HashMap<String,Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"192.168.110.182:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG,"group1");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG,"true");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG,"1000");//自动提交的频率
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG,"15000");//session超时时间
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }
    //配置消息订阅者结束

    //配置消息监听类

    @Bean//默认spring-Kafka会为每一个监听方法创建一个线程来向Kafka服务器拉取消息
    public SimpleConsumerListener simpleConsumerListener(){
        return new SimpleConsumerListener();
    }

//    @Bean//不需要注解配置的监听器
    public KafkaMessageListenerContainer demoListenerContainer(){
        ContainerProperties properties = new ContainerProperties("topic1");
        properties.setGroupId("group1");
        properties.setMessageListener(new MessageListener<Integer,String>() {
            private Logger logger = LoggerFactory.getLogger(this.getClass());
            @Override
            public void onMessage(ConsumerRecord<Integer, String> data) {
                logger.info("topic1 received :"+data.toString());
            }
        });
        return new KafkaMessageListenerContainer(consumerFactory(),properties);
    }
    //配置消息监听类结束

    //单消息消费异常处理器配置
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareErrorHandler(){
        return new ConsumerAwareListenerErrorHandler() {
            private Logger logger = LoggerFactory.getLogger(this.getClass());
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException exception, Consumer<?, ?> consumer) {
                logger.info("consumerAwareErrorHandler received:"+message.getPayload().toString());
                return null;
            }
        };
    }
    //单消息消费异常处理器结束
}
