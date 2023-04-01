package my.ylab.homework05.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import my.ylab.homework04.eventsourcing.message.MessageClassContainer;
import my.ylab.homework04.eventsourcing.message.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

@Controller
public class DbController {
    private static final String QUEUE_NAME = "westeros_queue";
    @Autowired
    private ConnectionFactory connectionFactory;
    private DbService dbService;

    @Autowired
    public DbController(DbService dbService) {
        this.dbService = dbService;
    }

    public void listen() {
        try {
            Channel channel = connectionFactory.newConnection().createChannel();
            try {
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
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (IOException | TimeoutException etx) {
            etx.printStackTrace();
        }
    }
}
