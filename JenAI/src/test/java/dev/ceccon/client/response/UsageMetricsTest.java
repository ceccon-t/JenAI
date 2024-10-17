package dev.ceccon.client.response;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UsageMetricsTest {

    @Test
    void getCompletionTokensFromConstructor() {
        Integer completionTokens = 5;
        Integer promptTokens = 37;
        Integer totalTokens = 42;

        UsageMetrics metrics = new UsageMetrics(completionTokens, promptTokens, totalTokens);

        assertEquals(completionTokens, metrics.getCompletionTokens());
    }

    @Test
    void getPromptTokensFromConstructor() {
        Integer completionTokens = 5;
        Integer promptTokens = 37;
        Integer totalTokens = 42;

        UsageMetrics metrics = new UsageMetrics(completionTokens, promptTokens, totalTokens);

        assertEquals(promptTokens, metrics.getPromptTokens());
    }

    @Test
    void getTotalTokensFromConstructor() {
        Integer completionTokens = 5;
        Integer promptTokens = 37;
        Integer totalTokens = 42;

        UsageMetrics metrics = new UsageMetrics(completionTokens, promptTokens, totalTokens);

        assertEquals(totalTokens, metrics.getTotalTokens());
    }

}