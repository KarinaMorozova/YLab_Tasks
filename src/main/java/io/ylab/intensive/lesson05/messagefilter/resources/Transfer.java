package io.ylab.intensive.lesson05.messagefilter.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Transfer
 *
 * <p>
 * Модуль переноса фильтрационных слов из файла в БД
 *
 * @author KarinaMorozova
 * 02.04.2023
 */
@Component
public class Transfer {
    private static final String ACTUAL_TABLE_NAME = "words";
    private static final String CREATE_TABLE_SQL = "create table words (ID bigserial NOT NULL, name varchar(50));";
    private static final String REMOVE_DATA_SQL = "delete from words;";
    private static final String INSERT_SQL = "insert into words (name) values (?);";
    private static final int BATCH_SIZE = 10000;
    DataSource dataSource;

    public Transfer(@Autowired DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void transferWordsToDB() {
        System.out.println("Укажите полный путь к файлу словаря для фильтрации");
        try (Scanner scanner = new Scanner(System.in)) {
            String input = scanner.nextLine();

            System.out.println("Укажите полный путь и название файла вывода");
            String output = scanner.nextLine();
            if (convert(input, output)) {
                sendToDB(output);
            }
        }
    }

    public boolean convert(String input, String output) {
        boolean result = false;
        List<String> list = new ArrayList<>();

        try (FileReader fileReader = new FileReader(input);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String wordLine;
            String[] words;
            while ((wordLine = reader.readLine()) != null) {
                words = wordLine.split(",");

                for (String w: words) {
                    if (w.length() == w.replaceAll(" .,:;?!\n", "").length()) {
                        list.add(w.trim());
                    }
                }
            }

            File outputFile = new File(output);
            if (outputFile.createNewFile()) {
                try (PrintWriter pw = new PrintWriter(output)) {
                    for (String s : list) {
                        pw.println(s);
                    }
                    pw.flush();

                    result = true;
                } catch (IOException ex) {
                    ex.getStackTrace();
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    private void sendToDB(String output) {
        prepareTable();
        List<String> words = readFromFile(output);
        transfer(words);
    }

    private void prepareTable() {
        boolean isExist;

        try {
            Connection connection = this.dataSource.getConnection();
            ResultSet rs = connection.getMetaData().getTables(null, null, ACTUAL_TABLE_NAME, null);
            isExist = rs.next();

            if (!isExist) {
                Statement statement = connection.createStatement();
                statement.execute(CREATE_TABLE_SQL);

                statement.close();
            }

            Statement stmt = connection.createStatement();
            stmt.execute(REMOVE_DATA_SQL);

            stmt.close();
            rs.close();
            connection.close();
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }
     }

    private List<String> readFromFile(String output) {
        List<String> words = new ArrayList<>();

        try (FileReader fileReader = new FileReader(output);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String stringFromFile;
            while ((stringFromFile = reader.readLine()) != null) {
                words.add(stringFromFile);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return words;
    }

    private void transfer(List<String> words) {
        Connection connection = null;
        try {
            connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);

            try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_SQL)) {
                for (int i = 0; i < words.size(); i++) {
                    preparedStatement.setString(1, words.get(i));
                    preparedStatement.addBatch();

                    if (i % BATCH_SIZE == 0 || i == words.size() - 1) {
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

        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
