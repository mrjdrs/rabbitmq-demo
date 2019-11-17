package com.jdr.maven.rabbitmq.exchange.rpc;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import static java.text.MessageFormat.format;

/**
 * @author zhoude
 * @date 2019/11/14 10:27
 */
public class RpcServer {

    private static final String QUEUE_NAME = "rpc.queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = RabbitMqUtils.getRpcConnection();
        final Channel channel = connection.createChannel();
        // 创建请求处理队列，用于服务端接收客户端RPC请求
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        System.out.println("等待RPC请求...");

        // 服务端监听客户端发送的RPC请求
        channel.basicConsume(QUEUE_NAME, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String correlationId = properties.getCorrelationId();
                String message = "";

                try {
                    message = new String(body);
                    System.err.println(format("service recv message:{0}, corrId:{1}", message, correlationId));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    AMQP.BasicProperties props = new AMQP.BasicProperties.Builder()
                            .correlationId(correlationId)
                            .build();

                    // 使用默认exchange，允许通过routingKey指定message将被发送给哪个queue
                    channel.basicPublish("", properties.getReplyTo(), props, (message + "--is done.").getBytes("UTF-8"));
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        });
    }
}
