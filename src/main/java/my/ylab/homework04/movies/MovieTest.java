package my.ylab.homework04.movies;

import java.io.File;
import java.sql.SQLException;
import javax.sql.DataSource;

import my.ylab.homework04.DbUtil;

public class MovieTest {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = initDb();
        MovieLoader movieLoader = new MovieLoaderImpl(dataSource);

        File dataFile = new File("./src/main/java/my/ylab/homework04/movies/resources/film.csv");
        movieLoader.loadData(dataFile);

        /**
         * Для созданной таблицы написать запрос, выводящий количество фильмов
         * каждого жанра (GROUP BY). Запрос написать в комментариях к коду решения:
         *
         * select a.subject, count(a.id)
         * from movie a
         * group by a.subject
         */
    }

    private static DataSource initDb() throws SQLException {
        String createMovieTable = "drop table if exists movie;"
                + "CREATE TABLE IF NOT EXISTS movie (\n"
                + "\tid bigserial NOT NULL,\n"
                + "\t\"year\" int4,\n"
                + "\tlength int4,\n"
                + "\ttitle varchar,\n"
                + "\tsubject varchar,\n"
                + "\tactors varchar,\n"
                + "\tactress varchar,\n"
                + "\tdirector varchar,\n"
                + "\tpopularity int4,\n"
                + "\tawards bool,\n"
                + "\tCONSTRAINT movie_pkey PRIMARY KEY (id)\n"
                + ");";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(createMovieTable, dataSource);
        return dataSource;
    }
}