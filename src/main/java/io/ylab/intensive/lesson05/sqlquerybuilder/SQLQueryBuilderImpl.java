package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLQueryBuilderImpl
 * <p>
 * Реализация интерфейса извлечения данных о таблицах {@link SQLQueryBuilder}
 *
 * @author Karina Morozova
 * 01.04.2023
 */
@Component
public class SQLQueryBuilderImpl implements  SQLQueryBuilder {
    private static final String SELECT_TBL = "select * from %s;";
    private static final String TABLE_LIST = "select table_name from information_schema.tables\n" +
            "where table_schema not in ('information_schema','pg_catalog');";
    private static final String SELECT = "select";
    private static final String FROM = "from";
    private DataSource dataSource;

    @Autowired
    public SQLQueryBuilderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String queryForTable(String tableName) {
        String result = null;
        StringBuilder sb = new StringBuilder();

        try (Connection connection = this.dataSource.getConnection();
             ResultSet rs = connection.getMetaData().getTables(null, null, tableName, null);
             Statement statement = connection.createStatement() ) {

            if (rs.next()) {
                sb.append(SELECT).append(" ");
                ResultSet resultSet = statement.executeQuery(String.format(SELECT_TBL, tableName));

                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    sb.append(columnName).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                sb.append(" ").append(FROM).append(" ").append(tableName);

                result = sb.toString();
            }
        }
        catch (SQLException qex) {
            System.err.format("SQL State: %s\n%s", qex.getSQLState(), qex.getMessage());
        }

        return result;
    }

    @Override
    public List<String> getTables() {
        List<String> resultList = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(TABLE_LIST)) {

            while (rs.next()) {
                resultList.add(rs.getString("table_name"));
            }
        }
        catch (SQLException qex) {
            System.err.format("SQL State: %s\n%s", qex.getSQLState(), qex.getMessage());
        }

        return resultList;
    }
}
