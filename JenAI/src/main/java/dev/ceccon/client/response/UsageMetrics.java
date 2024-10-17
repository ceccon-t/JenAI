package dev.ceccon.client.response;

import java.util.Objects;

public class UsageMetrics {

    private Integer completionTokens;
    private Integer promptTokens;
    private Integer totalTokens;

    public UsageMetrics(Integer completionTokens, Integer promptTokens, Integer totalTokens) {
        this.completionTokens = completionTokens;
        this.promptTokens = promptTokens;
        this.totalTokens = totalTokens;
    }

    public Integer getCompletionTokens() {
        return this.completionTokens;
    }

    public Integer getPromptTokens() {
        return this.promptTokens;
    }

    public Integer getTotalTokens() {
        return this.totalTokens;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsageMetrics metrics = (UsageMetrics) o;
        return Objects.equals(completionTokens, metrics.completionTokens) && Objects.equals(promptTokens, metrics.promptTokens) && Objects.equals(totalTokens, metrics.totalTokens);
    }

    @Override
    public int hashCode() {
        return Objects.hash(completionTokens, promptTokens, totalTokens);
    }
}
