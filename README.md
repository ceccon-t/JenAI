# JenAI

## Description

Simple CLI interface to chat with a local LLM.

The project is under development, this Readme will be updated as more features are added.

## How to use

Just enter your message when prompted and press Enter to send it.

The application manages the conversation as a chat between the user (you) and the assistant (the LLM model), so any new message you send will be answered considering the context of the previous messages, up until the context size limit of the model being used. After each answer from the assistant a token usage count will be displayed, in which you can check how many tokens were present in the context when answering - from that, you can have a sense of how close to the model's limit the conversation is getting. Once the limit is exceeded, the model will start losing the memory of the earliest messages in the conversation (it will probably still hallucinate answers about it if you ask).

To exit the conversation, simply enter `(exit)` as your next message and send it.

## How to run

The application is written in Java, so you will need to have the Java runtime installed. Assuming it has alread been installed, either download the Jar file from the latest entry in the [Releases](https://github.com/ceccon-t/JenAI/releases) section of this repository or build the project following the instructions below, and execute it.

The application assumes you have an LLM API listening on port 8080 of your computer. If you have experience configuring and running them, any with an OpenAI-compatible API should work nicely - if you do not have experience, the easiest way to get one running is to use [llamafile](https://github.com/mozilla-Ocho/llamafile), which is both easy to use and has very good documentation on how to set it up.

Don't like the results you have been getting? Just try another model! Llamafile's documentation explains how to use a single executable to run different models, the same should be true for any other tool you might choose.

## How to build the project

This is a simple Maven project, so the easiest way to build it is running `mvn clean package` in the JenAI folder (assuming Maven is installed - if not, check its site and install from there). A jar file containing everything the application needs to run will be created at `JenAI/target/JenAI-<VERSION>.jar`.

## More info

Coming soon.

