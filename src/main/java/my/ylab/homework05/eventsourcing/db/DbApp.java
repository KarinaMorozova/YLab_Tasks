package my.ylab.homework05.eventsourcing.db;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * DbApp
 *
 * <p>
 * Приложение, получающее сообщения из брокера и сохраняющее информацию из них в БД
 *
 * @author KarinaMorozova
 * 01.04.2023
 */

public class DbApp {
    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();

        DbController controller =  applicationContext.getBean(DbController.class);
        controller.listen();
    }
}
