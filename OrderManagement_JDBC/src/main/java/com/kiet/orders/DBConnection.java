package com.kiet.orders;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new IllegalStateException("db.properties file not found.");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        String url = properties.getProperty("db.url");
        String username = properties.getProperty("db.username");
        String password = properties.getProperty("db.password");

        if (password == null || password.equals("YOUR_MYSQL_PASSWORD")) {
            throw new IllegalStateException(
                    "Set your actual MySQL password in src/main/resources/db.properties");
        }

        return DriverManager.getConnection(url, username, password);
    }
}
