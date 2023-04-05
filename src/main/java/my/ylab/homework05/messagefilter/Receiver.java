package my.ylab.homework05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Receiver
 *
 * <p>
 * Модуль, осуществляющий получение сообщений из брокера
 *
 * @author KarinaMorozova
 * 02.04.2023
 */
@Component
public class Receiver {
    ConnectionFactory connectionFactory;
    String inputQueue;

    public Receiver(@Autowired ConnectionFactory connectionFactory, @Autowired String inputQueue) {
        this.connectionFactory = connectionFactory;
        this.inputQueue = inputQueue;
    }

    public String receive() {
        String received = "";
        try (Connection connection = connectionFactory.newConnection();
            Channel channel = connection.createChannel() ) {
            GetResponse message = channel.basicGet(inputQueue, true);
            if (message != null) {
                received = new String(message.getBody());
            }
        } catch (IOException | TimeoutException tex) {
            tex.printStackTrace();
        }

        return received;
    }
}
