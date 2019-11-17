package com.jdr.maven.rabbitmq.exchange;

import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author zhoude
 * @date 2019/11/5 11:21
 */
public abstract class RabbitMqUtils {

    public static Connection getConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory.newConnection();
    }

    public static Connection getRpcConnection() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory.newConnection(new Address[]{new Address("127.0.0.1", 5672)});
    }

    public static void close(Connection connection, Channel channel) throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }
}
