package com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.queue;

import com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.exchange.ExchangeConstant;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * mq队列配置类
 *
 * @author zhoude
 * @date 2019/11/12 16:20
 */
@Component
@Configuration
public class QueueConfig {

    @Bean(name = "orderQueue")
    public Queue initOrderQueue() {
        Map<String, Object> arguments = new HashMap<>(5);
        arguments.put("x-message-ttl", 10000);
        arguments.put("x-dead-letter-exchange", ExchangeConstant.ORDER_DEAD_EXCHANGE);
        return new Queue(QueueConstant.ORDER_QUEUE, true, false, false, arguments);
    }

    @Bean(name = "orderDeadQueue")
    public Queue initOrderDeadQueue() {
        return new Queue(QueueConstant.ORDER_DEAD_QUEUE);
    }
}
