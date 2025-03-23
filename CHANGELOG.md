# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - YYYY-MM-DD

### Added

### Changed

- Sanitizes existing chat messages when loading from previous state

### Deprecated

### Removed

### Fixed

- Sanitizes one more variation of upper case A
- Sanitizes variations of single and double quotes

### Security


## [1.7.1] - 2024-11-30

### Added

- Unit tests for command line options

### Changed

- Refactored configuration classes into new package
- Use JCommander library to parse command line options


## [1.7.0] - 2024-11-01

### Added

- Command line parameter to define temperature to be used when generating response


## [1.6.0] - 2024-10-26

### Added

- Option to use streaming response
- First unit tests

### Changed

- Use more suitable exception when command line arguments are invalid
- Internal refactor: encapsulate DTO classes on client package


## [1.5.1] - 2024-10-01

### Changed

- Refactor: moved DTOs package from top level to subpackage of client package (no end user change)


## [1.5.0] - 2024-09-25

### Added

- Use custom filename when saving conversations

### Changed

- Expanded list of commands displayed when starting application


## [1.4.1] - 2024-09-23

### Changed

- Improved separation between messages on CLI session


## [1.4.0] - 2024-09-08

### Added

- Command line parameter to load conversation state from file when starting application


## [1.3.0] - 2024-09-07

### Added

- Command to save conversation state to file


## [1.2.0] - 2024-09-02

### Added

- Command line parameter to choose model to be used in conversation
- Support to ollama as a backend server, due to set of changes implemented

### Fixed

- Parsing response from LLM server just ignores unknown fields instead of crashing


## [1.1.1] - 2024-08-26

### Fixed

- Removed debugging message when starting with custom port
- Sanitize tilde-accented vowels and dash look-alike to avoid crash

### Security

- Upgraded version of jackson due to dependabot pointing out vulnerabilities in the previous one


## [1.1.0] - 2024-08-22

### Added

- Command line parameter to choose port on which to reach LLM API


## [1.0.0] - 2024-08-19

### Added

- Initial release
- Consume LLM API with fixed configuration (localhost, port 8080)
- Chat between user and JenAI assistant
- Display token usage metrics after each response

