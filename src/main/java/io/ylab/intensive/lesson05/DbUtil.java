package io.ylab.intensive.lesson05;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;

import org.postgresql.ds.PGSimpleDataSource;

public class DbUtil {
    public static void applyDdl(String ddl, DataSource dataSource) {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(ddl);
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    /*
     * Настройки подключения НЕ МЕНЯЕМ!
     * Надо настроить БД таким образом, чтобы она работала со следующими
     * настройками
     */
    public static DataSource buildDataSource() throws SQLException {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);
        dataSource.getConnection().close();
        return dataSource;
    }
}
