package my.ylab.homework04.eventsourcing.db;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.GetResponse;
import my.ylab.homework04.eventsourcing.EndPoint;
import my.ylab.homework04.eventsourcing.Person;
import my.ylab.homework04.eventsourcing.message.MessageClassContainer;
import my.ylab.homework04.eventsourcing.message.MessageStatus;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DbApp extends EndPoint {
    public DbApp(String queue_name) {
        super(queue_name);
    }

    public static void main(String[] args){
        DbApp dbApp = new DbApp("westeros_queue");

        dbApp.listen();
    }

    private void listen() {
        try {
            // тут пишем создание и запуск приложения работы с БД
            while (!Thread.currentThread().isInterrupted()) {
                GetResponse message = this.channel.basicGet(this.queue_name, true);
                if (message == null) {
                    // no messages
                } else {
                    String received = new String(message.getBody());
                    System.out.println(received);

                    ObjectMapper objectMapper = new ObjectMapper();
                    MessageClassContainer container = objectMapper.readValue(received, MessageClassContainer.class);

                    if (container.getMessageStatus() == MessageStatus.DELETE) {
                        delete(container.getPerson());
                    }  else if (container.getMessageStatus() == MessageStatus.INSERT) {
                        insert(container.getPerson());
                    } else {
                        update(container.getPerson());
                    }
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void delete(Person person) {
        String removeByKey = "delete from person ps where ps.person_id = ?;";

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(removeByKey)
        ) {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    private void insert(Person person) {
        String insertStr = "insert into person (person_id, first_name, last_name, middle_name) values (?,?,?,?);";

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertStr) ) {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.setString(2, person.getName());
            preparedStatement.setString(3, person.getLastName());
            preparedStatement.setString(4, person.getMiddleName());

            int row = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    private void update(Person person) {
        String updateStr = "update person set first_name = ?, last_name = ?, middle_name = ? " +
                "where person_id = ?;";

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateStr)
        ) {
            preparedStatement.setString(1, person.getName());
            preparedStatement.setString(2, person.getLastName());
            preparedStatement.setString(3, person.getMiddleName());
            preparedStatement.setLong(4, person.getId());

            int row = preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

}
