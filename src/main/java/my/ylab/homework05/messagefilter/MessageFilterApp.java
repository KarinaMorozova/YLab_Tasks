package my.ylab.homework05.messagefilter;

import my.ylab.homework05.messagefilter.resources.Transfer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * MessageFilterApp
 *
 * <p>
 * Приложение - фильтр нецензурных слов, работающее через брокера сообщений
 *
 * @author KarinaMorozova
 * 01.04.2023
 */
public class MessageFilterApp {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(Config.class);
        applicationContext.start();

        Transfer transfer = applicationContext.getBean(Transfer.class);
        transfer.transferWordsToDB();

        Demo demo = applicationContext.getBean(Demo.class);
        demo.demonstrate();

        Scheduler scheduler = applicationContext.getBean(Scheduler.class);
        scheduler.start();
    }

}
