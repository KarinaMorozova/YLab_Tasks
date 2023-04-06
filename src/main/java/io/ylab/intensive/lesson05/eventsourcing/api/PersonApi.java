package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PersonApi
 * <p>
 * Интерфейс управления пользователями
 * @author KarinaMorozova
 * 31.03.2023
 */

@Component
public interface PersonApi {
    /**
     * Отправляет сообщение брокеру об удалении пользователя с заданным Id.
     * id. Если данных по personId не найдено, выводить в лог сообщение,
     * что была попытка удаления, но при этом данные не найдены.
     * Без выбрасывания Exception
     *
     * @param personId id
     */
    void deletePerson(Long personId);

    /**
     * Отправляет сообщение брокеру о сохранении данных пользователя.
     * В случае нахождения пользователя в БД данные о нем
     * обновляются, в случае отсутствия - создается новый пользователь.
     *
     * @param personId   id
     * @param firstName  Фамилия
     * @param lastName   Имя
     * @param middleName Отчество
     */
    void savePerson(Long personId, String firstName, String lastName, String middleName);

    /**
     * Осуществляет прямой запрос в БД и возвращает данные пользователя,
     * если персона для данного personId найдена, null в обратном случае
     *
     * @param personId id
     * @return Person {@link Person}
     */
    Person findPerson(Long personId);

    /**
     * Осуществляет прямой запрос в БД и возвращает список пользователей,
     * зарегистрированных в базе данных.
     *
     * @return Список Person {@link Person}
     */
    List<Person> findAll();
}