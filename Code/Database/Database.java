package Database;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        // Reuse the connection if it's already open
        if (connection != null && !connection.isClosed()) {
            return connection;
        }

        try {
            // 1) Load properties from config/db.properties
            Properties props = new Properties();
            try (FileInputStream fis = new FileInputStream("config/db.properties")) {
                props.load(fis);
            }

            // Note: keys are db.url, db.user, db.password
            String url = props.getProperty("db.url");
            String user = props.getProperty("db.user");
            String pass = props.getProperty("db.password");

            // 2) Optional: explicitly load driver (usually auto)
            // Class.forName("com.mysql.cj.jdbc.Driver");

            // 3) Open connection
            connection = DriverManager.getConnection(url, user, pass);
            System.out.println("✅ Connected to Railway MySQL");
            return connection;

        } catch (IOException e) {
            throw new SQLException("Failed to load DB config file", e);
        }
    }
}
