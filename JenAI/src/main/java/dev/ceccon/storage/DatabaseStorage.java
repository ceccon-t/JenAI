package dev.ceccon.storage;

import dev.ceccon.conversation.Chat;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseStorage implements Storage {

    private String port;
    private String username;
    private String password;

    public DatabaseStorage(String port, String username, String password) {
        this.port = port;
        this.username = username;
        this.password = password;

        try {
            Connection conn = getConnection();
            System.out.println("Connected successfully to Postgres database.");
        } catch (SQLException e) {
            System.out.println("Error trying to connect to database...");
        }
    }

    public DatabaseStorage() {
        this("", "", "");
    }

    private Connection getConnection() throws SQLException {
        String host = "localhost";
        String databaseName = "jenai";
        String url = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;

        return DriverManager.getConnection(url, username, password);
    }

    @Override
    public Chat load(String fileIdentifier) throws IOException {
        return null;
    }

    @Override
    public void save(Chat chat, String chosenIdentifier) throws IOException {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
