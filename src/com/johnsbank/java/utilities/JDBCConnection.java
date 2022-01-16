package com.johnsbank.java.utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {

    {
        // Upon loading the class into memory, load the driver for JDBC as well
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("The driver for JDBC was not found!", e);
        }
    }

    private static final JDBCConnection instance = new JDBCConnection();
    private Connection connection;

    private JDBCConnection() { /* Prevents anyone from instantiating */}

    public Connection getConnection() {

        try {
        if(connection == null || connection.isValid(30)){

            Properties props = new Properties();
            try {
                props.load(JDBCConnection.class.getClassLoader().getResourceAsStream("credentials"));
            } catch (IOException e) {
                throw new RuntimeException("You do not have the correct Credentials to access the database!", e);
            }

            String endpoint = props.getProperty("endpoint");
            String username = props.getProperty("username");
            String pass = props.getProperty("password");

            connection = DriverManager
                    .getConnection(endpoint,
                            username, pass);
        }
        }catch (SQLException e) {
            return null; // can't establish a connection
        }

        return connection;
    }

    /* Adheres to the Singleton Pattern */
    public static JDBCConnection getInstance() {return instance;}
}
