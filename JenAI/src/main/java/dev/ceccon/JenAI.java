package dev.ceccon;

import dev.ceccon.client.LLMClient;
import dev.ceccon.conversation.Chat;
import dev.ceccon.dtos.PromptDTO;
import dev.ceccon.dtos.ResponseDTO;

import java.io.IOException;

public class JenAI {
    public static void main(String[] args) throws IOException {
        Chat chat = new Chat();

        chat.addMessage(
                "system",
                "You are JenAI, a generative AI chatbot. Your name is due to the fact that your main interface was implemented in Java."
        );

        chat.addMessage(
                "user",
                "Say hello to the world."
        );

        ResponseDTO response = LLMClient.sendPrompt(
                "http://localhost:8080/v1/chat/completions",
                PromptDTO.forChat(chat)
        );

        chat.addMessage(
                response.getRole(),
                response.getContent()
        );

        System.out.println(response.getRole() + ": " + response.getContent());

    }
}
