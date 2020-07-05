package banking;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
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

    private static Connection getConnection() {
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
        Connection connection = Database.getConnection();
        Objects.requireNonNull(connection);

        String query = "create table if not exists card (" +
                "id INTEGER primary key," +
                "number TEXT," +
                "pin TEXT," +
                "balance INTEGER DEFAULT 0)";
        try (Statement statement = connection.createStatement()){
            statement.executeUpdate(query);
            connection.commit();
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
        Connection connection = Database.getConnection();
        Objects.requireNonNull(connection);

        String query = String.format("insert into card (number, pin) values (%s, %s)",
                bankAccount.getCardNumber(), bankAccount.getPin());
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Database.closeConnection(connection);
    }

    public static BankAccount load(String cardNumber, int pinCode) {
        Connection connection = Database.getConnection();
        Objects.requireNonNull(connection);

        String query = String.format("select number, pin, balance from card where number = %s and pin = %s",
                cardNumber, pinCode);
        try (Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query)){
            boolean found = resultSet.next();

            if (found) {
                BankAccount bankAccount = new BankAccount();
                bankAccount.setBalance(resultSet.getInt("balance"));
                bankAccount.setCardNumber(resultSet.getString("number"));
                bankAccount.setPin(Integer.parseInt(resultSet.getString("pin")));
                return bankAccount;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
