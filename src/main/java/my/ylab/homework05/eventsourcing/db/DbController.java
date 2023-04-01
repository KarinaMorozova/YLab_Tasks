package my.ylab.homework05.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import my.ylab.homework04.eventsourcing.message.MessageClassContainer;
import my.ylab.homework04.eventsourcing.message.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Component
public class DbController {
    private static final String QUEUE_NAME = "westeros_queue";
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private DbService dbService;

    public void listen() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel() ) {
                while (!Thread.currentThread().isInterrupted()) {
                    GetResponse message = channel.basicGet(QUEUE_NAME, true);
                    if (message == null) {
                        // no messages
                    } else {
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
