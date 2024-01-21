package cn.itcast.mq.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 失败消息的处理
 * ConditionalOnProperty:配置改配置类在变量enabled为true的进行生效
 *
 * @program: mq-demo
 * @author:
 * @create: 2024-01-21 20:19
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.rabbitmq.listener.simple.retry",name = "enabled",havingValue = "true")
public class ErrorConfiguration {

    /**
     * 定义接收失败消息的交换机
     *
     * @return {@link DirectExchange}
     */
    @Bean
    public DirectExchange errorExchange(){
        return new DirectExchange("error.direct");
    }

    /**
     * 定义接收失败消息的队列
     *
     * @return {@link Queue}
     */
    @Bean
    public Queue errorQueue(){
        return new Queue("error.queue");
    }

    /**
     * 绑定交换机与队列
     *
     * @param errorQueue     错误队列
     * @param errorExchange 直接交换
     * @return {@link Binding}
     */
    @Bean
    public Binding errorBinding(Queue errorQueue,DirectExchange errorExchange){
        return BindingBuilder.bind(errorQueue).to(errorExchange).with("error");
    }

    /**
     * 处理失败消息
     * 将失败的消息发送到失败的队列中
     *
     * @param rabbitTemplate mq模板
     * @return {@link MessageRecoverer}
     */
    @Bean
    public MessageRecoverer recovererMessageRecoverer(RabbitTemplate rabbitTemplate){
        return new RepublishMessageRecoverer(rabbitTemplate,"error.direct","error");
    }
}
