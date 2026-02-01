package dev.ceccon.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.conversation.Chat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;

public class DatabaseStorage implements Storage {

    private ObjectMapper mapper = new ObjectMapper();

    private String databaseEngine;
    private String port;
    private String username;
    private String password;
    private String databaseName;

    private Connection conn;

    public DatabaseStorage(String databaseEngine, String port, String username, String password, String databaseName) {
        this.databaseEngine = databaseEngine;
        this.port = port;
        this.username = username;
        this.password = password;
        this.databaseName = databaseName;

        try {
            conn = getConnection();
            System.out.println("Connected to database.");
            setupDatabase();
        } catch (SQLException e) {
            System.out.println("Error trying to connect to database...");
        }
    }

    public DatabaseStorage() {
        this("", "", "", "", "");
    }

    @Override
    public Storage clone() {
        return new DatabaseStorage(databaseEngine, port, username, password, databaseName);
    }

    private Connection getConnection() throws SQLException {
        String url;

        switch (databaseEngine) {
            case "sqlite":
                String dbFilepath = "jenai_chats/";
                Path folder = Paths.get(dbFilepath);
                try {
                    Files.createDirectories(folder);
                } catch (IOException e) {
                    throw new RuntimeException("Could not ensure database file for sqlite db existed: " + dbFilepath);
                }
                url = "jdbc:sqlite:" + dbFilepath + databaseName;
                return DriverManager.getConnection(url);
            case "postgres":
                String host = "localhost";
                url = "jdbc:postgresql://" + host + ":" + port + "/" + databaseName;;
                return DriverManager.getConnection(url, username, password);
            default:
                throw new RuntimeException("Database engine not supported: " + databaseEngine);
        }
    }

    private void setupDatabase() throws SQLException {
        String ensureExistsQuerySqlite = """
                CREATE TABLE IF NOT EXISTS chats (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    uid TEXT DEFAULT (lower(hex(randomblob(16)))) NULL,
                    "name" TEXT NOT NULL,
                    "content" TEXT NULL,
                    tags TEXT NULL,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NULL,
                    updated_at TIMESTAMP NULL
                );
                """;

        String ensureExistsQueryPostgres = """
                CREATE TABLE IF NOT EXISTS public.chats (
                	id serial4 NOT NULL,
                	uid uuid DEFAULT gen_random_uuid() NULL,
                	"name" text NOT NULL,
                	"content" text NULL,
                	tags text NULL,
                	created_at timestamp DEFAULT now() NULL,
                	updated_at timestamp NULL,
                	CONSTRAINT chats_pkey PRIMARY KEY (id)
                );
                """;

        String ensureQuery = databaseEngine.equals("sqlite") ? ensureExistsQuerySqlite : ensureExistsQueryPostgres;

        PreparedStatement statement = conn.prepareStatement(ensureQuery);
        statement.execute();
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
        String sql;

        Chat existing = load(chosenIdentifier);
        if (existing != null) {
            sql = """
                UPDATE chats
                SET content=?
                WHERE name=? 
            """;
        } else {
            sql = """
                INSERT INTO chats 
                    (content, name)
                VALUES
                    (?, ?)
            """;
        }

        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, mapper.writeValueAsString(chat));
            statement.setString(2, chosenIdentifier);

            statement.execute();
        } catch (SQLException e) {
            System.out.println("Error while trying to save chat '" + chosenIdentifier + "': " + e);
            e.printStackTrace();
        }
    }

    public String getDatabaseEngine() {
        return databaseEngine;
    }

    public void setDatabaseEngine(String databaseEngine) {
        this.databaseEngine = databaseEngine;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
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
