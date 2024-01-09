package cn.itcast.mq.listners;
/**
 * Author: CHAI
 * Date: 2023/12/27
 */

import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.Map;

/**
 *
 * @program: mq-demo
 * @author:
 * @create: 2023-12-27 22:37
 */
@Component
public class MqListner {


    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue1(String msg) throws InterruptedException {
        System.out.println("消费者1接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(20);
    }

    @RabbitListener(queues = "simple.queue")
    public void listenWorkQueue2(String msg) throws InterruptedException {
        System.err.println("消费者2........接收到消息：【" + msg + "】" + LocalTime.now());
        Thread.sleep(200);
    }

    /**绑定了fanout交换机的队列*/
    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String msg) {
        System.out.println("消费者1接收到Fanout消息：【" + msg + "】");
    }

    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String msg) {
        System.out.println("消费者2接收到Fanout消息：【" + msg + "】");
    }


    /**绑定了direct交换机的队列*/
    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueue1(String msg) {
        System.out.println("消费者1接收到direct.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "direct.queue2")
    public void listenDirectQueue2(String msg) {
        System.out.println("消费者2接收到direct.queue2的消息：【" + msg + "】");
    }

    /**绑定了direct交换机的队列*/
    @RabbitListener(queues = "topic.queue1")
    public void listenTopicQueue1(String msg){
        System.out.println("消费者1接收到topic.queue1的消息：【" + msg + "】");
    }

    @RabbitListener(queues = "topic.queue2")
    public void listenTopicQueue2(String msg){
        System.out.println("消费者2接收到topic.queue2的消息：【" + msg + "】");
    }


    /**
     * 使用注解的方式声明队列、交换机，并通过key值进行绑定
     *
     * @param msg 味精
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "direct.queue3"),
            exchange = @Exchange(name = "hmall.direct4",type = ExchangeTypes.DIRECT),
            key = {"red","blue","yellow"}
    ))
    public void directBinding(String msg){
        System.out.println("消费者2接收到direct.queue3的消息：【" + msg + "】");
    }

    /**
     * 测试消息转化器，接受map类型的数据
     *
     * @param msg 味精
     * @throws InterruptedException 中断异常
     */
    @RabbitListener(queues = "object.queue")
    public void listenSimpleQueueMessage(Map<String, Object> msg) throws InterruptedException {
        System.out.println("消费者接收到object.queue消息：【" + msg + "】");
    }



}
