package my.ylab.homework04.eventsourcing.api;

import my.ylab.homework04.eventsourcing.EndPoint;
import my.ylab.homework04.eventsourcing.Person;

import java.util.List;

public class ApiApp extends EndPoint {
    public ApiApp(String queue_name) {
        super(queue_name);
    }

    public static void main(String[] args) throws InterruptedException {
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        ApiApp apiApp = new ApiApp("westeros_queue");

        PersonApi personApi = new PersonApiImpl(apiApp.dataSource, apiApp.channel, apiApp.queue_name);

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
