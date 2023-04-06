package io.ylab.intensive.lesson05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Sender
 *
 * <p>
 * Модуль, отсылающий сообщения в брокер
 *
 * @author KarinaMorozova
 * 02.04.2023
 */
@Component
public class Sender {
    ConnectionFactory connectionFactory;
    String outputQueue;

    public Sender(@Autowired ConnectionFactory connectionFactory, @Autowired String outputQueue) {
        this.connectionFactory = connectionFactory;
        this.outputQueue = outputQueue;
    }

    public void send(final String toSend) {
        try (Connection connection = this.connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(outputQueue, false, false, false, null);

            System.out.println(toSend);
            channel.basicPublish("", outputQueue, null, toSend.getBytes());
        } catch (IOException | TimeoutException tex) {
            tex.printStackTrace();
        }
    }
}
