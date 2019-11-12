package com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.exchange;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * mq交换器配置类
 *
 * @author zhoude
 * @date 2019/11/12 16:12
 */
@Component
@Configuration
public class ExchangeConfig {

    @Bean(name = "orderExchange")
    public TopicExchange initOrderExchange() {
        return new TopicExchange(ExchangeConstant.ORDER_EXCHANGE);
    }

    @Bean(name = "orderDeadExchange")
    public FanoutExchange initOrderDeadExchange() {
        return new FanoutExchange(ExchangeConstant.ORDER_DEAD_EXCHANGE);
    }
}
