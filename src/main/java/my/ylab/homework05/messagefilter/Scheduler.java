package my.ylab.homework05.messagefilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    Processor processor;

    public Scheduler(@Autowired Processor processor) {
        this.processor = processor;
    }

    public void start() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                processor.processSingleMessage();
                Thread.sleep(1000);
            }
            catch (InterruptedException iex) {
                iex.printStackTrace();
            }

        }
    }

}
