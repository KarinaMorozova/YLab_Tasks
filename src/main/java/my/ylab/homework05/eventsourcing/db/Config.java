package my.ylab.homework05.eventsourcing.db;

import javax.sql.DataSource;

import com.rabbitmq.client.ConnectionFactory;
import my.ylab.homework05.DbUtil;
import org.postgresql.ds.PGSimpleDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("my.ylab.homework05.eventsourcing.db")
public class Config {
    static final String exchangeName = "exc";
    static final String queueName = "westeros-queue";
    static final String routingKey = "key";

    @Bean
    public DataSource dataSource() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setServerName("localhost");
        dataSource.setUser("postgres");
        dataSource.setPassword("postgres");
        dataSource.setDatabaseName("postgres");
        dataSource.setPortNumber(5432);

        String ddl = ""
                + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ")";

        DbUtil.applyDdl(ddl, dataSource);

        return dataSource;
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setVirtualHost("/");
        return connectionFactory;
    }
    @Bean
    public String exchangeName() {
        return exchangeName;
    }

    @Bean
    public String queueName() {
        return queueName;
    }

    @Bean
    public String routingKey() {
        return routingKey;
    }
}
