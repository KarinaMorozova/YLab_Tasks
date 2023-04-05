package my.ylab.homework05.messagefilter;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Demo
 *
 * <p>
 * Модуль с демонстрационными данными, помещенными в очередь input
 *
 * @author KarinaMorozova
 * 02.04.2023
 */
@Component
public class Demo {
    private static final String[] DEMO_DATA_ARRAY = {"Fuck you, уважаемый!", "Ах, ты, сучка крашена!",
            "Жопа есть, а слова нет", "fuck fuck! fuck? fucker; fucking, fuck\n fucking\n !fucker! Fucking fucK",
            "По хую мне"};
    ConnectionFactory connectionFactory;
    String inputQueue;

    public Demo(@Autowired ConnectionFactory connectionFactory, @Autowired String inputQueue) {
        this.connectionFactory = connectionFactory;
        this.inputQueue = inputQueue;
    }

    public void demonstrate() {
        try (Connection connection = connectionFactory.newConnection();
             Channel channel = connection.createChannel()) {
            channel.queueDeclare(inputQueue, false, false, false, null);

            for (String demoString: DEMO_DATA_ARRAY) {
                channel.basicPublish("", inputQueue, null, demoString.getBytes());
            }
        } catch (IOException | TimeoutException tex) {
            tex.printStackTrace();
        }
    }
}
