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

}