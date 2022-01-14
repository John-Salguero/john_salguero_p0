package com.johnsbank.java.utilities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCConnection {

    {
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

        if(connection == null){

            Properties props = new Properties();
            try {
                props.load(JDBCConnection.class.getClassLoader().getResourceAsStream("credentials"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String endpoint = props.getProperty("endpoint");
            String username = props.getProperty("username");
            String pass = props.getProperty("password");

            try {
                connection = DriverManager
                        .getConnection(endpoint,
                                username, pass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        return connection;
    }

    public static JDBCConnection getInstance() {return instance;}
}
