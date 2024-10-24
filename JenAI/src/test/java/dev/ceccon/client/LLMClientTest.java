package dev.ceccon.client;

import dev.ceccon.client.response.BlockResponse;
import dev.ceccon.client.response.StreamedResponse;
import dev.ceccon.conversation.Chat;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.function.Consumer;

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

    @Test
    void sendWithStreamingReturnsResponseWithSingleToken() throws IOException {
        try (MockedStatic<LLMConnection> mockedLLMConnection = mockStatic(LLMConnection.class)) {
            // expected values
            String role = "assistant";
            String content = "Hello!";

            // building mocks
            BufferedReader mockBufferedReader = mock(BufferedReader.class);
            String mockLine = """
                    data: {"choices":[{"delta":{"content":"%s"},"finish_reason":null,"index":0}],"created":1,"id":"","model":"","object":""}
            """.formatted(content).trim();
            when(mockBufferedReader.readLine()).thenReturn(mockLine, null);

            LLMConnection mockConnection = mock(LLMConnection.class);
            when(mockConnection.getBufferedReader()).thenReturn(mockBufferedReader);
            mockedLLMConnection.when(() -> LLMConnection.forUrl(any())).thenReturn(mockConnection);

            APIConfig mockConfig = mock(APIConfig.class);
            when(mockConfig.getFullUrl()).thenReturn("a");
            when(mockConfig.getModel()).thenReturn("b");

            Consumer mockedConsumer = mock(Consumer.class);

            // run test
            LLMClient client = new LLMClient(mockConfig);

            StreamedResponse response = client.sendWithStreamingResponse(new Chat(), mockedConsumer);

            assertEquals(role, response.getRole());
            assertEquals(content, response.getContent());
            verify(mockedConsumer, times(1)).accept(any());
        }
    }

    @Test
    void sendWithStreamingReturnsResponseWithMultipleTokens() throws IOException {
        try (MockedStatic<LLMConnection> mockedLLMConnection = mockStatic(LLMConnection.class)) {
            // expected values
            String role = "assistant";
            String firstToken = "Hello, ";
            String secondToken = "there!";
            String content = firstToken + secondToken;

            // building mocks
            BufferedReader mockBufferedReader = mock(BufferedReader.class);
            String mockLine1 = """
                    data: {"choices":[{"delta":{"content":"%s"},"finish_reason":null,"index":0}],"created":1,"id":"","model":"","object":""}
            """.formatted(firstToken).trim();
            String mockLine2 = """
                    data: {"choices":[{"delta":{"content":"%s"},"finish_reason":null,"index":0}],"created":1,"id":"","model":"","object":""}
            """.formatted(secondToken).trim();
            when(mockBufferedReader.readLine()).thenReturn(mockLine1, mockLine2, null);

            LLMConnection mockConnection = mock(LLMConnection.class);
            when(mockConnection.getBufferedReader()).thenReturn(mockBufferedReader);
            mockedLLMConnection.when(() -> LLMConnection.forUrl(any())).thenReturn(mockConnection);

            APIConfig mockConfig = mock(APIConfig.class);
            when(mockConfig.getFullUrl()).thenReturn("a");
            when(mockConfig.getModel()).thenReturn("b");

            Consumer mockedConsumer = mock(Consumer.class);

            // run test
            LLMClient client = new LLMClient(mockConfig);

            StreamedResponse response = client.sendWithStreamingResponse(new Chat(), mockedConsumer);

            assertEquals(role, response.getRole());
            assertEquals(content, response.getContent());
            verify(mockedConsumer, times(2)).accept(any());
        }
    }

    @Test
    void sendWithStreamingReturnsResponseWhenReceivingEmptyLinesInBetweenTokenEvents() throws  IOException{
        try (MockedStatic<LLMConnection> mockedLLMConnection = mockStatic(LLMConnection.class)) {
            // expected values
            String role = "assistant";
            String firstToken = "Hello, ";
            String secondToken = "there!";
            String content = firstToken + secondToken;

            // building mocks
            BufferedReader mockBufferedReader = mock(BufferedReader.class);
            String mockLine1 = """
                    data: {"choices":[{"delta":{"content":"%s"},"finish_reason":null,"index":0}],"created":1,"id":"","model":"","object":""}
            """.formatted(firstToken).trim();
            String mockLine2 = "";
            String mockLine3 = """
                    data: {"choices":[{"delta":{"content":"%s"},"finish_reason":null,"index":0}],"created":1,"id":"","model":"","object":""}
            """.formatted(secondToken).trim();
            when(mockBufferedReader.readLine()).thenReturn(mockLine1, mockLine2, mockLine3, null);

            LLMConnection mockConnection = mock(LLMConnection.class);
            when(mockConnection.getBufferedReader()).thenReturn(mockBufferedReader);
            mockedLLMConnection.when(() -> LLMConnection.forUrl(any())).thenReturn(mockConnection);

            APIConfig mockConfig = mock(APIConfig.class);
            when(mockConfig.getFullUrl()).thenReturn("a");
            when(mockConfig.getModel()).thenReturn("b");

            Consumer mockedConsumer = mock(Consumer.class);

            // run test
            LLMClient client = new LLMClient(mockConfig);

            StreamedResponse response = client.sendWithStreamingResponse(new Chat(), mockedConsumer);

            assertEquals(role, response.getRole());
            assertEquals(content, response.getContent());
            verify(mockedConsumer, times(2)).accept(any());
        }
    }

    @Test
    void sendWithStreamingReturnsResponseWhenFinalEventIsNotNull() throws IOException {
        try (MockedStatic<LLMConnection> mockedLLMConnection = mockStatic(LLMConnection.class)) {
            // expected values
            String role = "assistant";
            String firstToken = "Hello, ";
            String secondToken = "there!";
            String content = firstToken + secondToken;

            // building mocks
            BufferedReader mockBufferedReader = mock(BufferedReader.class);
            String mockLine1 = """
                    data: {"choices":[{"delta":{"content":"%s"},"finish_reason":null,"index":0}],"created":1,"id":"","model":"","object":""}
            """.formatted(firstToken).trim();
            String mockLine2 = """
                    data: {"choices":[{"delta":{"content":"%s"},"finish_reason":null,"index":0}],"created":1,"id":"","model":"","object":""}
            """.formatted(secondToken).trim();
            String mockLine3 = """
                    data: {"choices":[{"delta":{"content":""},"finish_reason":"stop","index":0}],"created":1,"id":"","model":"","object":""}
            """.trim();
            when(mockBufferedReader.readLine()).thenReturn(mockLine1, mockLine2, mockLine3);

            LLMConnection mockConnection = mock(LLMConnection.class);
            when(mockConnection.getBufferedReader()).thenReturn(mockBufferedReader);
            mockedLLMConnection.when(() -> LLMConnection.forUrl(any())).thenReturn(mockConnection);

            APIConfig mockConfig = mock(APIConfig.class);
            when(mockConfig.getFullUrl()).thenReturn("a");
            when(mockConfig.getModel()).thenReturn("b");

            Consumer mockedConsumer = mock(Consumer.class);

            // run test
            LLMClient client = new LLMClient(mockConfig);

            StreamedResponse response = client.sendWithStreamingResponse(new Chat(), mockedConsumer);

            assertEquals(role, response.getRole());
            assertEquals(content, response.getContent());
            verify(mockedConsumer, times(2)).accept(any());
        }
    }

}