package dev.ceccon;

import dev.ceccon.config.APIConfig;
import dev.ceccon.conversation.Chat;
import dev.ceccon.storage.CompositeStorage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JenAITest {

    @Test
    void cliOptionPortWithoutValueThrowsException() {
        String[] args = new String[]{"-p"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionPortWithNonNumericValueThrowsException() {
        String port = "port";
        String[] args = new String[]{"-p", port};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionPortWithNumericValueSetsPortParameterOnApiConfig() {
        String port = "8089";
        String[] args = new String[]{"-p", port};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new CompositeStorage());

        String portOnApiConfig = apiConfig.getPort();

        assertEquals(port, portOnApiConfig);
    }

    @Test
    void cliOptionModelWithoutValueThrowsException() {
        String[] args = new String[]{"-m"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionsModelWithValueSetsModelParameterOnApiConfig() {
        String model = "model";
        String[] args = new String[]{"-m", model};

        APIConfig apiConfig = new APIConfig();
        JenAI.parseArguments(args, apiConfig, new Chat(), new CompositeStorage());

        String modelOnApiConfig = apiConfig.getModel();

        assertEquals(model, modelOnApiConfig);
    }

    @Test
    void cliOptionStreamingWithoutValueThrowsException() {
        String[] args = new String[]{"-s"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionStreamingWithNonBooleanValueThrowsException() {
        String streaming = "error";
        String[] args = new String[]{"-s", streaming};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionStreamingWithTrueCausesStreamingOnAPIConfig() {
        String streaming = "true";
        String[] args = new String[]{"-s", streaming};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new CompositeStorage());

        boolean streamingOnApiConfig = apiConfig.getStreaming();

        assertEquals(Boolean.valueOf(streaming), streamingOnApiConfig);
    }

    @Test
    void cliOptionStreamingWithFalseCausesNonStreamingOnAPIConfig() {
        String streaming = "false";
        String[] args = new String[]{"-s", streaming};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new CompositeStorage());

        boolean streamingOnApiConfig = apiConfig.getStreaming();

        assertEquals(Boolean.valueOf(streaming), streamingOnApiConfig);
    }

    @Test
    void cliOptionChatWithoutValueThrowsException() {
        String[] args = new String[]{"-c"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionChatWithPathNotFoundThrowsException() throws IOException {
        String pathNotFound = "notfound";
        String[] args = new String[]{"-c", pathNotFound};

        CompositeStorage storage = mock(CompositeStorage.class);
        when(storage.load(pathNotFound)).thenThrow(IOException.class);

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), storage);
        });
    }

    @Test
    void cliOptionChatWithPathFoundSetsInitialChat() throws IOException {
        Chat chatToLoad = Chat.initialize("Initial chat");
        String pathFound = "found";
        String[] args = new String[]{"-c", pathFound};

        CompositeStorage storage = mock(CompositeStorage.class);
        when(storage.load(pathFound)).thenReturn(chatToLoad);

        Chat chatLoaded = Chat.initialize();

        JenAI.parseArguments(args, new APIConfig(), chatLoaded, storage);

        assertEquals(chatToLoad.getMessages(), chatLoaded.getMessages());
    }

    @Test
    void cliOptionTemperatureWithoutValueThrowsException() {
        String[] args = new String[]{"-t"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionTemperatureWithNonNumericValueThrowsException() {
        String temperature = "temperature";
        String[] args = new String[]{"-t", temperature};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionTemperatureWithWholeNumberSetsTemperatureOnApiConfig() {
        String temperature = "5";
        String[] args = new String[]{"-t", temperature};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new CompositeStorage());

        double temperatureOnApiConfig = apiConfig.getTemperature();

        assertEquals(Double.valueOf(temperature), temperatureOnApiConfig);
    }

    @Test
    void cliOptionTemperatureWithDecimalNumberSetsTemperatureOnApiConfig() {
        String temperature = "0.5";
        String[] args = new String[]{"-t", temperature};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new CompositeStorage());

        double temperatureOnApiConfig = apiConfig.getTemperature();

        assertEquals(Double.valueOf(temperature), temperatureOnApiConfig);
    }

    @Test
    void cliOptionLocalStorageEnabledWithoutValueThrowsException() {
        String[] args = new String[]{"-l"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionLocalStorageWithNonBooleanValueThrowsException() {
        String localStorageEnabled = "error";
        String[] args = new String[]{"-l", localStorageEnabled};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionLocalStorageEnabledDefaultsToLocalStorageOnCompositeStorage() {
        String[] args = new String[]{};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        assertTrue(compositeStorage.hasLocalStorage());
    }

    @Test
    void cliOptionLocalStorageEnabledWithTrueCausesLocalStorageOnCompositeStorage() {
        String localStorageEnabled = "true";
        String[] args = new String[]{"-l", localStorageEnabled};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        assertTrue(compositeStorage.hasLocalStorage());
    }

    @Test
    void cliOptionLocalStorageEnabledWithFalseCausesNoLocalStorageOnCompositeStorage() {
        String localStorageEnabled = "false";
        String[] args = new String[]{"-l", localStorageEnabled};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        assertFalse(compositeStorage.hasLocalStorage());
    }

    @Test
    void cliOptionInexistentThrowsException() {
        String[] args = new String[]{"-thisisnotanoption"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

}