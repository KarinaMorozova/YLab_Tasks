package io.ylab.intensive.lesson05.eventsourcing.api;

import io.ylab.intensive.lesson05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;

/**
 * ApiApp
 *
 * <p>
 * Приложение, работающее с базой данных через брокера сообщений
 *
 * @author KarinaMorozova
 * 31.03.2023
 */

public class ApiApp {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApi.class);

        personApi.savePerson(1L, "Arya", "Stark", "Eddard");
        Thread.sleep(1000);

        personApi.savePerson(2L, "Sansa", "Stark", "Eddard");
        Thread.sleep(1000);

        personApi.savePerson(3L, "Cersei", "Lannister", "Tywin");
        Thread.sleep(1000);

        System.out.println("List of players");
        List<Person> persons = personApi.findAll();
        for (Person p: persons) {
            System.out.println(p);
        }

        personApi.savePerson(2L, "Jon", "Snow", "Rhaegar");
        Thread.sleep(1000);

        personApi.deletePerson(3L);
        Thread.sleep(1000);

        System.out.println("Updated list of players");
        persons = personApi.findAll();
        for (Person p: persons) {
            System.out.println(p);
        }

        Thread.sleep(500);
        personApi.deletePerson(4L);
    }
}

