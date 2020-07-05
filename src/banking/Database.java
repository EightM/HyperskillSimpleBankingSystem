package banking;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

public class Database {

    private static String url;

    private Database() {
    }

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String url) {
        Database.url = url;
    }

    private static Connection getConnection(String url) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url);
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }

    private static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void init(String dbFilename) {
        setUrl("jdbc:sqlite:./" + dbFilename);
        createDbFile(dbFilename);
        createCardTable();
    }

    private static void createCardTable() {
        Connection connection = Database.getConnection(url);
        Objects.requireNonNull(connection);

        String query = "create table if not exists card (" +
                "id integer," +
                "number text," +
                "pin text," +
                "balance integer default 0)";
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Database.closeConnection(connection);
    }

    private static void createDbFile(String dbFilename) {
        Path dbPath = Paths.get(String.format("./%s", dbFilename));
        if (!Files.exists(dbPath)) {
            try {
                Files.createFile(dbPath);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public static void save(BankAccount bankAccount) {
    }
}
