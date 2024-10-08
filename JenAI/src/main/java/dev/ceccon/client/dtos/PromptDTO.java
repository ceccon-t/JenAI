package dev.ceccon.client.dtos;

import dev.ceccon.conversation.Chat;
import dev.ceccon.conversation.Message;

import java.util.ArrayList;
import java.util.List;

public class PromptDTO {

    public record MessageDTO(String role, String content) {}

    private String model = "";
    private List<MessageDTO> messages = new ArrayList<>();

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

}
