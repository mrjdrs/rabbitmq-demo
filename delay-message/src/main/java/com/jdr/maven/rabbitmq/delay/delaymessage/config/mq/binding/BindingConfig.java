package com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.binding;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * mq绑定器配置类
 *
 * @author zhoude
 * @date 2019/11/12 16:12
 */
@Component
@Configuration
public class BindingConfig {

    @Bean(name = "orderBinding")
    public Binding initOrderBinding(@Qualifier("orderQueue") Queue queue,
                                    @Qualifier("orderExchange") TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(BindingConstant.ORDER_ROUTING_KEY);
    }

    @Bean(name = "orderDeadBinding")
    public Binding initOrderDeadBinding(@Qualifier("orderDeadQueue") Queue queue,
                                        @Qualifier("orderDeadExchange") FanoutExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange);
    }
}
