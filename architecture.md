# JenAI - Architecture

This file describes the overall architecture of the project, including its most important design decisions, code structure and frameworks used.

## Design decisions

An important early decision (or "axiom") for this project was to try to avoid as much as possible frameworks that offered an API that is too abstract. Instead, the idea was to focus on building by hand most of the relevant logic, as the project itself was an exercise in getting familiar with interacting with LLM models. After the initial implementation, some abstractions were adopted in places where the low-level bit wrangling offered no particular insight with regards to working with generative AI technologies.

Another important design decision is that of keeping, side-by-side, support to both block responses and streaming responses. A block response is one that is only received once the entire text has been computed by the LLM model, meaning that the interface is blocked while the model works; a streaming response is one in which each token is received as soon as it is produced by the model, so the response is streamed token by token on the interface. Their individual processing necessities are kept separate in the code, with only the most basic commonalities receiving an abstraction. This was done to keep this project a useful reference of both implementations, as each can be useful on its own. By not imposing strong abstractions, the code can be easily referenced in future projects, when one or the other is necessary. Block responses are mostly useful due to how easily they provide access to token usage metrics, while streaming responses are mostly useful for user experience and as a reference for handling server-sent events in general.

## Structure

### Project meta files

The main folder of the project is named `JenAI`, and it is present at the root of the repository. Also at the root, `README.md` gives a short presentation at a project level, while this `architecture.md` file should quickly put any developer up to speed as to how the code is laid out. The `images` folder at root level contains images used only in the presentation of the repository, not to be used inside the application.

### Code overview

The main class of the application is also the only one at the root package, `JenAI`. It processes the options used when starting the application, create the appropriate configurations and launches a new `CLISession` with these configurations.

The big picture view is that there are four main responsibilities in the project: keeping the history of a conversation, managing a session with the user, communicating with an LLM server through its API and handling the persistance of conversation states (either to disk or to a database). Each one of these is handled in a particular package, while a few other subpackages handle helper utilities.

The most relevant packages are described below.

#### Conversation

Classes under the `conversation` package serve to model the history of a conversation, and are in general very simple. They mostly offer ways to read the history and to add new messages to the history. In particular, `Message` was well-suited to being a simple record, while `Chat` keeps a list of messages that represent the entire conversation and an utility method to add a new message to the end of the history (effectively making it the most recent one).

#### Config

Classes that manage the configuration of the application. `AppConfig` is the main wrapper around all configuration classes, while `APIConfig` stores the configuration for the API.

#### CLI

There is only one class under the `cli` package, but it is an important one. Its role is to manage the state of the conversation by manipulating objects of the classes under the `conversation` package based on input from the user (through a command-line interface) and from the LLM (through API call responses). This is probably the best place to start in order to understand how everything fits together.

#### Client

Classes under the `client` package handle the interaction with the LLM server through its API. Another axiom of the project was that it would only work with local LLMs, so only the port to which requests are sent is configurable - the host is always hardcoded as `localhost`. `LLMClient` is responsible for making the requests and returning the responses in a format that the rest of the application can work with. `LLMSanitizer` offers some helper methods to remove problematic characters and special tokens from messages.

##### DTOs

Helper classes to model the objects used by the LLM server API.

##### Response

Classes that model the two types of responses that application works with: block and streaming. The main differences being: only block responses allow for gathering token usage metrics and displaying this to the user; meanwhile streaming responses (at least in OpenAI-like APIs) use server-sent events, which requires a more complex processing on receiving and the definition of a specific DTO for the events. Both types of responses come with specific auxiliary classes to handle their specificities.

### Storage

Helper classes to handle saving and loading conversations. In order to allow for both saving conversations to files in the local filesystem and to a database, the main class used is `CompositeStorage`, which acts just as an aggregator that is populated at startup with the classes for the desired behaviors and then propagate all calls to every object it contains. The specific persistence logic is handled by `LocalFileStorage` and `DatabaseStorage` classes. At startup the CLI options informed are parsed and for each of the behaviors requested the respective class has an object instantiated and added to the composite storage, ensuring it is then available during the session.

The interface `Storage` allows for all types of storage being used with a consistent API, so that the rest of the code usually does not have to worry about which type of storage is being used at any particular time. Variables for classes under this package should favor using this interface for their type, whenever possible.

## Automated Tests

Tests are written with JUnit 5, and the source files can be found under `JenAI/src/test` hierarchy. As this project started with an exploratory purpose and only afterwards was shifted into a more stable format, most of its initial development did not include automated tests - these are being added alongside new features, usually covering the relevant parts for the particular feature. Whenever possible, for new developments TDD is the approach used.

To execute all tests, run `mvn test` on the main folder of the application.

## CI/CD

The project uses Github Actions to automatically generate a new release whenever new changes that alter the application itself are pushed into the `main` branch.

If the build breaks, a red failure sign is displayed near the hash of the commit in the repository. If all goes well, a green success sign is displayed instead. A badge with the status of the latest build for the main branch is also displayed in the Readme of the project.

The script that defines the main workflow can be found under `.github/workflows/main-workflow.yml`.


## Libraries and Frameworks

[JCommander](https://jcommander.org/) for parsing command line options.

[Maven](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) as a build automation tool.

[JUnit](https://junit.org/junit5/docs/current/user-guide/) and [Mockito](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html) for automated tests.

[Github Actions](https://docs.github.com/en/actions/learn-github-actions) for continuous integration.

