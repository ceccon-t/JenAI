# JenAI - Architecture

## Structure

### Project meta files

The main folder of the project is named `JenAI`, and it is present at the root of the repository. Also at the root, `README.md` gives a short presentation at a project level, while this `architecture.md` file should quickly put any developer up to speed as to how the code is laid out. The `images` folder at root level contains images used only in the presentation of the repository, not to be used inside the application.

### Code overview

An important early decision (or "axiom") for this project was to try to avoid as much as possible frameworks that offered an API that is too abstract. Instead, the idea was to focus on building by hand most of the relevant logic, as the project itself was an exercise in getting familiar with interacting with LLM models. After the initial implementation, some abstractions were adopted in places where the low-level bit wrangling offered no particular insight with regards to working with generative AI technologies.

The main class of the application is also the only one at the root package, `JenAI`. It processes the options used when starting the application, create the appropriate configurations and launches a new `CLISession` with these configurations.

The big picture view is that there are three main responsibilities in the project: keeping the history of a conversation, managing a session with the user and communicating with an LLM server through its API. Each one of these is handled in a particular package, while a few other packages handle helper utilities.

The most relevant packages are described below.

### Conversation

Classes under the `conversation` package serve to model the history of a conversation, and are in general very simple. They mostly offer ways to read the history and to add new messages to the history. In particular, `Message` was well-suited to being a simple record, while `Chat` keeps a list of messages that represent the entire conversation and an utility method to add a new message to the end of the history (effectively making it the most recent one).

### CLI

There is only one class under the `cli` package, but it is an important one. Its role is to manage the state of the conversation by manipulating objects of the classes under the `conversation` package based on input from the user (through a command-line interface) and from the LLM (through API call responses). This is probably the best place to start in order to understand how everything fits together.

### Client

Classes under the `client` package handle the interaction with the LLM server through its API. Another axiom of the project was that it would only work with local LLMs, so only the port to which requests are sent is configurable - the host is always hardcoded as `localhost`. `LLMClient` is responsible for making the requests and returning the responses in a format that the rest of the application can work with. `APIConfig` stores the configuration for the API, while `LLMSanitizer` offers some helper methods to remove problematic characters and special tokens from messages.

#### DTOs

Helper classes to model the objects used by the LLM server API.

### Storage

Helper classes to handle saving conversations.


## CI/CD

The project uses Github Actions to automatically generate a new release whenever new changes that alter the application itself are pushed into the `main` branch.

If the build breaks, a red failure sign is displayed near the hash of the commit in the repository. If all goes well, a green success sign is displayed instead. A badge with the status of the latest build for the main branch is also displayed in the Readme of the project.

The script that defines the main workflow can be found under `.github/workflows/main-workflow.yml`.


## Libraries and Frameworks

[Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) as a build automation tool.

[Github Actions](https://docs.github.com/en/actions/learn-github-actions) for continuous integration.

