package dev.ceccon.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class APIConfigTest {

    @Test
    void getFullUrlBuildsUrlWithPropertiesBeingSet() {
        String protocol = "http";
        String host = "localhost";
        String port = "8089";
        String endpoint = "v1/chat/completions";
        String url = protocol + "://" + host + ":" + port + "/" + endpoint;

        APIConfig config = new APIConfig();
        config.setPort(port);

        String urlFromConfig = config.getFullUrl();

        assertEquals(url, urlFromConfig);
    }

    @Test
    void getAndSetPort() {
        String port = "8081";

        APIConfig config = new APIConfig();
        config.setPort(port);

        String portOnConfig = config.getPort();

        assertEquals(port, portOnConfig);
    }

    @Test
    void getAndSetModel() {
        String model = "llama3";

        APIConfig config = new APIConfig();
        config.setModel(model);

        String modelOnConfig = config.getModel();

        assertEquals(model, modelOnConfig);
    }

    @Test
    void getAndSetTemperature() {
        double temperature = 0.5;

        APIConfig config = new APIConfig();
        config.setTemperature(temperature);

        double temperatureOnConfig = config.getTemperature();

        assertEquals(temperature, temperatureOnConfig);
    }

    @Test
    void getAndSetStreaming() {
        boolean streaming = false;

        APIConfig config = new APIConfig();
        config.setStreaming(streaming);

        boolean streamingOnConfig = config.getStreaming();

        assertEquals(streaming, streamingOnConfig);
    }

}