package dev.ceccon.client.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlockResponseTest {

    @Test
    void getRoleFromConstructor() {
        String role = "user";
        String content = "aaa";
        UsageMetrics metrics = new UsageMetrics(5, 6, 11);

        BlockResponse response = new BlockResponse(role, content, metrics);

        assertTrue(role.equals(response.getRole()));
    }

    @Test
    void getContentFromConstructor() {
        String role = "user";
        String content = "aaa";
        UsageMetrics metrics = new UsageMetrics(5, 6, 11);

        BlockResponse response = new BlockResponse(role, content, metrics);

        assertTrue(content.equals(response.getContent()));
    }

    @Test
    void getMetricsFromConstructor() {
        String role = "user";
        String content = "aaa";
        UsageMetrics metrics = new UsageMetrics(5, 6, 11);

        BlockResponse response = new BlockResponse(role, content, metrics);

        assertTrue(metrics.equals(response.getMetrics()));
    }

}