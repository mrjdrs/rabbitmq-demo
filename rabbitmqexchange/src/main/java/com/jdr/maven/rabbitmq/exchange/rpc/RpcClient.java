package com.jdr.maven.rabbitmq.exchange.rpc;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.text.MessageFormat.format;

/**
 * @author zhoude
 * @date 2019/11/14 10:27
 */
public class RpcClient {

    private static final String QUEUE_NAME = "rpc.queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        final Connection connection = RabbitMqUtils.getConnection();
        Channel channel = connection.createChannel();

        // 随机创建corrId
        final String collId = UUID.randomUUID().toString();
        // 客户端创建匿名队列，用于响应服务端请求
        String callbackQueueName = channel.queueDeclare().getQueue();

        // 客户端发送消息；使用默认exchange（exchange=""），允许通过routingKey指定message将被发送给哪个queue
        channel.basicPublish("", QUEUE_NAME, getBasicPublishProperties(collId, callbackQueueName),
                "hello world".getBytes());
        // 客户端接收服务端响应的消息
        channel.basicConsume(callbackQueueName, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (collId.equals(properties.getCorrelationId())) {
                    System.out.println(format("client recv message:{0}, corrId:{1}", new String(body), collId));
                } else {
                    System.out.println("不是本次请求的消息");
                }
            }
        });

        TimeUnit.SECONDS.sleep(1);

        RabbitMqUtils.close(connection, channel);
    }

    private static AMQP.BasicProperties getBasicPublishProperties(String corrId, String callbackQueueName) {
        return new AMQP.BasicProperties().builder()
                .correlationId(corrId)
                .replyTo(callbackQueueName).build();
    }
}
