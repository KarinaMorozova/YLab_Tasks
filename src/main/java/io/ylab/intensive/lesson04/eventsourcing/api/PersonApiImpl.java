package io.ylab.intensive.lesson04.eventsourcing.api;

import com.fasterxml.jackson.databind.ObjectMapper;


import com.rabbitmq.client.Channel;
import io.ylab.intensive.lesson04.eventsourcing.Person;
import io.ylab.intensive.lesson04.eventsourcing.message.MessageClassContainer;
import io.ylab.intensive.lesson04.eventsourcing.message.MessageStatus;

import javax.sql.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Тут пишем реализацию
 */
public class PersonApiImpl implements PersonApi {
    private String queue_name;
    private DataSource dataSource;
    private Channel channel;

    public PersonApiImpl(DataSource dataSource, Channel channel, String queue_name) {
        this.dataSource = dataSource;
        this.channel = channel;
        this.queue_name = queue_name;
    }

    @Override
    public void deletePerson(Long personId) {
        Person person = findPerson(personId);
        if (person != null) {
            MessageClassContainer container = new MessageClassContainer(person, MessageStatus.DELETE);
            sendMessage(container);
        }
        else {
            System.err.println("Была произведена попытка удаления не существующих данных");
        }
    }

    @Override
    public void savePerson(Long personId, String firstName, String lastName, String middleName){
        MessageStatus messageStatus;

        if (findPerson(personId) != null) {
            messageStatus = MessageStatus.UPDATE;
        }
        else {
            messageStatus = MessageStatus.INSERT;
        }

        Person person = new Person(personId, firstName, lastName, middleName);
        MessageClassContainer container = new MessageClassContainer(person, messageStatus);
        sendMessage(container);
    }

    private void sendMessage(MessageClassContainer messageClassContainer) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonInString = mapper.writeValueAsString(messageClassContainer);

            channel.basicPublish("", this.queue_name, null, jsonInString.getBytes());
        }
        catch (IOException ex) {
            System.err.format("Error: %s", ex.getMessage());
        }
    }

    @Override
    public Person findPerson(Long personId) {
        String findPersonByKey = "select ps.* from person ps where ps.person_id = %d;";

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(findPersonByKey, personId)) ) {
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

        String selectStr = "select p.* from person p";

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(selectStr)) {

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
