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
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 排它队列
 *
 * @author zhoude
 * @date 2019/11/9 22:16
 */
public class Product {

    private static final String EXCHANGE_NAME = "exclusive.exchange";
    private static final String QUEUE_NAME = "exclusive.queue";
    private static final String ROUTING_KEY_NAME = "exclusive.routing.key";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(QUEUE_NAME, true, true, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_NAME);
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN,
                "排它队列".getBytes());

        Connection connection2 = RabbitMqUtils.getConnection();
        Channel channel2 = connection2.createChannel();
        channel2.basicConsume(QUEUE_NAME, new DefaultConsumer(channel2) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("收到消息：" + new String(body));
            }
        });

        // 线程休眠5秒，待消息回调完成
        TimeUnit.SECONDS.sleep(5);

        RabbitMqUtils.close(connection, channel);
    }
}
