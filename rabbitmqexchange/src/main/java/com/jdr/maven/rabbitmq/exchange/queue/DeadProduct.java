package com.jdr.maven.rabbitmq.exchange.queue;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 死信交换机
 *
 * @author zhoude
 * @date 2019/11/9 22:52
 */
public class DeadProduct {

    private static final String EXCHANGE_NAME = "normal.exchange";
    private static final String DEAD_EXCHANGE_NAME = "dead.exchange";
    private static final String QUEUE_NAME = "normal.queue";
    private static final String DEAD_QUEUE_NAME = "dead.queue";
    private static final String ROUTING_KEY_NAME = "normal.routing.key";
    private static final String DEAD_ROUTING_KEY_NAME = "dead.routing.key";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.exchangeDeclare(DEAD_EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(QUEUE_NAME, false, false, false, getArguments());
        channel.queueDeclare(DEAD_QUEUE_NAME, false, false, false, null);

        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_NAME);
        channel.queueBind(DEAD_QUEUE_NAME, DEAD_EXCHANGE_NAME, ROUTING_KEY_NAME);
        for (int i = 0; i < 10; i++) {
            String message = "死信交换机" + i;
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                    message.getBytes());
        }

        RabbitMqUtils.close(connection, channel);
    }

    private static Map<String, Object> getArguments() {
        Map<String, Object> result = new HashMap<String, Object>(5);
        result.put("x-message-ttl", 5000);
        result.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        result.put("x-dead-letter-routing-key", DEAD_ROUTING_KEY_NAME);
        return result;
    }
}
