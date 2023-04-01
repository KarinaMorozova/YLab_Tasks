package my.ylab.homework05.eventsourcing.db;

import my.ylab.homework04.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class DbService {
    @Autowired
    private DataSource dataSource;

    public void delete(Person person) {
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

    public void insert(Person person) {
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

    public void update(Person person) {
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
