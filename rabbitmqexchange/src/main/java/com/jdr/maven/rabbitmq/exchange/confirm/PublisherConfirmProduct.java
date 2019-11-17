package com.jdr.maven.rabbitmq.exchange.confirm;

import com.jdr.maven.rabbitmq.exchange.RabbitMqUtils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import static java.text.MessageFormat.format;

/**
 * @author zhoude
 * @date 2019/11/17 16:317
 */
public class PublisherConfirmProduct {

    private static final String EXCHANGE_NAME = "demo.exchange";
    private static final String ROUTING_KEY = "demo.routingkey";
    private static final String QUEUE_NAME = "demo.queue";
    private static final String MESSAGE = "Hello World！";

    /**
     * 单条确认
     */
    public static void commonConfirm() throws Exception {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = initChannel(connection);

        channel.confirmSelect();
        for (int i = 0; i < 100; i++) {
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, MESSAGE.getBytes());
            if (channel.waitForConfirms()) {
                // 逐条确认是否发送成功
                System.out.println("send success!");
            }
        }

        RabbitMqUtils.close(connection, channel);
    }

    /**
     * 批量确认
     */
    public static void batchConfirm() throws Exception {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = initChannel(connection);

        channel.confirmSelect();
        for (int i = 0; i < 100; i++) {
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, MESSAGE.getBytes());
        }

        // 批量确认是否发送成功，如果某一次确认失败这一批都要重新发送
        if (channel.waitForConfirms()) {
            System.out.println("send success!");
        }

        RabbitMqUtils.close(connection, channel);
    }

    /**
     * 异步确认
     */
    public static void asyncConfirm() throws Exception {
        Connection connection = RabbitMqUtils.getConnection();
        Channel channel = initChannel(connection);
        channel.basicQos(1);

        channel.confirmSelect();

        // 定义一个未确认消息集合
        final SortedSet<Long> unConfirmSet = Collections.synchronizedNavigableSet(new TreeSet<>());
        for (int i = 0; i < 100; i++) {
            channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, MESSAGE.getBytes());
            unConfirmSet.add(channel.getNextPublishSeqNo());
        }

        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.err.println(format("拒绝消息 deliveryTag:{0}, multiple:{1}", deliveryTag, multiple));
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.err.println(format("确认消息 deliveryTag:{0}, multiple:{1}", deliveryTag, multiple));
                if (multiple) {
                    // multiple为true，则deliveryTag之前的所有消息全部被确认
                    unConfirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    // 否则只确认一条消息
                    unConfirmSet.remove(deliveryTag);
                }
            }
        });

        TimeUnit.SECONDS.sleep(5);
        System.out.println(unConfirmSet.size());

        RabbitMqUtils.close(connection, channel);
    }

    private static Channel initChannel(Connection connection) throws IOException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);
        return channel;
    }

    public static void main(String[] args) throws Exception {
//        commonConfirm();
//        batchConfirm();
        asyncConfirm();
    }
}
