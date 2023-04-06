package io.ylab.intensive.lesson04.filesort;

import io.ylab.intensive.lesson04.DbUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

public class FileSorterTest {
    public static void main(String[] args) throws SQLException, FileNotFoundException {
        DataSource dataSource = initDb();

        long start = System.currentTimeMillis();
        File data = generate("./src/main/java/io/ylab/intensive/lesson04/filesort/resources/data.txt", 1_000_000);
        long end = System.currentTimeMillis() - start;
        System.out.println("Генерация данных и запись в файл за " + TimeUnit.MILLISECONDS.toMinutes(end) + ":" + TimeUnit.MILLISECONDS.toSeconds(end) % 60);

        FileSorter fileSorter = new FileSortImpl(dataSource);
        File res = fileSorter.sort(data);

        System.out.println("Название файла с отсортированными данными: " + res.getName());
    }

    private static File generate(String name, int count) throws FileNotFoundException {
        Random random = new Random();
        File file = new File(name);
        try (PrintWriter pw = new PrintWriter(file)) {
            for (int i = 0; i < count; i++) {
                pw.println(random.nextLong());
            }
            pw.flush();
        }
        return file;
    }

    public static DataSource initDb() throws SQLException {
        String createSortTable = ""
                + "drop table if exists numbers;"
                + "CREATE TABLE if not exists numbers (\n"
                + "\tval bigint\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createSortTable, dataSource);
        return dataSource;
    }
}