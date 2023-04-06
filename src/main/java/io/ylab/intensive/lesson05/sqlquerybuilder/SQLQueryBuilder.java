package io.ylab.intensive.lesson05.sqlquerybuilder;

import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

/**
 * SQLQueryBuilder
 *
 * <p>
 * Интерфейс извлечения данных о таблицах
 *
 * @author KarinaMorozova
 * 01.04.2023
 */
@Component
public interface SQLQueryBuilder {
    /**
     * Проверяет есть ли запрошенная таблица в БД
     * Если нет - метод возвращает null.
     * Если есть - метод возвращает строку запроса SELECT с выбором всех полей таблицы
     *
     * @param tableName название таблицы
     * @return String
     */
    String queryForTable(String tableName) throws SQLException;

    /**
     * Проверяет есть ли запрошенная таблица в БД
     * Если нет - метод возвращает null.
     * Если есть - метод возвращает строку запроса SELECT с выбором всех полей таблицы
     *
     *  @return Список строк
     */
    List<String> getTables() throws SQLException;
}
