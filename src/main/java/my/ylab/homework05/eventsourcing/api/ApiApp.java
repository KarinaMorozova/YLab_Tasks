package my.ylab.homework05.eventsourcing.api;

import com.rabbitmq.client.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;


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
    @Autowired
    static DataSource dataSource;
    @Autowired
    static ConnectionFactory connectionFactory;
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApi.class, dataSource, connectionFactory);


        personApi.savePerson(1L, "Arya", "Stark", "Eddard");
    }
}

