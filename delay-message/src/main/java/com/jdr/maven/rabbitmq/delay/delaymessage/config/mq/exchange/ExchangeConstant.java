package com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.exchange;

/**
 * mq交换器常量
 *
 * @author zhoude
 * @date 2019/11/12 16:16
 */
public interface ExchangeConstant {

    /**
     * 订单交换器
     */
    String ORDER_EXCHANGE = "order.exchange";

    /**
     * 订单死信交换器
     */
    String ORDER_DEAD_EXCHANGE = "order.dead.exchange";
}
