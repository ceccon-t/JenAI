# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased] - YYYY-MM-DD

### Added

### Changed

### Deprecated

### Removed

### Fixed

### Security


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

