package dev.ceccon.storage;

import dev.ceccon.conversation.Chat;

import java.io.IOException;

public interface Storage {

    Storage clone();
    Chat load(String fileIdentifier) throws IOException;
    void save (Chat chat, String chosenIdentifier) throws IOException;

}
