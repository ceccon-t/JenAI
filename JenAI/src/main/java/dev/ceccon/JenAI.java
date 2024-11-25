package dev.ceccon;

import dev.ceccon.cli.CLISession;
import dev.ceccon.config.APIConfig;
import dev.ceccon.client.LLMClient;
import dev.ceccon.conversation.Chat;
import dev.ceccon.storage.LocalFileStorage;
import org.apache.commons.cli.*;

import java.io.IOException;

public class JenAI {

    public static void main(String[] args) throws IOException {

        APIConfig apiConfig = new APIConfig();
        Chat chat = Chat.initialize();
        LocalFileStorage storage = new LocalFileStorage();
        try {
            parseArguments(args, apiConfig, chat, storage);
        } catch (IllegalArgumentException e) {
            return;
        }

        LLMClient llmClient = new LLMClient(apiConfig);

        CLISession session = new CLISession(chat, apiConfig, llmClient, storage);
        session.start();
    }

    public static void parseArgumentsWithCliCommons(String[] args, APIConfig apiConfig, Chat chat, LocalFileStorage storage) throws IllegalArgumentException {
        Options options = new Options();
        options.addOption(new Option("p", "port", true, "LLM server port"));
        options.addOption(new Option("m", "model", true, "Model to use (when possible to choose)"));
        options.addOption(new Option("c", "chat", true, "Path to json of chat history to continue"));
        options.addOption(new Option("s", "streaming", true, "Use streaming response mode"));
        options.addOption(new Option("t", "temperature", true, "Temperature value to use when generating answers"));

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("JenAI", options);
            throw new IllegalArgumentException("Could not parse parameter.");
        }

        if (cmd.hasOption("p")) {
            try {
                Integer port = Integer.parseInt(cmd.getOptionValue("p"));
                apiConfig.setPort(port.toString());
            } catch (NumberFormatException e) {
                System.out.println("Could not parse port parameter. \nUsage: $ java -jar JenAI.jar -p <port_number>");
                throw new IllegalArgumentException("Could not parse parameter.");
            }
        }

        if (cmd.hasOption("m")) {
            String model = cmd.getOptionValue("m");
            apiConfig.setModel(model);
        }

        if (cmd.hasOption("c")) {
            try {
                String chatHistoryPathInput = cmd.getOptionValue("c");
                Chat loadedChat = storage.load(chatHistoryPathInput);
                chat.loadConversationState(loadedChat);
            } catch (IOException e) {
                System.out.println("Could not parse chat history parameter. \nUsage: $ java -jar JenAI.jar -c <path_to_chat_json_file>");
                throw new IllegalArgumentException("Could not parse parameter.");
            }
        }

        if (cmd.hasOption("s")) {
            try {
                String streamingArgument = cmd.getOptionValue("s");
                if (!streamingArgument.equals("true") && !streamingArgument.equals("false")) throw new IllegalArgumentException("Boolean string was not <true|false>");
                boolean useStreamingResponse = Boolean.valueOf(streamingArgument);
                apiConfig.setStreaming(useStreamingResponse);
            } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
                System.out.println("Could not parse streaming response parameter. \nUsage: $ java -jar JenAI.jar -s <true|false>");
                throw new IllegalArgumentException("Could not parse parameter.");
            }
        }

        if (cmd.hasOption("t")) {
            try {
                Double temperature = Double.parseDouble(cmd.getOptionValue("t"));
                apiConfig.setTemperature(temperature);
            } catch (NumberFormatException e) {
                System.out.println("Could not parse temperature parameter. \nUsage: $ java -jar JenAI.jar -t <temperature>");
                throw new IllegalArgumentException("Could not parse parameter.");
            }
        }

    }

    public static void parseArguments(String[] args, APIConfig apiConfig, Chat chat, LocalFileStorage storage) throws IllegalArgumentException {
        parseArgumentsWithCliCommons(args, apiConfig, chat, storage);
        return;
//        for (int i = 0; i < args.length; i++) {
//            if (args[i].equals("-p")) {
//                try {
//                    Integer port = Integer.parseInt(args[i+1]);
//                    apiConfig.setPort(port.toString());
//                    i += 1;
//                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
//                    System.out.println("Could not parse port parameter. \nUsage: $ java -jar JenAI.jar -p <port_number>");
//                    throw new IllegalArgumentException("Could not parse parameter.");
//                }
//            } else if (args[i].equals("-m")) {
//                try {
//                    String model = args[i+1];
//                    apiConfig.setModel(model);
//                    i += 1;
//                } catch (ArrayIndexOutOfBoundsException e) {
//                    System.out.println("Could not parse model parameter. \nUsage: $ java -jar JenAI.jar -m <model_name>");
//                    throw new IllegalArgumentException("Could not parse parameter.");
//                }
//            } else if (args[i].equals("-c")) {
//                try {
//                    String chatHistoryPathInput = args[i+1];
//                    i += 1;
//                    Chat loadedChat = storage.load(chatHistoryPathInput);
//                    chat.loadConversationState(loadedChat);
//                } catch (ArrayIndexOutOfBoundsException | IOException e) {
//                    System.out.println("Could not parse chat history parameter. \nUsage: $ java -jar JenAI.jar -c <path_to_chat_json_file>");
//                    throw new IllegalArgumentException("Could not parse parameter.");
//                }
//            } else if (args[i].equals("-s")) {
//                try {
//                    String streamingArgument = args[i+1];
//                    if (!streamingArgument.equals("true") && !streamingArgument.equals("false")) throw new IllegalArgumentException("Boolean string was not <true|false>");
//                    boolean useStreamingResponse = Boolean.valueOf(streamingArgument);
//                    apiConfig.setStreaming(useStreamingResponse);
//                    i += 1;
//                } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
//                    System.out.println("Could not parse streaming response parameter. \nUsage: $ java -jar JenAI.jar -s <true|false>");
//                    throw new IllegalArgumentException("Could not parse parameter.");
//                }
//            } else if (args[i].equals("-t")) {
//                try {
//                    Double temperature = Double.parseDouble(args[i+1]);
//                    apiConfig.setTemperature(temperature);
//                    i += 1;
//                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
//                    System.out.println("Could not parse temperature parameter. \nUsage: $ java -jar JenAI.jar -t <temperature>");
//                    throw new IllegalArgumentException("Could not parse parameter.");
//                }
//            } else {
//                System.out.println("Could not recognize parameter '" + args[i] + "'.");
//                throw new IllegalArgumentException("Could not parse parameter.");
//            }
//        }
    }

}
