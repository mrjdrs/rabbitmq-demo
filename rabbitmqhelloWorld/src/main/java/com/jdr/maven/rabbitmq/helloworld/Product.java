package com.jdr.maven.rabbitmq.helloworld;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static com.jdr.maven.rabbitmq.helloworld.RabbitMqUtils.close;
import static com.jdr.maven.rabbitmq.helloworld.RabbitMqUtils.getConnection;

/**
 * rabbitmq hello world product
 *
 * @author zhoude
 * @date 2019/11/5 11:13
 */
public class Product {

    private static final String EXCHANGE_NAME = "exchange_hello_world";
    private static final String QUEUE_NAME = "hello_world_queue";
    private static final String ROUTING_KEY = "hello_world_routingKey";

    public static void main(String[] args) throws IOException, TimeoutException {
        // 创建连接
        Connection connection = getConnection();
        // 建立信道
        Channel channel = connection.createChannel();
        // 创建交换器
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        // 创建队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 绑定路由键
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        // 发送消息到mq
        channel.basicPublish(EXCHANGE_NAME,
                ROUTING_KEY,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                "Hello world".getBytes());
        // 关闭资源连接
        close(connection, channel);
    }
}
