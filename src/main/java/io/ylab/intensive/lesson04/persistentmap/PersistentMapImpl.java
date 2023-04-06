package io.ylab.intensive.lesson04.persistentmap;

import java.sql.*;
import java.util.*;
import javax.sql.DataSource;

/**
 * Класс, методы которого надо реализовать
 */
public class PersistentMapImpl implements PersistentMap {
    HashMap<String, String> dataMap;

    public Map<String, HashMap<String, String>> pMap;

    private String currentMapName;

    private DataSource dataSource;

    public PersistentMapImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void init(String name) {
        currentMapName = name;
        dataMap = new HashMap<>();
        pMap = new HashMap<>();

        String selectStr = "select * from persistent_map pm where pm.map_name = '%s'";

        try (Connection connection = this.dataSource.getConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(String.format(selectStr, currentMapName))) {

            while (rs.next()) {
                String key = rs.getString("key");
                String value = rs.getString("value");
                dataMap.put(key, value);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        pMap.put(name, dataMap);
    }

    @Override
    public boolean containsKey(String key) {
        String selectByKey = "select * from persistent_map pm where pm.map_name = '%s' and pm.key = '%s';";
        boolean result = false;

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             ResultSet rs = statement.executeQuery(String.format(selectByKey, currentMapName, key))) {

            rs.last();
            result = rs.getRow() > 0;
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return result;
    }

    @Override
    public List<String> getKeys() {
        List<String> resultList = new ArrayList<>();

        String selectStr = "select pm.* from persistent_map pm where pm.map_name = '%s';";

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(selectStr, currentMapName))) {

            while (rs.next()) {
                resultList.add(rs.getString("key"));
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return resultList;
    }

    @Override
    public String get(String key) throws SQLException {
        String result = null;

        String selectValueByKey = "select pm.* from persistent_map pm where pm.map_name = '%s' and pm.key = '%s';";

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(selectValueByKey, currentMapName, key)) ) {

            if (rs.next()) {
                result = rs.getString("value");
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return result;
    }

    @Override
    public void remove(String key) throws SQLException {
        String removeByKey = "delete from persistent_map pm where pm.map_name = '%s' and pm.key = '%s';";

        try (Connection connection = this.dataSource.getConnection();
            Statement statement = connection.createStatement() ) {
            statement.execute(String.format(removeByKey, currentMapName, key));

            init(currentMapName);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    @Override
    public void put(String key, String value) {
        String insertStr = "insert into persistent_map (map_name, key, value) values (?,?,?);";

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertStr)
        ) {
            remove(key);

            preparedStatement.setString(1, currentMapName);
            preparedStatement.setString(2, key);
            preparedStatement.setString(3, value);

            int row = preparedStatement.executeUpdate();

            init(currentMapName);
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    @Override
    public void clear() {
        dataMap.clear();
        pMap.clear();

        String deleteStr = "delete from persistent_map pm where pm.map_name = '%s';";
        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement() ) {
            statement.execute(String.format(deleteStr, currentMapName));

            init(currentMapName);
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }
}
