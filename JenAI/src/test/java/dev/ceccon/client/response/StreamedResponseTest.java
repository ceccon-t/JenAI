package dev.ceccon.client.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StreamedResponseTest {

    @Test
    void getRoleFromConstructor() {
        String role = "user";
        String content = "aaa";

        StreamedResponse response = new StreamedResponse(role, content);

        assertEquals(role, response.getRole());
    }

    @Test
    void getContentFromConstructor() {
        String role = "user";
        String content = "aaa";

        StreamedResponse response = new StreamedResponse(role, content);

        assertEquals(content, response.getContent());
    }

}