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
                if (chat != null) return chat;
            } catch (IOException e) {
            }
        }
        System.out.println("Could not find chat " + fileIdentifier);
        return null;
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
