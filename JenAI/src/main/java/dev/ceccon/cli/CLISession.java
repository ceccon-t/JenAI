package dev.ceccon.cli;

import dev.ceccon.client.LLMClient;
import dev.ceccon.client.LLMSanitizer;
import dev.ceccon.conversation.Chat;
import dev.ceccon.conversation.Message;
import dev.ceccon.dtos.PromptDTO;
import dev.ceccon.dtos.ResponseDTO;

import java.io.IOException;
import java.util.Scanner;

public class CLISession {

    private static final String EXIT_COMMAND = "(exit)";

    private Chat chat;
    private Scanner sc = new Scanner(System.in);

    public void start() throws IOException {
        printInstructions();
        chat = new Chat();
        String userInput = "";
        String userMessage = "";
        String assistantMessage = "";
        ResponseDTO response;

        chat.addMessage(
                "system",
                "You are JenAI, a generative AI chatbot. Your name is due to the fact that your main interface was implemented in Java."
        );

        do {
            // User turn
            userInput = getUserInput();
            if (userInput.equals(EXIT_COMMAND)) break;

            userMessage = LLMSanitizer.sanitizeForChat(userInput);
            chat.addMessage(
                    "user",
                    userMessage
            );

            // Bot turn
            response = LLMClient.sendPrompt(
                    "http://localhost:8080/v1/chat/completions",
                    PromptDTO.forChat(chat)
            );

            printMessageToCLI(new Message(response.getRole(), LLMSanitizer.sanitizeLLMSpecialTokens(response.getContent())));
            assistantMessage = LLMSanitizer.sanitizeForChat(response.getContent());
            chat.addMessage(
                    response.getRole(),
                    assistantMessage
            );
            printUsageMetrics(response);

        } while(true);

        System.out.println("\nBye!");
    }

    private void printInstructions() {
        System.out.println("===================================");
        System.out.println("Welcome to JenAI chat!             ");
        System.out.println("                                   ");
        System.out.println("Enter your message when prompted,  ");
        System.out.println("then wait for the bot's response.  ");
        System.out.println("Say " + EXIT_COMMAND + " to quit.  ");
        System.out.println("===================================");
    }

    private String getUserInput() {
        System.out.println(" ");
        System.out.print("user: ");
        String input = sc.nextLine();
        return input;
    }

    private void printMessageToCLI(Message message) {
        System.out.println(message.role() + ": " + message.content());
    }

    private void printUsageMetrics(ResponseDTO responseDTO) {
        StringBuilder builder = new StringBuilder();
        ResponseDTO.UsageDTO usageDTO = responseDTO.getUsage();
        String usageReport = builder.append("(Token usage metrics: [")
                .append("Completion tokens: ").append(usageDTO.completion_tokens())
                .append(", ")
                .append("Prompt tokens: ").append(usageDTO.prompt_tokens())
                .append(", ")
                .append("Total tokens: ").append(usageDTO.total_tokens())
                .append("])")
                .toString();
        System.out.println(usageReport);
    }

}
