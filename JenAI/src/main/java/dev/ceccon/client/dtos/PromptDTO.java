package dev.ceccon.client.dtos;

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

    public static PromptDTO forChat(Chat chat) {
        PromptDTO prompt = new PromptDTO();

        for (Message message : chat.getMessages()) {
            prompt.messages.add(new MessageDTO(message.role(), message.content()));
        }

        return prompt;
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
