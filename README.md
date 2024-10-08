# JenAI

![Build status](https://github.com/ceccon-t/JenAI/actions/workflows/main-workflow.yml/badge.svg "Build status")

## Description

Simple CLI interface to chat with a local LLM.

![Short conversation about Open Source and haiku creation between user and assistant](https://raw.githubusercontent.com/ceccon-t/JenAI/main/images/JenAI_v1-0-0_sc0.png "Short conversation about Open Source and haiku creation between user and assistant")

## How to use

Just type your message when prompted and press Enter to send it.

The application manages the conversation as a chat between the user (you) and the assistant (the LLM model), so any new message you send will be answered considering the context of the previous messages, up until the context size limit of the model being used. After each answer from the assistant a token usage count will be displayed, in which you can check how many tokens were present in the context when answering - from that, you can have a sense of how close to the model's limit the conversation is getting. Once the limit is exceeded, the model will start losing the memory of the earliest messages in the conversation (it will probably still hallucinate answers about it if you ask).

You can save the conversation to disk by entering `(save)` as a message. This will create two files, one in plain text and one in json format, to a `jenai_chats` folder on the current working directory - if the folder does not exist, it will be created. When saving you can specify a custom filename to be used with `(save) <filename>`, otherwise the filename defaults to `JenAI_chat_` with the date and hour as suffix.

To exit the conversation, simply type `(exit)` as your next message and send it.

## Dependencies

The application assumes you have an LLM API listening on port 8080 of your computer (port number is configurable, check section below). If you have experience configuring and running them, any with an OpenAI-compatible API should work nicely - if you do not have experience, the easiest way to get one running is to use [llamafile](https://github.com/mozilla-Ocho/llamafile), which is both easy to use and has very good documentation on how to set it up.

Don't like the results you have been getting? Just try another model! Llamafile's documentation explains how to use a single executable to run different models, the same should be true for any other tool you might choose.

## How to run

The application is written in Java, so you will need to have the Java runtime installed. Assuming it has already been installed, either download the Jar file from the latest entry in the [Releases](https://github.com/ceccon-t/JenAI/releases) section of this repository or build the project following the instructions below, and execute it.

Example with llm server running on default port (8080):

`$ java -jar JenAI.jar`

Example using a custom port (8888, in this case):

`$ java -jar JenAI.jar -p 8888`

You can also start a conversation from a previously saved state (check `How to use` section for info about saving). For this, pass a `-c` option with the path from the current working directory to the json file that was saved (make sure to reference the JSON file, not the plain text one). Example:

`$ java -jar JenAI.jar -c ./jenai_chats/JenAI_chat_2024-09-08_12-00-00_7167862348.json`

If building the project with Maven, instead of `JenAI.jar` be sure to use the path to the generated jar, which will be in the `target` directory and have the version as a suffix.

### Run with ollama

If you are using ollama for the LLM server, you will have to at a minimum pass the name of the model you want to chat with when starting the application by using the `-m <model_name>` option. You will probably also want to use ollama's default port, 11434. Here is an example of how to chat with llama3.1 using ollama (check ollama's documentation for other model options):

`$ java -jar JenAI.jar -p 11434 -m llama3.1`

## How to build the project

This is a simple Maven project, so the easiest way to build it is running `mvn clean package` in the JenAI folder (assuming Maven is installed - if not, check its site and install from there). A jar file containing everything the application needs to run will be created at `JenAI/target/JenAI-<VERSION>.jar`.

## More info

To get a short intro to how the code is organized, you can check `architecture.md`.

