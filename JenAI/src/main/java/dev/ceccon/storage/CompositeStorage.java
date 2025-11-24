package dev.ceccon.storage;

import dev.ceccon.conversation.Chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CompositeStorage implements Storage {

    public List<Storage> storages = new ArrayList<>();

    @Override
    public Chat load(String fileIdentifier) throws IOException {
        Chat chat;
        for (Storage storage : storages) {
            try {
                chat = storage.load(fileIdentifier);
                return chat;
            } catch (IOException e) {
            }
        }
        throw new IOException("Could find chat " + fileIdentifier);
    }

    @Override
    public void save(Chat chat, String chosenIdentifier) throws IOException {
        for (Storage storage : storages) {
            storage.save(chat, chosenIdentifier);
        }
    }

    public void addStorage(Storage storage) {
        this.storages.add(storage);
    }
}
