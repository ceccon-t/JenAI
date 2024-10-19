package dev.ceccon.client.response;

public class StreamedResponse implements Response {

    private String role;
    private String content;

    public StreamedResponse(String role, String content) {
        this.role = role;
        this.content = content;
    }

    @Override
    public String getRole() {
        return this.role;
    }

    @Override
    public String getContent() {
        return this.content;
    }
}
