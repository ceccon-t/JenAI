package dev.ceccon.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.conversation.Chat;

import java.io.IOException;
import java.sql.*;

public class DatabaseStorage implements Storage {

    private ObjectMapper mapper = new ObjectMapper();

    private String port;
    private String username;
    private String password;

    private Connection conn;

    public DatabaseStorage(String port, String username, String password) {
        this.port = port;
        this.username = username;
        this.password = password;

        try {
            conn = getConnection();
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
    public Chat load(String chatName) throws IOException {
        System.out.println("Trying to load '" + chatName + "'");
        String sql = """
                SELECT * FROM chats
                    WHERE
                    name = ?
        """;

        Chat chat = null;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, chatName);

            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String content = rs.getString("content");
                chat = mapper.readValue(content, Chat.class);
            }
        } catch (SQLException e) {
            System.out.println("Error while trying to load chat '" + chatName + "': " + e);
            e.printStackTrace();
        }
        return chat;
    }

    @Override
    public void save(Chat chat, String chosenIdentifier) throws IOException {
        String sql = """
            INSERT INTO chats 
                (name, content)
            VALUES
                (?, ?)
        """;

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, chosenIdentifier);
            statement.setString(2, mapper.writeValueAsString(chat));

            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error while trying to save chat '" + chosenIdentifier + "': " + e);
            e.printStackTrace();
        }
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
