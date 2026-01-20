package dev.ceccon;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import dev.ceccon.cli.CLISession;
import dev.ceccon.config.APIConfig;
import dev.ceccon.client.LLMClient;
import dev.ceccon.conversation.Chat;
import dev.ceccon.storage.CompositeStorage;
import dev.ceccon.storage.DatabaseStorage;
import dev.ceccon.storage.LocalFileStorage;

import java.io.IOException;

public class JenAI {

    public static void main(String[] args) throws IOException {

        APIConfig apiConfig = new APIConfig();
        Chat chat = Chat.initialize();
        CompositeStorage storage = new CompositeStorage();
        try {
            parseArguments(args, apiConfig, chat, storage);
        } catch (IllegalArgumentException e) {
            return;
        }

        LLMClient llmClient = new LLMClient(apiConfig);

        CLISession session = new CLISession(chat, apiConfig, llmClient, storage);
        session.start();
    }

    public static void parseArguments(String[] args, APIConfig apiConfig, Chat chat, CompositeStorage storage) throws IllegalArgumentException {
        JenAIArgs jenAIArgs = new JenAIArgs();
        JCommander cmd = JCommander.newBuilder()
                .addObject(jenAIArgs)
                .build();

        try {
            cmd.parse(args);
        } catch(ParameterException e) {
            throw new IllegalArgumentException("Could not parse parameter.");
        }

        if (jenAIArgs.hasPort()) {
            Integer port = jenAIArgs.getPort();
            apiConfig.setPort(port.toString());
        }

        if (jenAIArgs.hasModel()) {
            String model = jenAIArgs.getModel();
            apiConfig.setModel(model);
        }

        if (jenAIArgs.hasStreaming()) {
            boolean useStreamingResponse = jenAIArgs.getStreaming();
            apiConfig.setStreaming(useStreamingResponse);
        }

        if (jenAIArgs.hasTemperature()) {
            Double temperature = jenAIArgs.getTemperature();
            apiConfig.setTemperature(temperature);
        }

        if (!jenAIArgs.hasLocalStorageEnabled() || jenAIArgs.getLocalStorageEnabled() == true) {
            storage.addStorage(new LocalFileStorage());
        }
        if (jenAIArgs.hasDatabaseEnabled()) {
            String dbEngine = jenAIArgs.hasDatabaseEngine() ? jenAIArgs.getDatabaseEngine() : "sqlite";
            String port = jenAIArgs.hasDatabasePort() ? jenAIArgs.getDatabasePort() : "";
            String username = jenAIArgs.hasDatabaseUsername() ? jenAIArgs.getDatabaseUsername() : "";
            String password = jenAIArgs.hasDatabasePassword() ? jenAIArgs.getDatabasePassword() : "";
            String databaseName = dbEngine.equals("sqlite") ? "jenai.db" : "jenai";
            DatabaseStorage databaseStorage = new DatabaseStorage(dbEngine, port, username, password, databaseName);
            storage.addStorage(databaseStorage);
        }

        if (jenAIArgs.hasChat()) {
            try {
                String chatHistoryPathInput = jenAIArgs.getChat();
                Chat loadedChat = storage.load(chatHistoryPathInput);
                chat.loadConversationState(loadedChat);
            } catch (IOException e) {
                System.out.println("Could not parse chat history parameter. \nUsage: $ java -jar JenAI.jar -c <path_to_chat_json_file>");
                throw new IllegalArgumentException("Could not parse parameter.");
            }
        }

    }

    static class JenAIArgs {
        @Parameter(
                names = {"-p", "--port"},
                description = "LLM server port",
                required = false,
                arity = 1
        )
        private Integer port;

        @Parameter(
                names = {"-m", "--model"},
                description = "Model to use (when possible to choose)",
                required = false,
                arity = 1
        )
        private String model;

        @Parameter(
                names = {"-c", "--chat"},
                description = "Path to json of chat history to continue",
                required = false,
                arity = 1
        )
        private String chat;

        @Parameter(
                names = {"-s", "--streaming"},
                description = "Use streaming response mode",
                required = false,
                arity = 1
        )
        private Boolean streaming;

        @Parameter(
                names = {"-t", "--temperature"},
                description = "Temperature value to use when generating answers",
                required = false,
                arity = 1
        )
        private Double temperature;

        @Parameter(
                names = {"-l", "--localStorageEnabled"},
                description = "Enable using local filesystem for persistence",
                required = false,
                arity = 1
        )
        private Boolean localStorageEnabled;

        @Parameter(
                names = {"-d", "--databaseEnabled"},
                description = "Enable using database for persistence (file saving is not impacted)",
                required = false,
                arity = 1
        )
        private Boolean databaseEnabled;

        @Parameter(
                names = {"-e", "--databaseEngine"},
                description = "Determines which database is used (one of [postgres|sqlite])",
                required = false,
                arity = 1
        )
        private String databaseEngine;

        @Parameter(
                names = {"-r", "--databasePort"},
                description = "Port where to connect to database",
                required = false,
                arity = 1
        )
        private String databasePort;

        @Parameter (
                names = {"-u", "--databaseUsername"},
                description = "Username for login in database",
                required = false,
                arity = 1
        )
        private String databaseUsername;

        @Parameter(
                names = {"-w", "--databasePassword"},
                description = "Password for login in database",
                required = false,
                arity = 1
        )
        private String databasePassword;

        public Integer getPort() {
            return port;
        }

        public String getModel() {
            return model;
        }

        public String getChat() {
            return chat;
        }

        public Boolean getStreaming() {
            return streaming;
        }

        public Double getTemperature() {
            return temperature;
        }

        public Boolean getLocalStorageEnabled() { return localStorageEnabled; }

        public Boolean getDatabaseEnabled() {
            return databaseEnabled;
        }

        public String getDatabaseEngine() { return databaseEngine; }

        public String getDatabasePort() {
            return databasePort;
        }

        public String getDatabaseUsername() {
            return databaseUsername;
        }

        public String getDatabasePassword() {
            return databasePassword;
        }

        public boolean hasPort() {
            return port != null;
        }

        public boolean hasModel() {
            return model != null;
        }

        public boolean hasChat() {
            return chat != null;
        }

        public boolean hasStreaming() {
            return streaming != null;
        }

        public boolean hasTemperature() {
            return temperature != null;
        }

        public boolean hasLocalStorageEnabled() { return localStorageEnabled != null; }

        public boolean hasDatabaseEnabled() {
            return databaseEnabled != null;
        }

        public boolean hasDatabaseEngine() { return databaseEngine != null; }

        public boolean hasDatabasePort() {
            return databasePort != null;
        }

        public boolean hasDatabaseUsername() {
            return databaseUsername != null;
        }

        public boolean hasDatabasePassword() {
            return databasePassword != null;
        }

    }

}
