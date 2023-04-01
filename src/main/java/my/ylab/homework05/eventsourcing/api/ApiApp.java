package my.ylab.homework05.eventsourcing.api;

import my.ylab.homework05.eventsourcing.Person;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.List;


/**
 * ApiApp
 *
 * <p>
 * Гырк мырк пырк
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

        List<Person> list = personApi.findAll();
        for (Person p: list) {
            System.out.println(p);
        }
    }
}

