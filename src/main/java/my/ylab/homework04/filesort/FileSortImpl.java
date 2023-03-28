package my.ylab.homework04.filesort;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.sql.DataSource;

public class FileSortImpl implements FileSorter {
    private DataSource dataSource;

    private static final int BATCH_SIZE = 10000;

    public FileSortImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public File sort(File data) {
        // ТУТ ПИШЕМ РЕАЛИЗАЦИЮ
        long start = System.currentTimeMillis();

        List<Long> longs = readFromFile(data);
        long end = System.currentTimeMillis() - start;
        System.out.println("Чтение из файла за " + TimeUnit.MILLISECONDS.toMinutes(end) + ":" + TimeUnit.MILLISECONDS.toSeconds(end) % 60);

        start = System.currentTimeMillis();
        regularInsert(longs);
        end = System.currentTimeMillis() - start;
        System.out.println("Обычная вставка данных в БД за " + TimeUnit.MILLISECONDS.toMinutes(end) + ":" + TimeUnit.MILLISECONDS.toSeconds(end) % 60);

        removeData();

        start = System.currentTimeMillis();
        batchInsert(longs);
        end = System.currentTimeMillis() - start;
        System.out.println("Batch-вставка данных в БД за " + TimeUnit.MILLISECONDS.toMinutes(end) + ":" + TimeUnit.MILLISECONDS.toSeconds(end) % 60);

        start = System.currentTimeMillis();
        longs = sortData();
        end = System.currentTimeMillis() - start;
        System.out.println("Сортировка данных за " + TimeUnit.MILLISECONDS.toMinutes(end) + ":" + TimeUnit.MILLISECONDS.toSeconds(end) % 60);

        start = System.currentTimeMillis();
        File output = new File(data.getParent() + "\\output.txt");
        writeToFile(longs, output);
        end = System.currentTimeMillis() - start;
        System.out.println("Запись в файл за " + TimeUnit.MILLISECONDS.toMinutes(end) + ":" + TimeUnit.MILLISECONDS.toSeconds(end) % 60);

        return output;
    }

    private List<Long> readFromFile(File data) {
        List<Long> result = new ArrayList<>();

        try (FileReader fileReader = new FileReader(data);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String numStr;
            while ((numStr = reader.readLine()) != null) {
                result.add(Long.valueOf(numStr));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private void regularInsert(List<Long> longs) {
        String insertStr = "insert into numbers (val) values (?);";


        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertStr)
        ) {
            for (Long number: longs) {
                preparedStatement.setLong(1, number);
                preparedStatement.execute();
            }
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    private void batchInsert(List<Long> longs) {
        String insertStr = "insert into numbers (val) values (?);";

        try {
            Connection connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(insertStr)) {
                for (int i = 0; i < longs.size(); i++) {
                    preparedStatement.setLong(1, longs.get(i));
                    preparedStatement.addBatch();
                    if (i % BATCH_SIZE == 0 || i == longs.size() - 1) {
                        try {
                            int[] result = preparedStatement.executeBatch();
                            connection.commit();
                        } catch (BatchUpdateException bex) {
                            System.err.format("SQL State: %s\n%s", bex.getSQLState(), bex.getMessage());
                            connection.rollback();
                        }
                    }
                }
            }
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    private void removeData() {
        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement() ) {
            statement.execute("delete from numbers;");
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
    }

    private List<Long> sortData() {
        List<Long> result = new ArrayList<>();
        String sortByDesc = "select a.val from numbers a order by a.val desc;";

        try (Connection connection = this.dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(String.format(sortByDesc)) ) {

            while (rs.next()) {
                Long value = rs.getLong("val");
                result.add(value);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

        return result;
    }

    private void writeToFile(List<Long> list, File file) {
        try (PrintWriter pw = new PrintWriter(file)) {
            for (Long num: list) {
                pw.println(num);
            }
            pw.flush();
        }
        catch (IOException ex) {
            ex.getStackTrace();
        }
    }
}