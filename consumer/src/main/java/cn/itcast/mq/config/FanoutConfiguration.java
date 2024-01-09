package cn.itcast.mq.config;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 声明交换机与队列
 * @program: mq-demo
 * @author:
 * @create: 2024-01-08 22:26
 */
@Configuration
public class FanoutConfiguration {

    /**
     * 声明Fanout交换机
     *
     * @return {@link FanoutExchange}
     */
    @Bean
    public FanoutExchange fanoutExchange(){
//        也可以通过ExchangeBuilder类来创建交换机
//        ExchangeBuilder.fanoutExchange("");
        return new FanoutExchange("hmall.fanout2");
    }

    /**
     * 声明队列
     *
     * @return {@link Queue}
     */
    @Bean
    public Queue queue(){
//        也可以通过QueueBuilder类来创建队列，durable设置是否持久化，默认为持久化
        QueueBuilder.durable("").build();
        return new Queue("fanout.queue3");
    }

    /**
     * 绑定交换与队列
     *
     * @param queue          队列
     * @param fanoutExchange 交换机
     * @return {@link Binding}
     */
    @Bean
    public Binding fanoutBinding3(Queue queue,FanoutExchange fanoutExchange){
        return BindingBuilder.bind(queue).to(fanoutExchange);
    }

    /**
     * 声明direct类型的交换机
     *
     * @return {@link DirectExchange}
     */
    @Bean
    public DirectExchange directExchange(){
        return new DirectExchange("hmall.direct3");
    }

    /**
     * 绑定direct类型的交换机，并且添加对应的键值
     * 绑定不同的键值需要指定多个bean
     *
     * @param queue          队列
     * @param directExchange 交换机
     * @return {@link Binding}
     */
    @Bean
    public Binding directBinding(Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("red");
    }

    @Bean
    public Binding directBinding2(Queue queue,DirectExchange directExchange){
        return BindingBuilder.bind(queue).to(directExchange).with("blue");
    }

}
