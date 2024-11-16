package dev.ceccon.client.dtos;

import dev.ceccon.config.APIConfig;
import dev.ceccon.conversation.Chat;
import dev.ceccon.conversation.Message;

import java.util.ArrayList;
import java.util.List;

public class PromptDTO {

    public record MessageDTO(String role, String content) {}

    private String model = "";
    private List<MessageDTO> messages = new ArrayList<>();
    private boolean stream = false;
    private double temperature = 0.0;

    private PromptDTO() {}

    public static PromptDTO build(Chat chat, APIConfig config) {
        PromptDTO dto = new PromptDTO();
        dto.addMessagesFromChat(chat);
        dto.setTemperature(config.getTemperature());
        dto.setModel(config.getModel());

        return dto;
    }

    private void addMessagesFromChat(Chat chat) {
        for (Message message : chat.getMessages()) {
            this.messages.add(new MessageDTO(message.role(), message.content()));
        }
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getTemperature() {
        return temperature;
    }

}
