package my.ylab.homework05.eventsourcing.db;

import my.ylab.homework04.eventsourcing.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * DbService
 * <p>
 * Сервис работы с базой данных путем обработки сообщений брокера
 *
 * @author Karina Morozova
 * 01.04.2023
 */
@Component
public class DbService {
    private static final String REMOVE_BY_ID = "delete from person ps where ps.person_id = ?;";
    private static final String INSERT_INFO = "insert into person (person_id, first_name, last_name, middle_name) values (?,?,?,?);";
    private static final String UPDATE_INFO = "update person set first_name = ?, last_name = ?, middle_name = ? " +
            "where person_id = ?;";
    private DataSource dataSource;
    public DbService(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void delete(Person person) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_BY_ID)
        ) {
            preparedStatement.setLong(1, person.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public void insert(Person person) {
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INFO) ) {
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
        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INFO)
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
