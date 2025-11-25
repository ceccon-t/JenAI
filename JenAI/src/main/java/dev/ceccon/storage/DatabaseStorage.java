package dev.ceccon.storage;

import dev.ceccon.conversation.Chat;

import java.io.IOException;

public class DatabaseStorage implements Storage {

    private String username;
    private String password;

    public DatabaseStorage(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public DatabaseStorage() {
        this("", "");
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
