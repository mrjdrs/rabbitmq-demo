package com.jdr.maven.rabbitmq.delay.delaymessage.service.delay.impl;

import com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.binding.BindingConstant;
import com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.exchange.ExchangeConstant;
import com.jdr.maven.rabbitmq.delay.delaymessage.service.delay.IDelayMessage;
import lombok.AllArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

/**
 * @author zhoude
 * @date 2019/11/12 16:45
 */
@Service("rabbitMqDelayMessage")
@AllArgsConstructor
public class RabbitMqDelayMessage implements IDelayMessage {

    private final AmqpTemplate amqpTemplate;

    @Override
    public void productMessage() {
        String orderId = "0000";
        for (int i = 1; i <= 10; i++) {
            amqpTemplate.convertAndSend(ExchangeConstant.ORDER_EXCHANGE, BindingConstant.ORDER_ROUTING_KEY, orderId + i);
            System.err.println(System.currentTimeMillis() + "创建了一个超时订单，订单ID：" + orderId + i);
        }
    }

    @Override
    public void consumerMessage() {
        // rabbitMq通过listener消息，无需实现consumer
    }
}
