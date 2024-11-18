package dev.ceccon.config;

public class AppConfig {

    private APIConfig apiConfig;

    public AppConfig(APIConfig apiConfig) {
        this.apiConfig = apiConfig;
    }

    public APIConfig getApiConfig() {
        return apiConfig;
    }

}
