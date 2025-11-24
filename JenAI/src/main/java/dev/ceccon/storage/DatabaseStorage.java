package dev.ceccon.storage;

import dev.ceccon.conversation.Chat;

import java.io.IOException;

public class DatabaseStorage implements Storage {

    @Override
    public Chat load(String fileIdentifier) throws IOException {
        return null;
    }

    @Override
    public void save(Chat chat, String chosenIdentifier) throws IOException {

    }

}
