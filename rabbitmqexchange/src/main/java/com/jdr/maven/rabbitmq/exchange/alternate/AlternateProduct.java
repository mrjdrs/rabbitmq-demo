package com.jdr.maven.rabbitmq.exchange.alternate;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 备份交换器
 *
 * @author zhoude
 * @date 2019/11/11 21:33
 */
public class AlternateProduct {

    private static final String EXCHANGE_NAME = "alternate.exchange";
    private static final String EXCHANGE_BAK_NAME = "alternate-bak.exchange";

    private static final String QUEUE_NAME = "alternate.queue";
    private static final String QUEUE_BAK_NAME = "alternate-bak.queue";

    private static final String ROUTING_KEY_NAME = "alternate.routing.key";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, false, getExchangeDeclareArgs());
        // fanout类型，放款路由限制
        channel.exchangeDeclare(EXCHANGE_BAK_NAME, BuiltinExchangeType.FANOUT, false, false, false, null);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(QUEUE_BAK_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_NAME);
        // 因为交换器QUEUE_BAK_NAME设置fanout类型，所以可以不必关心路由键，故随便写可能将消息路由到对应的队列中
        channel.queueBind(QUEUE_BAK_NAME, EXCHANGE_BAK_NAME, "123");

        // 发消息时路由键设置一个不存在的""，让其路由不到，从而把消息发到备份队列中
        channel.basicPublish(EXCHANGE_NAME, "", MessageProperties.PERSISTENT_TEXT_PLAIN,
                "alternate".getBytes());

        RabbitMqUtils.close(connection, channel);
    }

    private static Map<String, Object> getExchangeDeclareArgs() {
        Map<String, Object> result = new HashMap<String, Object>(1);
        result.put("alternate-exchange", EXCHANGE_BAK_NAME);
        return result;
    }
}
