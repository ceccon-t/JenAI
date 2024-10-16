package dev.ceccon.client;

public interface UsageMetrics {

    Integer completion_tokens();

    Integer prompt_tokens();

    Integer total_tokens();

}
