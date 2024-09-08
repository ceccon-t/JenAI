package dev.ceccon;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.ceccon.cli.CLISession;
import dev.ceccon.client.APIConfig;
import dev.ceccon.client.LLMClient;
import dev.ceccon.conversation.Chat;
import dev.ceccon.storage.LocalFileStorage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidParameterException;

public class JenAI {

    private static APIConfig apiConfig = new APIConfig();
    private static Chat chat = Chat.initialize();

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {

        try {
            parseArguments(args);
        } catch (InvalidParameterException e) {
            return;
        }

        LLMClient llmClient = new LLMClient(apiConfig);
        LocalFileStorage storage = new LocalFileStorage();

        CLISession session = new CLISession(chat, llmClient, storage);
        session.start();
    }

    private static void parseArguments(String[] args) throws InvalidParameterException {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                try {
                    Integer port = Integer.parseInt(args[i+1]);
                    apiConfig.setPort(port.toString());
                    i += 1;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Could not parse port parameter. \nUsage: $ java -jar JenAI.jar -p <port_number>");
                    throw new InvalidParameterException("Could not parse parameter.");
                }
            } else if (args[i].equals("-m")) {
                try {
                    String model = args[i+1];
                    apiConfig.setModel(model);
                    i += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Could not parse model parameter. \nUsage: $ java -jar JenAI.jar -m <model_name>");
                    throw new InvalidParameterException("Could not parse parameter.");
                }
            } else if (args[i].equals("-c")) {
                try {
                    String chatHistoryPathInput = args[i+1];
                    i += 1;
                    Path filePath = Paths.get(chatHistoryPathInput.replace("\\", "/"));
                    chat = mapper.readValue(filePath.toFile(), Chat.class);
                } catch (ArrayIndexOutOfBoundsException | IOException e) {
                    System.out.println("Could not parse chat history parameter. \nUsage: $ java -jar JenAI.jar -c <path_to_chat_json_file>");
                    throw new InvalidParameterException("Could not parse parameter.");
                }
            } else {
                System.out.println("Could not recognize parameter '" + args[i] + "'.");
                throw new InvalidParameterException("Could not parse parameter.");
            }
        }
    }

}
