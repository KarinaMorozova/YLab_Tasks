package my.ylab.homework05.eventsourcing.api;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;


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
        // Тут пишем создание PersonApi, запуск и демонстрацию работы
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();
        PersonApi personApi = applicationContext.getBean(PersonApi.class);
        // пишем взаимодействие с PersonApi
    }
}

