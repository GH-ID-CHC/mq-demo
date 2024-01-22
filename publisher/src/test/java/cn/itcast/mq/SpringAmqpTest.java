package cn.itcast.mq;
/**
 * Author: CHAI
 * Date: 2023/12/27
 */

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @program: mq-demo
 * @author:
 * @create: 2023-12-27 21:55
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SpringAmqpTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.host}")
    private String hostname;

    /**
     * rabbitTemplate测试发送消息
     */
    @Test
    public void testSendMessage(){
        String message="hello SpringAmqp";


        String queue="simple.queue";
        rabbitTemplate.convertAndSend(queue,message);
    }

//    测试fanout交换机
    @Test
    public void testFanoutExchange() {
        // 交换机名称
        String exchangeName = "hmall.fanout";
        // 消息
        String message = "hello, everyone!";
        rabbitTemplate.convertAndSend(exchangeName, "", message);
    }

    /**
     * 测试direct交换机
     */
    @Test
    public void testSendDirectExchange() {
        // 交换机名称
        String exchangeName = "hmall.direct";
        // 消息
        String message = "蓝色警报！日本乱排核废水，导致海洋生物变异，惊现哥斯拉！";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "blue", message);
    }

    /**
     * 测试发送topic交换
     */
    @Test
    public void testSendTopicExchange() {
        // 交换机名称
        String exchangeName = "hmall.topic";
        // 消息
        String message = "喜报！孙悟空大战哥斯拉，胜!";
        // 发送消息
        rabbitTemplate.convertAndSend(exchangeName, "china.news", message);
    }

    /**
     * 测试消息转换器
     *
     * @throws InterruptedException 中断异常
     */
    @Test
    public void testSendMap() throws InterruptedException {
        // 准备消息
        Map<String,Object> msg = new HashMap<>();
        msg.put("name", "柳岩");
        msg.put("age", 21);
        // 发送消息
        rabbitTemplate.convertAndSend("object.queue", msg);
    }

    /**
     * 生产者确认模式
     *
     * @throws InterruptedException 中断异常
     */
    @Test
    public void testPublisherConfirm() throws InterruptedException {
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
        correlationData.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error("handle message ack fail", throwable);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm confirm) {
                log.debug("收到消息回执信息");
                if (confirm.isAck()) {
                    log.debug("消息发送成功！收到ack");
                } else {
                    log.error("消息发送失败,收到nack,原因：{}",confirm.getReason());
                }
            }
        });
        rabbitTemplate.convertAndSend("hmall.direct", "red", "hello", correlationData);

        //测试附加:接收消息回执的需要进行等待
        Thread.sleep(2000);
    }

    /**
     * 发送延迟消息
     */
    @Test
    public void testSendDelayMessage() {
        String message="hello delay";
        rabbitTemplate.convertAndSend("delay.direct", "delay", message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
//                设置延迟1秒
                message.getMessageProperties().setDelay(10000);
                return message;
            }
        });
    }

}
