package cn.itcast.mq.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * 生产者确认消息
 * 配置ReturnCallback
 * @program: mq-demo
 * @author:
 * @create: 2024-01-16 20:24
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MqConfirmConfig implements ApplicationContextAware {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
//        RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
                log.debug("收到消息：message:{},replyCode:{},replyText:{},exchange:{},routingKey:{}",message,replyCode,replyText,exchange,routingKey);
            }
        });
    }
}
