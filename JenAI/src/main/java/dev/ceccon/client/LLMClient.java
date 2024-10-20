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

public class LLMClient {

    public static final String DEFAULT_BOT_ROLE = "assistant";

    private APIConfig config;

    public LLMClient(APIConfig config) {
        this.config = config;
    }

    public BlockResponse send(Chat chat) throws IOException {
        PromptDTO promptDTO = PromptDTO.forChat(chat);
        String urlString = config.getFullUrl();
        promptDTO.setModel(config.getModel());
        ObjectMapper mapper = new ObjectMapper();

        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        String body = mapper.writeValueAsString(promptDTO);

        try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            dos.writeBytes(body);
            dos.flush();
        }

        int responseCode = connection.getResponseCode();
        String rawResponse = "";

        try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            rawResponse = response.toString();
        }

        connection.disconnect();

        ResponseDTO responseDTO = mapper.readValue(rawResponse, ResponseDTO.class);
        return responseDTO.toBlockResponse();
    }

    public StreamedResponse sendWithStreamingResponse(Chat chat) throws IOException {
        PromptDTO promptDTO = PromptDTO.forChat(chat);
        promptDTO.setStream(true);
        String urlString = config.getFullUrl();
        promptDTO.setModel(config.getModel());
        ObjectMapper mapper = new ObjectMapper();

        URL url = new URL(urlString);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Accept", "application/json");

        String body = mapper.writeValueAsString(promptDTO);

        try (DataOutputStream dos = new DataOutputStream(connection.getOutputStream())) {
            dos.writeBytes(body);
            dos.flush();
        }

        int responseCode = connection.getResponseCode();
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

                System.out.print(eventData);
                responseBuilder.append(eventData);
            }
            rawResponse = responseBuilder.toString();
        }

        connection.disconnect();

        StreamedResponse response = new StreamedResponse(DEFAULT_BOT_ROLE, rawResponse);
        return response;
    }

}
