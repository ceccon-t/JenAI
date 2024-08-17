# JenAI

## Description

Simple CLI interface to chat with a local LLM.

The project is under development, this Readme will be updated as more features are added.

## How to run

The application is written in Java, so you will need to have the Java runtime installed. Assuming it has alread been installed, either download the Jar file from the latest entry in the [Releases](https://github.com/ceccon-t/JenAI/releases) section in this repository (once the first release is ready) or build the project following the instructions below, and execute it.

The application assumes you have an LLM API listening on port 8080 of your computer. If you have experience configuring and running them, any with an OpenAI-compatible API should work nicely - if you do not have experience, the easiest way to get one running is to use [llamafile](https://github.com/mozilla-Ocho/llamafile), which is both easy to use and has very good documentation on how to set it up.

## How to build the project

This is a simple Maven project, so the easiest way to build it is running `mvn clean package` in the JenAI folder (assuming Maven is installed - if not, check its site and install from there). A jar file containing everything the application needs to run will be created at `JenAI/target/JenAI-<VERSION>.jar`.

## More info

Coming soon.

