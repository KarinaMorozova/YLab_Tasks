package my.ylab.homework05.eventsourcing.api;

import my.ylab.homework05.eventsourcing.Person;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
public interface PersonApi {
    void deletePerson(Long personId);

    void savePerson(Long personId, String firstName, String lastName, String middleName);

    Person findPerson(Long personId);

    List<Person> findAll();
}