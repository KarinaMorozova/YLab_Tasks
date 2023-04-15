package io.ylab.intensive.lesson04.persistentmap;


import io.ylab.intensive.lesson04.DbUtil;

import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;

public class PersistentMapTest {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDb();
        PersistentMap persistentMap = new PersistentMapImpl(dataSource);

        // Написать код демонстрации работы
        playSomeData(persistentMap);
    }

    private static void playSomeData(PersistentMap persistentMap) {
        try {
            persistentMap.init("Westeros");
            persistentMap.put("Arya Stark", "Winterfell");
            persistentMap.put("Sansa Stark", "Winterfell");
            persistentMap.put("Jon Snow", "Winterfell");

            persistentMap.put("Cersei Lannister", "Casterly Rock");
            persistentMap.put("Jaime Lannister", "Casterly Rock");

            persistentMap.init("Braavos");
            persistentMap.put("Jaqen Hghar", "Lorath");

            persistentMap.init("Westeros");

            System.out.println("Ключи от Вестероса у: " );
            List<String> players = persistentMap.getKeys();
            for (String plr: players) {
                System.out.println("Игрок " + plr);
            }
            persistentMap.remove("Jaime Lannister");

            System.out.println();
            String player = "Arya Stark";
            System.out.println("Локация игрока Арья Старк: " + persistentMap.get(player));

            String isExists = (persistentMap.containsKey("Cersei Lannister")) ? "Да" : "Нет";
            System.out.println("Есть ли игрок Серсея Ланнистер: " + isExists);

            isExists = (persistentMap.containsKey("Tywin Lannister")) ? "Да" : "Нет";
            System.out.println("Есть ли игрок Тайвин Ланнистер: " + isExists);

            System.out.println();
            System.out.println("Очистить карту");
            System.out.println();

            persistentMap.init("Braavos");
            System.out.println("Ключи от Браавоса у: " );
            players = persistentMap.getKeys();
            for (String plr: players) {
                System.out.println("Игрок " + plr);
            }

            persistentMap.clear();


        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    public static DataSource initDb() throws SQLException {
        String createMapTable = ""
                + "drop table if exists persistent_map; "
                + "CREATE TABLE if not exists persistent_map (\n"
                + "   map_name varchar,\n"
                + "   KEY varchar,\n"
                + "   value varchar\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMapTable, dataSource);
        return dataSource;
    }
}