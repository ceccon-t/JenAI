package dev.ceccon.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AppConfigTest {

    @Test
    void canGetAPIConfigFromConstructor() {
        APIConfig apiConfig = new APIConfig();

        AppConfig config = new AppConfig(apiConfig);

        APIConfig result = config.getApiConfig();

        assertEquals(apiConfig, result);
    }

}