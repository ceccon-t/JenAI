package dev.ceccon;

import dev.ceccon.config.APIConfig;
import dev.ceccon.conversation.Chat;
import dev.ceccon.storage.LocalFileStorage;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JenAITest {

    @Test
    void cliOptionPortWithoutValueThrowsException() {
        String[] args = new String[]{"-p"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new LocalFileStorage());
        });
    }

    @Test
    void cliOptionPortWithNonNumericValueThrowsException() {
        String port = "port";
        String[] args = new String[]{"-p", port};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new LocalFileStorage());
        });
    }

    @Test
    void cliOptionPortWithNumericValueSetsPortParameterOnApiConfig() {
        String port = "8089";
        String[] args = new String[]{"-p", port};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new LocalFileStorage());

        String portOnApiConfig = apiConfig.getPort();

        assertEquals(port, portOnApiConfig);
    }

    @Test
    void cliOptionModelWithoutValueThrowsException() {
        String[] args = new String[]{"-m"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new LocalFileStorage());
        });
    }

    @Test
    void cliOptionsModelWithValueSetsModelParameterOnApiConfig() {
        String model = "model";
        String[] args = new String[]{"-m", model};

        APIConfig apiConfig = new APIConfig();
        JenAI.parseArguments(args, apiConfig, new Chat(), new LocalFileStorage());

        String modelOnApiConfig = apiConfig.getModel();

        assertEquals(model, modelOnApiConfig);
    }

    @Test
    void cliOptionStreamingWithoutValueThrowsException() {
        String[] args = new String[]{"-s"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new LocalFileStorage());
        });
    }

    @Test
    void cliOptionStreamingWithNonBooleanValueThrowsException() {
        String streaming = "error";
        String[] args = new String[]{"-s", streaming};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new LocalFileStorage());
        });
    }

    @Test
    void cliOptionStreamingWithTrueCausesStreamingOnAPIConfig() {
        String streaming = "true";
        String[] args = new String[]{"-s", streaming};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new LocalFileStorage());

        boolean streamingOnApiConfig = apiConfig.getStreaming();

        assertEquals(Boolean.valueOf(streaming), streamingOnApiConfig);
    }

    @Test
    void cliOptionStreamingWithFalseCausesNonStreamingOnAPIConfig() {
        String streaming = "false";
        String[] args = new String[]{"-s", streaming};

        APIConfig apiConfig = new APIConfig();

        JenAI.parseArguments(args, apiConfig, new Chat(), new LocalFileStorage());

        boolean streamingOnApiConfig = apiConfig.getStreaming();

        assertEquals(Boolean.valueOf(streaming), streamingOnApiConfig);
    }

    @Test
    void cliOptionChatWithoutValueThrowsException() {
        String[] args = new String[]{"-c"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new LocalFileStorage());
        });
    }

    @Test
    void cliOptionChatWithPathNotFoundThrowsException() throws IOException {
        String pathNotFound = "notfound";
        String[] args = new String[]{"-c", pathNotFound};

        LocalFileStorage storage = mock(LocalFileStorage.class);
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

        LocalFileStorage storage = mock(LocalFileStorage.class);
        when(storage.load(pathFound)).thenReturn(chatToLoad);

        Chat chatLoaded = Chat.initialize();

        JenAI.parseArguments(args, new APIConfig(), chatLoaded, storage);

        assertEquals(chatToLoad.getMessages(), chatLoaded.getMessages());
    }

}