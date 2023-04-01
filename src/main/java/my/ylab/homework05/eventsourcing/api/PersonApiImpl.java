package my.ylab.homework05.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import my.ylab.homework05.eventsourcing.Person;
import my.ylab.homework05.eventsourcing.message.MessageClassContainer;
import my.ylab.homework05.eventsourcing.message.MessageStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Component
public class PersonApiImpl implements PersonApi {
    private static final String FIND_BY_KEY = "select ps.* from person ps where ps.person_id = %d;";
    private static final String FIND_ALL = "select p.* from person p";
    private static final String QUEUE_NAME = "westeros_queue";
    @Autowired
    private DataSource dataSource;
    @Autowired
    private ConnectionFactory connectionFactory;

    @Override
    public void deletePerson(Long personId) {
        Person person = findPerson(personId);
        if (person != null) {
            MessageClassContainer container = new MessageClassContainer(person, MessageStatus.DELETE);
            sendMessage(container);
        } else {
            System.err.println("Была произведена попытка удаления не существующих данных");
        }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName){
        MessageStatus messageStatus;

        if (findPerson(personId) != null) {
            messageStatus = MessageStatus.UPDATE;
        } else {
            messageStatus = MessageStatus.INSERT;
        }

        Person person = new Person(personId, firstName, lastName, middleName);
        MessageClassContainer container = new MessageClassContainer(person, messageStatus);
        sendMessage(container);
    }

    private void sendMessage(MessageClassContainer messageClassContainer) {
        try (
            com.rabbitmq.client.Connection connection = this.connectionFactory.newConnection();
            Channel channel = connection.createChannel()
        ) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(messageClassContainer);

            channel.basicPublish("", QUEUE_NAME, null, jsonInString.getBytes());
        } catch (IOException | TimeoutException tex) {
            tex.printStackTrace();
        }
    }

    @Override
    public Person findPerson(Long personId) {
        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(FIND_BY_KEY, personId)) ) {
            if (rs.next()) {
                Person result = new Person();
                result.setId(rs.getLong("person_id"));
                result.setName(rs.getString("first_name"));
                result.setLastName(rs.getString("last_name"));
                result.setMiddleName(rs.getString("middle_name"));

                return result;
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return null;
    }

    @Override
    public List<Person> findAll() {
        List<Person> persons = new ArrayList<>();

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(FIND_ALL)) {

            while (rs.next()) {
                Long person_id = rs.getLong("person_id");
                String name = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String middleName = rs.getString("middle_name");
                persons.add(new Person(person_id, name, lastName, middleName));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return persons;
    }
}
