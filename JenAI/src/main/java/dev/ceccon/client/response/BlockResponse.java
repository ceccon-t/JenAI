package dev.ceccon.client.response;

public class BlockResponse implements Response {

    private String role;
    private String content;
    private UsageMetrics metrics;

    public BlockResponse(String role, String content, UsageMetrics metrics) {
        this.role = role;
        this.content = content;
        this.metrics = metrics;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public String getContent() {
        return this.content;
    }

    public UsageMetrics getMetrics() {
        return this.metrics;
    }
}
