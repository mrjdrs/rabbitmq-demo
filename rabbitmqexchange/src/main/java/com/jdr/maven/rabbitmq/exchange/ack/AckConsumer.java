package com.jdr.maven.rabbitmq.exchange.ack;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.GetResponse;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author zhoude
 * @date 2019/11/11 13:55
 */
public class AckConsumer {

    private static final String QUEUE_NAME = "ack.queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = RabbitMqUtils.getConnection();
        final Channel channel = connection.createChannel();

        // 因为basicConsumer会消费队列中所有的消息，这样不方便演示basicAck中multiple参数的效果，故用basicGet来消费消息
        GetResponse getResponse = channel.basicGet(QUEUE_NAME, false);
        System.err.println(getResponse.getEnvelope().getDeliveryTag() + ": " + new String(getResponse.getBody()));
        System.err.println("-------------------------");

        GetResponse getResponse2 = channel.basicGet(QUEUE_NAME, false);
        System.err.println(getResponse2.getEnvelope().getDeliveryTag() + ": " + new String(getResponse2.getBody()));
        System.err.println("-------------------------");

        TimeUnit.SECONDS.sleep(10);

        GetResponse getResponse3 = channel.basicGet(QUEUE_NAME, false);
        System.err.println(getResponse3.getEnvelope().getDeliveryTag() + ": " + new String(getResponse3.getBody()));
        channel.basicAck(getResponse3.getEnvelope().getDeliveryTag(), false);
        System.err.println("-------------------------");

        // 休眠10秒，方便看到multiple参数的效果
        TimeUnit.SECONDS.sleep(10);

        GetResponse getResponse4 = channel.basicGet(QUEUE_NAME, false);
        System.err.println(getResponse4.getEnvelope().getDeliveryTag() + ": " + new String(getResponse4.getBody()));
        channel.basicAck(getResponse4.getEnvelope().getDeliveryTag(), true);

        RabbitMqUtils.close(connection, channel);
    }
}
