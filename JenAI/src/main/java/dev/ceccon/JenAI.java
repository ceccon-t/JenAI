package dev.ceccon;

import dev.ceccon.cli.CLISession;
import dev.ceccon.client.APIConfig;
import dev.ceccon.client.LLMClient;

import java.io.IOException;

public class JenAI {

    public static void main(String[] args) throws IOException {

        APIConfig config = new APIConfig();

        if (args.length > 0 && args[0].equals("-p")) {
            try {
                Integer port = Integer.parseInt(args[1]);
                config.setPort(port.toString());
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println("Could not parse port parameter. \nUsage: $ java -jar JenAI.jar -p <port_number>");
                return;
            }
        }

        LLMClient llmClient = new LLMClient(config);

        CLISession session = new CLISession(llmClient);
        session.start();
    }

}
