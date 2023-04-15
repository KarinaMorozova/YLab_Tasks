package io.ylab.intensive.lesson05.sqlquerybuilder;

import java.sql.SQLException;
import java.util.List;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * SQLQueryExtenderTest
 *
 * <p>
 * Приложение, собирающее информацию о таблицах базы данных
 * и выводящее селективные запросы из таблиц со списком всех полей
 *
 * @author KarinaMorozova
 * 01.04.2023
 */
public class SQLQueryExtenderTest {
    public static void main(String[] args) throws SQLException {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        SQLQueryBuilder queryBuilder = applicationContext.getBean(SQLQueryBuilder.class);

        List<String> tables = queryBuilder.getTables();

        // вот так сгенерируем запросы для всех таблиц что есть в БД
        for (String tableName : tables) {
            System.out.println(queryBuilder.queryForTable(tableName));
        }
    }
}
