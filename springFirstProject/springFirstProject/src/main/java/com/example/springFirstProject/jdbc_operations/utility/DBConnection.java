package com.example.springFirstProject.jdbc_operations.utility;

import com.example.springFirstProject.jdbc_operations.exception.HandleSQLExceptions;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Logger;

public class DBConnection {

    private static Connection connection = null;
    private static final Logger LOGGER = Logger.getLogger(DBConnection.class.getName());

    public static Connection getConnection () {
        if (connection != null) return connection;
        Properties properties = new Properties();
        try (InputStream inputStream = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")){
            properties.load(inputStream);
            String url = properties.getProperty("spring.datasource.url");
            String username = properties.getProperty("spring.datasource.username");
            String password = properties.getProperty("spring.datasource.password");
            String driver = properties.getProperty("spring.datasource.driver-class-name");

//            Class.forName(driver);
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("connection established successfully. with url : " + url + "  user : " + username + " password : " + password);
        } catch (IOException | SQLException e) {
            HandleSQLExceptions.handleSqlException("DBConnection.class ", e, LOGGER);
        }
        return connection;
    }
}
