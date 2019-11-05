package com.jdr.maven.rabbitmq.helloworld;

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

import static com.jdr.maven.rabbitmq.helloworld.RabbitMqUtils.close;
import static com.jdr.maven.rabbitmq.helloworld.RabbitMqUtils.getConnection;

/**
 * rabbitmq hello world consumer
 *
 * @author zhoude
 * @date 2019/11/5 11:35
 */
public class Consumer {

    private static final String EXCHANGE_NAME = "exchange_hello_world";
    private static final String QUEUE_NAME = "hello_world_queue";
    private static final String ROUTING_KEY = "hello_world_routingKey";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        // 创建连接
        Connection connection = getConnection();
        // 建立信道
        final Channel channel = connection.createChannel();
        // 消费消息
        channel.basicConsume(QUEUE_NAME, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.err.println("收到消息：" + new String(body));
                // 处理完消息后告诉服务器我已经收到消息了
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        });
        // 睡眠线程，用于模拟应用处理时间
        TimeUnit.SECONDS.sleep(2);
        // 关闭资源连接
        close(connection, channel);
    }
}
