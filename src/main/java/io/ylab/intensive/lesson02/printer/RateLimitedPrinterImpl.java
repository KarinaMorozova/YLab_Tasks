package io.ylab.intensive.lesson02.printer;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RateLimitedPrinterImpl implements RateLimitedPrinter{
    private Long lastTime;
    private int interval;

    public RateLimitedPrinterImpl(int interval) {
        this.interval = interval;
        this.lastTime = 0L;
    }

    public void print(String message) {
        long currentTime = System.currentTimeMillis();

        if ((currentTime - this.lastTime) > this.interval) {
            Date currentDate = new Date(currentTime);
            DateFormat df = new SimpleDateFormat("HH:mm:ss");

            System.out.println("Время вывода " + message + " элемента: " + df.format(currentDate));
            this.lastTime = currentTime;
        }
    }
}
