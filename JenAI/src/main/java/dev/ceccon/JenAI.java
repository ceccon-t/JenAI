package dev.ceccon;

import dev.ceccon.cli.CLISession;
import dev.ceccon.client.APIConfig;
import dev.ceccon.client.LLMClient;
import dev.ceccon.storage.LocalFileStorage;

import java.io.IOException;
import java.security.InvalidParameterException;

public class JenAI {

    public static void main(String[] args) throws IOException {

        APIConfig config;

        try {
            config = parseApiConfig(args);
        } catch (InvalidParameterException e) {
            return;
        }

        LLMClient llmClient = new LLMClient(config);
        LocalFileStorage storage = new LocalFileStorage();

        CLISession session = new CLISession(llmClient, storage);
        session.start();
    }

    private static APIConfig parseApiConfig(String[] args) throws InvalidParameterException {
        APIConfig config = new APIConfig();

        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                try {
                    Integer port = Integer.parseInt(args[i+1]);
                    config.setPort(port.toString());
                    i += 1;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    System.out.println("Could not parse port parameter. \nUsage: $ java -jar JenAI.jar -p <port_number>");
                    throw new InvalidParameterException("Could not parse parameter.");
                }
            } else if (args[i].equals("-m")) {
                try {
                    String model = args[i+1];
                    config.setModel(model);
                    i += 1;
                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Could not parse model parameter. \nUsage: $ java -jar JenAI.jar -m <model_name>");
                    throw new InvalidParameterException("Could not parse parameter.");
                }
            } else {
                System.out.println("Could not recognize parameter '" + args[i] + "'.");
                throw new InvalidParameterException("Could not parse parameter.");
            }
        }

        return config;
    }

}
