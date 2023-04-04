package my.ylab.homework05.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.*;
import my.ylab.homework04.eventsourcing.message.MessageClassContainer;
import my.ylab.homework04.eventsourcing.message.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class DbController {
    ConnectionFactory connectionFactory;
    DbService dbService;
    String exchangeName;
    String queueName;
    String routingKey;

    public DbController(@Autowired ConnectionFactory connectionFactory, @Autowired DbService dbService,
                        @Autowired String exchangeName, @Autowired String queueName, @Autowired String routingKey) {
        this.connectionFactory = connectionFactory;
        this.dbService = dbService;
        this.queueName = queueName;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
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
