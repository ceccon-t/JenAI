package dev.ceccon.conversation;

import dev.ceccon.client.LLMSanitizer;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private static final String DEFAULT_SYSTEM_PROMPT =
            "You are JenAI, a generative AI chatbot. Your name is due to the fact that your main interface was implemented in Java.";

    public static Chat initialize(String systemPrompt) {
        Chat newChat = new Chat();
        newChat.addMessage(
                "system",
                systemPrompt
        );

        return newChat;
    }

    public static Chat initialize() {
        return initialize(DEFAULT_SYSTEM_PROMPT);
    }

    private List<Message> messages = new ArrayList<>();

    public List<Message> getMessages() {
        return messages;
    }

    public void addMessage(String role, String content) {
        messages.add(new Message(role, content));
    }

    public void loadConversationState(Chat otherChat) {
        this.messages = new ArrayList<>();
        otherChat.getMessages().forEach(m -> this.messages.add(new Message(m.role(), LLMSanitizer.sanitizeForChat(m.content()))));
    }

}
