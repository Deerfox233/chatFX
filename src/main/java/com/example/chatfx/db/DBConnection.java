package com.example.chatfx.db;


import java.sql.*;

public class DBConnection {
    private static String url = "jdbc:mysql://localhost:3306/chatFX?useUnicode=true&characterEncoding=utf8&useSSL=true";
    private static String dbUsername = "root";
    private static String dbPassword = "123456";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        return connection = DriverManager.getConnection(url, dbUsername, dbPassword);
    }

    public static void close(PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.close();
        connection.close();
    }

    public static void close(ResultSet resultSet, PreparedStatement preparedStatement) throws SQLException {
        resultSet.close();
        preparedStatement.close();
        connection.close();
    }
}
