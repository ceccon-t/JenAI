package dev.ceccon.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.dtos.PromptDTO;
import dev.ceccon.dtos.ResponseDTO;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class LLMClient {

    private APIConfig config;

    public LLMClient(APIConfig config) {
        this.config = config;
    }

    public ResponseDTO send(PromptDTO promptDTO) throws IOException {
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

        return mapper.readValue(rawResponse, ResponseDTO.class);
    }

}
