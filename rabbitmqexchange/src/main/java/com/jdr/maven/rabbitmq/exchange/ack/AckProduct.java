package com.jdr.maven.rabbitmq.exchange.ack;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhoude
 * @date 2019/11/11 13:55
 */
public class AckProduct {

    private static final String EXCHANGE_NAME = "ack.exchange";
    private static final String QUEUE_NAME = "ack.queue";
    private static final String ROUTING_KEY = "ack.routing-key";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        for (int i = 0; i < 20; i++) {
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, null, ("ack" + i).getBytes());
        }

        RabbitMqUtils.close(connection, channel);
    }
}
