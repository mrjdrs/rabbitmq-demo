package com.jdr.maven.rabbitmq.delay.delaymessage.service.delay;

/**
 * 延迟消息接口
 *
 * @author zhoude
 * @date 2019/11/12 16:45
 */
public interface IDelayMessage {

    /**
     * 生产延迟消息
     */
    void productMessage();

    /**
     * 消费延迟消息
     */
    void consumerMessage();
}
