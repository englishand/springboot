package com.zhy.kafka.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaOperations;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KafkaProducer {

    private final static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    @Autowired
    private KafkaTemplate<Integer,String> kafkaTemplate;

    @Test
    public void main() throws InterruptedException,RuntimeException{
//        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(KafkaConfig.class);
//        KafkaTemplate<Integer,String> kafkaTemplate = (KafkaTemplate<Integer, String>) ctx.getBean("kafkaTemplate");
        Date date = new Date();
        String message = new SimpleDateFormat().format(date)+" this is a producer message";
//        while (true){
            ListenableFuture<SendResult<Integer,String>> send = kafkaTemplate.send("topic1",0,0,message);
            send.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
                @Override
                public void onFailure(Throwable ex) {
                    logger.info("消息发送失败："+ex.getMessage());
                }

                @Override
                public void onSuccess(SendResult<Integer, String> result) {
                    logger.info(String.format("消息发送成功：%tF %tT topic:[%s]|offset:[%d]|partition:[%d]",
                            date,date,
                            result.getRecordMetadata().topic(),result.getRecordMetadata().offset(),result.getRecordMetadata().partition()));
                }
            });
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//        }

    }

    //Kafka开启事务方法2：使用kafkaTemplate.executeInTransaction()
//    @Test
//    public void testTransactional() {
//        Date date = new Date();
//        for (int i=0;i<10;i++){
//            ListenableFuture<SendResult<Integer,String>>  send = kafkaTemplate.executeInTransaction(new KafkaOperations.OperationsCallback<Integer, String, ListenableFuture<SendResult<Integer, String>>>() {
//                @Override
//                public ListenableFuture<SendResult<Integer, String>> doInOperations(KafkaOperations<Integer, String> operations) {
//                    //这里不指定分区号和key，所以将message按照round-robin模式发送到每个Partition
//                     operations.send("topic1",new SimpleDateFormat().format(date)+" this is a producer transactional message");
//                    throw new RuntimeException("fail");//测试事务
//                }
//            });
//
//            send.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
//                @Override
//                public void onFailure(Throwable ex) {
//                    logger.info("消息发送失败："+ex.getMessage());
//                }
//
//                @Override
//                public void onSuccess(SendResult<Integer, String> result) {
//                    logger.info(String.format("消息发送成功：%tF %tT topic:[%s]|offset:[%d]|partition:[%d]",
//                            date,date,
//                            result.getRecordMetadata().topic(),result.getRecordMetadata().offset(),result.getRecordMetadata().partition()));
//                }
//            });
//        }
//
//
//    }
}
