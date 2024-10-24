package dev.ceccon.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.client.dtos.PromptDTO;
import dev.ceccon.client.dtos.ResponseDTO;
import dev.ceccon.client.dtos.StreamingResponseEventDTO;
import dev.ceccon.client.response.BlockResponse;
import dev.ceccon.client.response.StreamedResponse;
import dev.ceccon.conversation.Chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class LLMClient {

    public static final String DEFAULT_BOT_ROLE = "assistant";

    private APIConfig config;

    public LLMClient(APIConfig config) {
        this.config = config;
    }

public BlockResponse send(Chat chat) throws IOException {
    ObjectMapper mapper = new ObjectMapper();

    PromptDTO promptDTO = PromptDTO.forChat(chat);
    promptDTO.setStream(false);
    promptDTO.setModel(config.getModel());
    String body = mapper.writeValueAsString(promptDTO);

    LLMConnection llmConnection = LLMConnection.forUrl(config.getFullUrl());
    llmConnection.send(body);

    String rawResponse = "";

    try (BufferedReader in = llmConnection.getBufferedReader()) {
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        rawResponse = response.toString();
    }

    llmConnection.close();

    ResponseDTO responseDTO = mapper.readValue(rawResponse, ResponseDTO.class);
    return responseDTO.toBlockResponse();
}

    public StreamedResponse sendWithStreamingResponse(Chat chat, Consumer<String> tokenConsumer) throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        PromptDTO promptDTO = PromptDTO.forChat(chat);
        promptDTO.setStream(true);
        promptDTO.setModel(config.getModel());
        String body = mapper.writeValueAsString(promptDTO);

        String urlString = config.getFullUrl();
        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            dos.writeBytes(body);
            dos.flush();
        }

        String rawResponse = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder responseBuilder = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                inputLine = LLMSanitizer.sanitizeLLMSpecialTokens(inputLine);
                if (inputLine.isEmpty()) continue;
                StreamingResponseEventDTO eventDTO = mapper.readValue(inputLine.substring(6), StreamingResponseEventDTO.class);
                if (eventDTO.isFinalEvent()) break;
                String eventData = eventDTO.getBestChoice();

                tokenConsumer.accept(eventData);
                responseBuilder.append(eventData);
            }
            rawResponse = responseBuilder.toString();
        }

        connection.disconnect();

        StreamedResponse response = new StreamedResponse(DEFAULT_BOT_ROLE, rawResponse);
        return response;
    }

}
