package com.jdr.maven.rabbitmq.delay.delaymessage.listener.mq;

import com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.exchange.ExchangeConstant;
import com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.queue.QueueConstant;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * 订单mq监听类
 *
 * @author zhoude
 * @date 2019/11/12 16:41
 */
@Component
public class OrderListener {

    @RabbitListener(queues = QueueConstant.ORDER_DEAD_QUEUE)
    public void process(String message) {
        System.err.println("监听到" + ExchangeConstant.ORDER_DEAD_EXCHANGE + "的消息");
        System.err.println(System.currentTimeMillis() + "消费了一个超时订单，订单ID：" + message);
    }
}
