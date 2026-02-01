package dev.ceccon.storage;

import dev.ceccon.conversation.Chat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompositeStorage implements Storage {

    private static final String NO_STORAGE_CONFIGURED_MESSAGE = "No storage configured! Running without persistence (no load or save command allowed)...";

    private List<Storage> storages = new ArrayList<>();

    @Override
    public Storage clone() {
        CompositeStorage newCompositeStorage = new CompositeStorage();

        storages.stream().forEach(newCompositeStorage::addStorage);

        return newCompositeStorage;
    }

    @Override
    public Chat load(String fileIdentifier) throws IOException {
        if (storages.isEmpty()) {
            System.out.println(NO_STORAGE_CONFIGURED_MESSAGE);
            return null;
        }

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
        if (storages.isEmpty()) {
            System.out.println(NO_STORAGE_CONFIGURED_MESSAGE);
            return;
        }

        for (Storage storage : storages) {
            storage.save(chat, chosenIdentifier);
        }
    }

    public void addStorage(Storage storage) {
        this.storages.add(storage);
    }

    public boolean hasLocalStorage() {
        return storages.stream().anyMatch(s -> s instanceof LocalFileStorage);
    }

    public boolean hasDatabaseStorage() {
        return storages.stream().anyMatch(s -> s instanceof DatabaseStorage);
    }

    public List<Storage> getStorages() {
        return storages.stream().map(s -> s.clone()).collect(Collectors.toList());
    }

}
