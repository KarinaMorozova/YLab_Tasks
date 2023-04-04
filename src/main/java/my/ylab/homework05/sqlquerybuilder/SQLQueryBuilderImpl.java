package my.ylab.homework05.sqlquerybuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Component
public class SQLQueryBuilderImpl implements  SQLQueryBuilder {
    private static final String EXIST_TBL = "SELECT * FROM pg_tables WHERE tablename = '%s';";
    private static final String SELECT_TBL = "SELECT * FROM %s;";
    private static final String TABLE_LIST = "SELECT table_name FROM information_schema.tables\n" +
            "WHERE table_schema NOT IN ('information_schema','pg_catalog');";

    private DataSource dataSource;

    public SQLQueryBuilderImpl(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public String queryForTable(String tableName) {
        String result = null;
        StringBuilder sb = new StringBuilder();

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(EXIST_TBL, tableName))) {

            if (rs.next()) {
                ResultSet resultSet = statement.executeQuery(String.format(SELECT_TBL, tableName));
                sb.append("SELECT ");

                int columnCount = resultSet.getMetaData().getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = resultSet.getMetaData().getColumnName(i);
                    sb.append(columnName).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length());
                sb.append(" FROM ").append(tableName);

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
