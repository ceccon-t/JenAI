package dev.ceccon;

import dev.ceccon.config.APIConfig;
import dev.ceccon.conversation.Chat;
import dev.ceccon.storage.CompositeStorage;
import dev.ceccon.storage.DatabaseStorage;
import dev.ceccon.storage.Storage;
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
    void cliOptionDatabaseEnabledWithoutValueThrowsException() {
        String[] args = new String[]{"-d"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionDatabaseWithNonBooleanValueThrowsException() {
        String databaseEnabled = "error";
        String[] args = new String[]{"-d", databaseEnabled};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionDatabaseEnabledDefaultsToNoDatabaseStorageOnCompositeStorage() {
        String[] args = new String[]{};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        assertFalse(compositeStorage.hasDatabaseStorage());
    }

    @Test
    void cliOptionDatabaseEnabledWithTrueCausesDatabaseStorageOnCompositeStorage() {
        String databaseEnabled = "true";
        String[] args = new String[]{"-d", databaseEnabled};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        assertTrue(compositeStorage.hasDatabaseStorage());
    }

    @Test
    void cliOptionDatabaseEnabledWithFalseCausesNoDatabaseStorageOnCompositeStorage() {
        String databaseEnabled = "false";
        String[] args = new String[]{"-d", databaseEnabled};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        assertFalse(compositeStorage.hasDatabaseStorage());
    }

    @Test
    void cliOptionDatabaseEngineWithoutValueThrowsException() {
        String[] args = new String[]{"-e"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    private DatabaseStorage getDatabaseStorage(CompositeStorage compositeStorage) {
        for (Storage s : compositeStorage.getStorages()) {
            if (s instanceof DatabaseStorage) return (DatabaseStorage) s;
        }
        return null;
    }

    @Test
    void cliOptionDatabaseEngineDefaultsToSqlite() {
        String[] args = new String[]{"-d", "true"};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage = getDatabaseStorage(compositeStorage);
        assertEquals("sqlite", dbStorage.getDatabaseEngine());
    }

    @Test
    void cliOptionDatabaseEngineSetsEngineOnDatabaseStorage() {
        String engine = "postgres";
        String[] args = new String[]{"-d", "true", "-e", engine};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage =getDatabaseStorage(compositeStorage);
        assertEquals(engine, dbStorage.getDatabaseEngine());
    }

    @Test
    void cliOptionDatabasePortWithoutValueThrowsException() {
        String[] args = new String[]{"-r"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionDatabasePortDefaultsToBlank() {
        String[] args = new String[]{"-d", "true"};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage = getDatabaseStorage(compositeStorage);
        assertEquals("", dbStorage.getPort());
    }

    @Test
    void cliOptionDatabasePortSetsPortOnDatabaseStorage() {
        String port = "1234";
        String[] args = new String[]{"-d", "true", "-r", port};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage =getDatabaseStorage(compositeStorage);
        assertEquals(port, dbStorage.getPort());
    }

    @Test
    void cliOptionDatabaseUsernameWithoutValueThrowsException() {
        String[] args = new String[]{"-u"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionDatabaseUsernameDefaultsToBlank() {
        String[] args = new String[]{"-d", "true"};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage = getDatabaseStorage(compositeStorage);
        assertEquals("", dbStorage.getUsername());
    }

    @Test
    void cliOptionDatabaseUsernameSetsUsernameOnDatabaseStorage() {
        String username = "user";
        String[] args = new String[]{"-d", "true", "-u", username};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage =getDatabaseStorage(compositeStorage);
        assertEquals(username, dbStorage.getUsername());
    }

    @Test
    void cliOptionDatabasePasswordWithoutValueThrowsException() {
        String[] args = new String[]{"-w"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

    @Test
    void cliOptionDatabasePasswordDefaultsToBlank() {
        String[] args = new String[]{"-d", "true"};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage = getDatabaseStorage(compositeStorage);
        assertEquals("", dbStorage.getPassword());
    }

    @Test
    void cliOptionDatabasePasswordSetsPasswordOnDatabaseStorage() {
        String password = "pass";
        String[] args = new String[]{"-d", "true", "-w", password};

        CompositeStorage compositeStorage = new CompositeStorage();

        JenAI.parseArguments(args, new APIConfig(), new Chat(), compositeStorage);

        DatabaseStorage dbStorage =getDatabaseStorage(compositeStorage);
        assertEquals(password, dbStorage.getPassword());
    }

    @Test
    void cliOptionInexistentThrowsException() {
        String[] args = new String[]{"-thisisnotanoption"};

        assertThrows(IllegalArgumentException.class, () -> {
            JenAI.parseArguments(args, new APIConfig(), new Chat(), new CompositeStorage());
        });
    }

}