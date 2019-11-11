package com.jdr.maven.rabbitmq.exchange.internal;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhoude
 * @date 2019/11/11 16:23
 */
public class InternalProduct {

    private static final String EXCHANGE_NAME = "internal.exchange";
    private static final String EXCHANGE_NAME2 = "internal.exchange2";
    private static final String QUEUE_NAME = "internal.queue";
    private static final String ROUTING_KEY_NAME = "internal.routing.key";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, false, false, true, null);
        channel.exchangeDeclare(EXCHANGE_NAME2, BuiltinExchangeType.DIRECT, false, false, false, null);
        channel.exchangeBind(EXCHANGE_NAME2, EXCHANGE_NAME, ROUTING_KEY_NAME);

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_NAME);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME2, ROUTING_KEY_NAME);


        channel.basicPublish(EXCHANGE_NAME2, ROUTING_KEY_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                "internal".getBytes());

        RabbitMqUtils.close(connection, channel);
    }
}
