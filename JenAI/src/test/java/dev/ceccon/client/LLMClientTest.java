package dev.ceccon.client;

import dev.ceccon.client.response.BlockResponse;
import dev.ceccon.conversation.Chat;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.BufferedReader;
import java.io.IOException;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class LLMClientTest {

    @Test
    void sendBuildsBlockResponse() throws IOException {
        try (MockedStatic<LLMConnection> mockedLLMConnection = mockStatic(LLMConnection.class)) {
            // expected values
            String role = "assistant";
            String content = "Hello!";

            // building mocks
            BufferedReader mockBufferedReader = mock(BufferedReader.class);
            String mockLine = """
                    {"choices":[{"finish_reason":"stop","index":0,"message":{"content":"%s","role":"%s"}}],"created":1,"id":"1","model":"","object":"","usage":{"completion_tokens":1,"prompt_tokens":1,"total_tokens":2}};
            """.formatted(content, role);
            when(mockBufferedReader.readLine()).thenReturn(mockLine, null);

            LLMConnection mockConnection = mock(LLMConnection.class);
            when(mockConnection.getBufferedReader()).thenReturn(mockBufferedReader);
            mockedLLMConnection.when(() -> LLMConnection.forUrl(any())).thenReturn(mockConnection);

            APIConfig mockConfig = mock(APIConfig.class);
            when(mockConfig.getFullUrl()).thenReturn("a");
            when(mockConfig.getModel()).thenReturn("b");

            // run test
            LLMClient client = new LLMClient(mockConfig);

            BlockResponse response = client.send(new Chat());

            assertEquals(role, response.getRole());
            assertEquals(content, response.getContent());
        }
    }

}