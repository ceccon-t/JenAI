package dev.ceccon;

import dev.ceccon.config.APIConfig;
import dev.ceccon.conversation.Chat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JenAITest {

    @Test
    void cliOptionPortWithoutValueThrowsException() {
        String[] args = new String[]{"-p"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat());
        });
    }

    @Test
    void cliOptionPortWithNonNumericValueThrowsException() {
        String port = "port";
        String[] args = new String[]{"-p", port};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat());
        });
    }

    @Test
    void cliOptionPortWithNumericValueSetsPortParameterOnApiConfig() {
        String port = "8089";
        String[] args = new String[]{"-p", port};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat());

        String portOnApiConfig = apiConfig.getPort();

        assertEquals(port, portOnApiConfig);
    }

    @Test
    void cliOptionModelWithoutValueThrowsException() {
        String[] args = new String[]{"-m"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat());
        });
    }

    @Test
    void cliOptionsModelWithValueSetsModelParameterOnApiConfig() {
        String model = "model";
        String[] args = new String[]{"-m", model};

        APIConfig apiConfig = new APIConfig();
        JenAI.parseArguments(args, apiConfig, new Chat());

        String modelOnApiConfig = apiConfig.getModel();

        assertEquals(model, modelOnApiConfig);
    }

    @Test
    void cliOptionStreamingWithoutValueThrowsException() {
        String[] args = new String[]{"-s"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat());
        });
    }

    @Test
    void cliOptionStreamingWithNonBooleanValueThrowsException() {
        String streaming = "error";
        String[] args = new String[]{"-s", streaming};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat());
        });
    }

    @Test
    void cliOptionStreamingWithTrueCausesStreamingOnAPIConfig() {
        String streaming = "true";
        String[] args = new String[]{"-s", streaming};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat());

        boolean streamingOnApiConfig = apiConfig.getStreaming();

        assertEquals(Boolean.valueOf(streaming), streamingOnApiConfig);
    }

    @Test
    void cliOptionStreamingWithFalseCausesNonStreamingOnAPIConfig() {
        String streaming = "false";
        String[] args = new String[]{"-s", streaming};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat());

        boolean streamingOnApiConfig = apiConfig.getStreaming();

        assertEquals(Boolean.valueOf(streaming), streamingOnApiConfig);
    }

}