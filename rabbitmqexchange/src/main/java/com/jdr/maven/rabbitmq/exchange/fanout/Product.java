package com.jdr.maven.rabbitmq.exchange.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.jdr.maven.rabbitmq.exchange.RabbitMqUtils.*;

/**
 * rabbitmq hello world product
 *
 * @author zhoude
 * @date 2019/11/5 11:13
 */
public class Product {

    private static final String EXCHANGE_NAME = "exchange";
    private static final String QUEUE_NAME = "exchange_queue1";
    private static final String QUEUE_NAME2 = "exchange_queue2";
    private static final String ROUTING_KEY = "exchange_routingKey";
    private static final String ROUTING_KEY2 = "exchange_routingKey2";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);

        // 定义队列1、队列2
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueDeclare(QUEUE_NAME2, false, false, false, null);
        // 将队列1、队列2绑定到同一个fanout交换器上，但路由键不一样
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        channel.queueBind(QUEUE_NAME2, EXCHANGE_NAME, ROUTING_KEY2);

        // 仅发送路由键1的消息，但因绑定键是fanout类型，所以队列1和队列2都会受到消息
        channel.basicPublish(EXCHANGE_NAME,
                ROUTING_KEY,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                "Hello world".getBytes());

        close(connection, channel);
    }
}
