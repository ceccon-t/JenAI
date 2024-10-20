package dev.ceccon.cli;

import dev.ceccon.client.APIConfig;
import dev.ceccon.client.LLMClient;
import dev.ceccon.client.LLMSanitizer;
import dev.ceccon.client.response.BlockResponse;
import dev.ceccon.client.response.Response;
import dev.ceccon.client.response.StreamedResponse;
import dev.ceccon.client.response.UsageMetrics;
import dev.ceccon.conversation.Chat;
import dev.ceccon.conversation.Message;
import dev.ceccon.storage.LocalFileStorage;

import java.io.IOException;
import java.util.Scanner;

public class CLISession {

    private static final String EXIT_COMMAND = "(exit)";
    private static final String SAVE_COMMAND = "(save)";

    private Chat chat;
    private Scanner sc = new Scanner(System.in);

    private APIConfig apiConfig;
    private LLMClient llmClient;
    private LocalFileStorage storage;

    public CLISession(Chat chat, APIConfig apiConfig, LLMClient llmClient, LocalFileStorage storage) {
        this.chat = chat;
        this.apiConfig = apiConfig;
        this.llmClient = llmClient;
        this.storage = storage;
    }

    public void start() throws IOException {
        printInstructions();
        recapChatHistory();
        String userInput = "";
        String userMessage = "";
        String assistantMessage = "";
        Response response;

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
            if (usingStreaming()) {
                prepareScreenForStreamingResponse();
                StreamedResponse streamedResponse = llmClient.sendWithStreamingResponse(chat);
                tidyUpScreenAfterStreamingResponse();
                response = streamedResponse;
            } else {
                BlockResponse blockResponse = llmClient.send(chat);
                response = blockResponse;
                printMessageToCLI(new Message(response.getRole(), LLMSanitizer.sanitizeLLMSpecialTokens(response.getContent())));
                printUsageMetrics(blockResponse);
            }

            assistantMessage = LLMSanitizer.sanitizeForChat(response.getContent());
            chat.addMessage(
                    response.getRole(),
                    assistantMessage
            );

        } while(true);

        System.out.println("\nBye!");
    }

    private boolean usingStreaming() {
        return apiConfig.getStreaming();
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
        System.out.println("=============================================================");
        System.out.println("Welcome to JenAI chat!                                       ");
        System.out.println("                                                             ");
        System.out.println("Enter your message when prompted,                            ");
        System.out.println("then wait for the bot's response.                            ");
        System.out.println("                                                             ");
        System.out.println("Commands:                                                    ");
        System.out.println("  " + SAVE_COMMAND + ", to save with default filename.       ");
        System.out.println("  " + SAVE_COMMAND + " <filename>, to use a custom filename. ");
        System.out.println("  " + EXIT_COMMAND + ", to quit.                             ");
        System.out.println("=============================================================");
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

    private void prepareScreenForStreamingResponse() {
        printMessageSeparator();
        System.out.print(LLMClient.DEFAULT_BOT_ROLE + ": ");
    }

    private void tidyUpScreenAfterStreamingResponse() {
        System.out.println("");
    }

    private void printUsageMetrics(BlockResponse response) {
        StringBuilder builder = new StringBuilder();
        UsageMetrics metrics = response.getMetrics();
        String usageReport = builder.append("(Token usage metrics: [")
                .append("Completion tokens: ").append(metrics.getCompletionTokens())
                .append(", ")
                .append("Prompt tokens: ").append(metrics.getPromptTokens())
                .append(", ")
                .append("Total tokens: ").append(metrics.getTotalTokens())
                .append("])")
                .toString();
        System.out.println(usageReport);
    }

}
