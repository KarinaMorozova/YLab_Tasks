package my.ylab.homework05.messagefilter;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Processor
 *
 * <p>
 * Модуль основного процесса: выбор сообщения из очереди input,
 * фильтрация нецензурных слов через сравнение их с таблицей слов в БД,
 * запись отфильтрованного сообщения в очередь output
 *
 * @author KarinaMorozova
 * 02.04.2023
 */
@Component
public class Processor {
    public static final String SELECT_SQL = "select name from words where name = ?;";
    Sender sender;
    Receiver receiver;
    ConnectionFactory connectionFactory;
    DataSource dataSource;

    public Processor (@Autowired Sender sender, @Autowired Receiver receiver,
                      @Autowired ConnectionFactory connectionFactory, @Autowired DataSource dataSource) {
        this.sender = sender;
        this.receiver = receiver;
        this.connectionFactory = connectionFactory;
        this.dataSource = dataSource;
    }

    public void processSingleMessage() {
        String inputString = receiver.receive();

        if (inputString.isEmpty()) {
            return;
        }

        String[] inputWords = inputString.split("\\s");
        StringBuilder sb = new StringBuilder();

        for (String word: inputWords) {
            word = word.trim();
            String noLetters = word.replaceAll("[A-Za-zА-яЁё]", "");
            String tempWord = word.replaceAll("[.,:;?!\n]", "");

            try (Connection connection = this.dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SELECT_SQL) ){

                preparedStatement.setString(1, tempWord.toLowerCase());
                ResultSet rs = preparedStatement.executeQuery();

                if (rs.next()) {
                    sb.append(tempWord.charAt(0)).append("*".repeat(tempWord.length() - 2)).append(tempWord.charAt(tempWord.length() - 1));
                    sb.append(noLetters).append(" ");
                }
                else {
                    sb.append(word).append(" ");
                }
            } catch (SQLException e) {
                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            }
        }

        sender.send(sb.toString().trim());
    }

}
