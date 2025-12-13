package dev.ceccon.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.conversation.Chat;
import dev.ceccon.conversation.Message;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalFileStorage implements Storage{

    private String baseFolder = "./jenai_chats/";
    private ObjectMapper mapper = new ObjectMapper();

    private String getBaseFolder() {
        return this.baseFolder;
    }

    private void setBaseFolder(String baseFolder) {
        this.baseFolder = baseFolder;
    }

    private String getAbsoluteBaseFolder() {
        return Paths.get(baseFolder).toAbsolutePath().normalize().toString();
    }

    @Override
    public Chat load(String filePath) throws IOException {
        // From absolute value
        Path path = Paths.get(filePath.replace("\\", "/"));
        if (Files.exists(path)) return readChatFromFile(path);

        // From relative to base folder
        path = Paths.get(baseFolder + filePath.replace("\\", "/") + ".json");
        if (Files.exists(path)) return readChatFromFile(path);

        return null;
    }

    private Chat readChatFromFile(Path path) throws IOException {
        return mapper.readValue(path.toFile(), Chat.class);
    }

    @Override
    public void save(Chat chat, String chosenFilename) throws IOException {
        System.out.println("Attempting to save conversation to " + getAbsoluteBaseFolder());
        ensureBaseFolderExists();

        chosenFilename = chosenFilename.trim();
        String filename = chosenFilename.isEmpty() ? generateFileName() : chosenFilename;

        saveAsText(chat, filename);
        saveAsJSON(chat, filename);
    }

    private void saveAsText(Chat chat, String filename) throws IOException {
        Path filePath = Paths.get(baseFolder + "/" + filename + ".txt");

        Files.createFile(filePath);

        for (Message message : chat.getMessages()) {
            Files.writeString(filePath, "----" + System.lineSeparator(), StandardOpenOption.APPEND);
            Files.writeString(filePath, message.role() + ": ", StandardOpenOption.APPEND);
            Files.writeString(filePath, message.content(), StandardOpenOption.APPEND);
            Files.writeString(filePath, System.lineSeparator() + System.lineSeparator(), StandardOpenOption.APPEND);
        }
    }

    private void saveAsJSON(Chat chat, String filename) throws IOException {
        Path filePath = Paths.get(baseFolder + "/" + filename + ".json");
        File file = filePath.toFile();

        mapper.writeValue(file, chat);
    }

    private void ensureBaseFolderExists() throws IOException {
        Path folder = Paths.get(baseFolder);
        Files.createDirectories(folder);
    }

    private String generateFileName() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss_ns"));
        return "JenAI_chat_" + timestamp;
    }
}
