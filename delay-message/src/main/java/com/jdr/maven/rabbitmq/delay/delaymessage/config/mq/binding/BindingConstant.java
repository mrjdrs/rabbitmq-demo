package com.jdr.maven.rabbitmq.delay.delaymessage.config.mq.binding;

/**
 * mq绑定器常量
 *
 * @author zhoude
 * @date 2019/11/12 16:16
 */
public interface BindingConstant {

    /**
     * 订单绑定器
     */
    String ORDER_ROUTING_KEY = "order.routing.key";

    /**
     * 订单死信绑定器
     */
    String ORDER_DEAD_ROUTING_KEY = "order.dead.routing.key";
}
