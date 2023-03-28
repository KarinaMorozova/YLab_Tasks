package my.ylab.homework04.eventsourcing;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import my.ylab.homework04.DbUtil;
import my.ylab.homework04.RabbitMQUtil;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public abstract class EndPoint {
    protected String queue_name;

    protected DataSource dataSource;
    protected Channel channel;
    protected Connection connection;

    public EndPoint(String queue_name) {
        try {
            this.dataSource = initDb();
            this.queue_name = queue_name;

            ConnectionFactory factory = initMQ();
            factory.setHost("localhost");
            this.connection = factory.newConnection();
            this.channel = this.connection.createChannel();
            this.channel.queueDeclare(this.queue_name, false, false, false, null);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static ConnectionFactory initMQ() throws Exception {
        return RabbitMQUtil.buildConnectionFactory();
    }
    private static DataSource initDb() throws SQLException {
        String ddl = ""
                + "drop table if exists person;"
                + "create table if not exists person (\n"
                + "person_id bigint primary key,\n"
                + "first_name varchar,\n"
                + "last_name varchar,\n"
                + "middle_name varchar\n"
                + ")";
        DataSource dataSource = DbUtil.buildDataSource();
        DbUtil.applyDdl(ddl, dataSource);
        return dataSource;
    }

    public void close() throws IOException, TimeoutException {
        this.channel.close();
        this.connection.close();
    }
}
