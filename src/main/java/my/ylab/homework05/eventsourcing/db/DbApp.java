package my.ylab.homework05.eventsourcing.db;

import my.ylab.homework05.eventsourcing.api.PersonApi;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * DbApp
 *
 * <p>
 * DbApp
 *
 * @author KarinaMorozova
 * 01.04.2023
 */

public class DbApp {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
    }
}
