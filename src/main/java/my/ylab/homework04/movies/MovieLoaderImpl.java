package my.ylab.homework04.movies;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;


public class MovieLoaderImpl implements MovieLoader {
    List<Movie> movies = new ArrayList<>();
    private DataSource dataSource;

    public MovieLoaderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void loadData(File file) {
        parseFile(file);
        importData();
    }

    private void parseFile(File csvFile) {
        try (FileReader fileReader = new FileReader(csvFile);
             BufferedReader reader = new BufferedReader(fileReader)) {

            String stringFromFile;
            int firstLines = 0;

            while ((stringFromFile = reader.readLine()) != null) {
                if (firstLines < 2) {
                    firstLines++;
                    continue;
                }

                String[] record = stringFromFile.split(";");
                Movie current = createMovie(record);
                movies.add(current);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private Movie createMovie(String[] record) {
        Movie movie = new Movie();

        movie.setYear((!record[0].equals("")) ? Integer.parseInt(record[0]) : null);
        movie.setLength((!record[1].equals("")) ? Integer.parseInt(record[1]) : null);

        movie.setTitle((!record[2].equals("")) ? record[2] : null);
        movie.setSubject((!record[3].equals("")) ? record[3] : null);

        movie.setActors((!record[4].equals("")) ? record[4] : null);
        movie.setActress((!record[5].equals("")) ? record[5] : null);
        movie.setDirector((!record[6].equals("")) ? record[6] : null);

        movie.setPopularity((!record[7].equals("")) ? Integer.parseInt(record[7]) : null);
        movie.setAwards(record[8].equals("Yes"));

        return movie;
    }

    private void importData() {
        String SQL_INSERT = "insert into movie (year, length, title, subject, "
                + "actors, actress, director, popularity, awards) values (?,?,?,?,?,?,?,?,?);";

        try (Connection connection = this.dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)
        ) {
            for (Movie movie: movies) {
                // preparedStatement.setNull(<index>, java.sql.Types.<тип>)
                if (movie.getYear() == null) {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                }
                else {
                    preparedStatement.setInt(1, movie.getYear());
                }

                if (movie.getLength() == null) {
                    preparedStatement.setNull(2, java.sql.Types.INTEGER);
                }
                else {
                    preparedStatement.setInt(2, movie.getLength());
                }

                preparedStatement.setString(3, movie.getTitle());
                preparedStatement.setString(4, movie.getSubject());

                preparedStatement.setString(5, movie.getActors());
                preparedStatement.setString(6, movie.getActress());
                preparedStatement.setString(7, movie.getDirector());

                if (movie.getPopularity() == null) {
                    preparedStatement.setNull(8, java.sql.Types.INTEGER);
                }
                else {
                    preparedStatement.setInt(8, movie.getPopularity());
                }
                preparedStatement.setBoolean(9, movie.getAwards());

                int row = preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        }

    }
}