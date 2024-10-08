package dev.ceccon.client;

public class APIConfig {

    private String protocol = "http";
    private String host = "localhost";
    private String port =  "8080";
    private String endpoint = "v1/chat/completions";

    private String model = "";

    public void setPort(String port) {
        this.port = port;
    }

    public String getModel() {
        return this.model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getFullUrl() {
        // Default example: "http://localhost:8080/v1/chat/completions"
        return protocol + "://" + host + ":" + port + "/" + endpoint;
    }
}
