package dev.ceccon.cli;

import dev.ceccon.client.LLMClient;
import dev.ceccon.client.LLMSanitizer;
import dev.ceccon.conversation.Chat;
import dev.ceccon.conversation.Message;
import dev.ceccon.dtos.PromptDTO;
import dev.ceccon.dtos.ResponseDTO;
import dev.ceccon.storage.LocalFileStorage;

import java.io.IOException;
import java.util.Scanner;

public class CLISession {

    private static final String EXIT_COMMAND = "(exit)";
    private static final String SAVE_COMMAND = "(save)";

    private Chat chat;
    private Scanner sc = new Scanner(System.in);

    private LLMClient llmClient;
    private LocalFileStorage storage;

    public CLISession(Chat chat, LLMClient llmClient, LocalFileStorage storage) {
        this.chat = chat;
        this.llmClient = llmClient;
        this.storage = storage;
    }

    public void start() throws IOException {
        printInstructions();
        recapChatHistory();
        String userInput = "";
        String userMessage = "";
        String assistantMessage = "";
        ResponseDTO response;

        do {
            // User turn
            userInput = getUserInput();
            if (userInput.equals(EXIT_COMMAND)) break;
            if (userInput.startsWith(SAVE_COMMAND)) { save(userInput); continue; }

            userMessage = LLMSanitizer.sanitizeForChat(userInput);
            chat.addMessage(
                    "user",
                    userMessage
            );

            // Bot turn
            response = llmClient.send(
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

    private void save(String userInput) {
        String resultMessage = "Conversation saved to " + storage.getAbsoluteBaseFolder();
        try {
            String filename = userInput.replace(SAVE_COMMAND, "").trim();
            storage.save(chat, filename);
        } catch (IOException e) {
            resultMessage = "Error when trying to save...";
        }
        System.out.println(resultMessage);
    }

    private void printInstructions() {
        System.out.println("===================================");
        System.out.println("Welcome to JenAI chat!             ");
        System.out.println("                                   ");
        System.out.println("Enter your message when prompted,  ");
        System.out.println("then wait for the bot's response.  ");
        System.out.println("                                   ");
        System.out.println("Say " + SAVE_COMMAND + " to save.  ");
        System.out.println("Say " + EXIT_COMMAND + " to quit.  ");
        System.out.println("===================================");
    }

    private void recapChatHistory() {
        for (Message message : chat.getMessages()) {
            if (message.role().equals("system")) continue;

            printMessageToCLI(message);
            System.out.println(" ");
        }
    }

    private String getUserInput() {
        printMessageSeparator();
        System.out.print("user: ");
        String input = sc.nextLine();
        return input;
    }

    private void printMessageSeparator() {
        System.out.println("\n----");
    }

    private void printMessageToCLI(Message message) {
        printMessageSeparator();
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
