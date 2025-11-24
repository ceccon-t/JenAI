package dev.ceccon.storage;

import dev.ceccon.conversation.Chat;

import java.io.IOException;

public interface Storage {

    public Chat load(String fileIdentifier) throws IOException;
    public void save (Chat chat, String chosenIdentifier) throws IOException;

}
