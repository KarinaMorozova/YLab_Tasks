package io.ylab.intensive.lesson05.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;

import io.ylab.intensive.lesson05.eventsourcing.message.MessageClassContainer;
import io.ylab.intensive.lesson05.eventsourcing.message.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * DbController
 * <p>
 * Контроллер, прослушивающий сообщения брокера и
 * отсылающий команды на изменение в БД
 *
 * @author Karina Morozova
 * 01.04.2023
 */
@Component
public class DbController {
    private ConnectionFactory connectionFactory;
    private DbService dbService;
    private static final String queueName = "westeros-queue";
    private static final String exchangeName = "exc";
    private static final String routingKey = "key";

    public DbController(@Autowired ConnectionFactory connectionFactory, @Autowired DbService dbService) {
        this.connectionFactory = connectionFactory;
        this.dbService = dbService;
    }

    public void listen() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel() ) {
                channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
                channel.queueDeclare(queueName, true, false, false, null);
                channel.queueBind(queueName, exchangeName, routingKey, null);

                while (!Thread.currentThread().isInterrupted()) {
                    GetResponse message = channel.basicGet(queueName, true);
                    if (message != null) {
                        String received = new String(message.getBody());
                        System.out.println(received);

                        ObjectMapper objectMapper = new ObjectMapper();
                        MessageClassContainer container = objectMapper.readValue(received, MessageClassContainer.class);

                        if (container.getMessageStatus() == MessageStatus.DELETE) {
                            dbService.delete(container.getPerson());
                        } else if (container.getMessageStatus() == MessageStatus.INSERT) {
                            dbService.insert(container.getPerson());
                        } else {
                            dbService.update(container.getPerson());
                        }
                    }
                }
        } catch (IOException | TimeoutException tex) {
            tex.printStackTrace();
        }
    }
}
