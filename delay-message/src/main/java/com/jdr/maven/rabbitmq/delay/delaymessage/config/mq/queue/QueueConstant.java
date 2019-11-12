package com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.queue;

/**
 * mq队列常量
 *
 * @author zhoude
 * @date 2019/11/12 16:16
 */
public interface QueueConstant {

    /**
     * 订单队列
     */
    String ORDER_QUEUE = "order.queue";

    /**
     * 订单死信队列
     */
    String ORDER_DEAD_QUEUE = "order.dead.queue";
}
