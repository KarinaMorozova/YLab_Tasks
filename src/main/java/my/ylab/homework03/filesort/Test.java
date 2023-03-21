package my.ylab.homework03.filesort;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        File dataFile = new Generator().generate("./src/main/java/my/ylab/homework03/filesort/resources/data.txt", 375_000_000);
        System.out.println(new Validator(dataFile).isSorted()); // false
        long mid = System.currentTimeMillis() - start;
        System.out.println("Генерация данных за " + TimeUnit.MILLISECONDS.toMinutes(mid) + ":" + TimeUnit.MILLISECONDS.toSeconds(mid) % 60);

        File sortedFile = new Sorter().sortFile(dataFile);
        System.out.println(new Validator(sortedFile).isSorted()); // true

        long end = System.currentTimeMillis() - start;

        System.out.println("Сортировка данных за " + TimeUnit.MILLISECONDS.toMinutes(end) + ":" + TimeUnit.MILLISECONDS.toSeconds(end) % 60);
    }
}
