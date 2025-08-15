# JetBrains TeamCity API E2E Tests

This repository contains end-to-end API tests for TeamCity that import a Kotlin DSL-based project from a Git repository using REST API and verify the project is
available after loading settings.

## Key Features

- **Spring Boot Integration**: Utilizes Spring Boot for setting up test configurations and running tests.
- **JUnit 5**: Provides a robust testing environment using the JUnit 5 framework.
- **REST Assured**: Facilitates easy and powerful REST API testing.
- **Logback for Logging**: Leverages Spring's default Logback for flexible and performant logging capabilities.
- **Lombok**: Simplifies Java code by reducing boilerplate.
- **Allure Reporting**: Integrates with Allure for generating detailed test reports.
- **GitHub Actions for CI**: Implements Continuous Integration (CI) using GitHub Actions to automatically build, test, and publish test reports. The CI workflow
  triggers on push, pull request, or manually, ensuring that the code is always in a deployable state.
- **Test Parallelization**: Supports both thread-based and fork-based parallelization strategies to speed up test execution.
- **Automatic Retry of Failing Tests**: Automatically retries failing tests to handle flaky tests or transient failures.
- **Docker Support**: Provides Docker support for running tests in a containerized environment, ensuring consistent test execution across different machines.

## Prerequisites

- Java 21
- Maven
- A running TeamCity server (baseUrl)
- Valid TeamCity credentials (admin or appropriate permissions)
- A Git repository hosting TeamCity Kotlin DSL (e.g., https://github.com/donesvad/teamcity-dsl-demo)

## Configuration

Configuration lives under src/test/resources. By default, application-dev.yml is used.

Properties under tc:

- baseUrl: TeamCity server URL (e.g., http://localhost:8111)
- username: TeamCity username
- password: TeamCity password
- projectId: Target TeamCity project ID to create/import under _Root
- dslRepoUrl: URL of your DSL repo (HTTPS)
- dslRepoBranch: Branch ref of your DSL (e.g., refs/heads/main)
- vcsAuthMethod: VCS auth method for the VCS root (ANONYMOUS | PASSWORD | PRIVATE_KEY_DEFAULT | PRIVATE_KEY_FILE)
- vcsUsername: Username for the VCS (required for PASSWORD)
- vcsToken: Personal Access Token (PAT) for the VCS; will be sent as secure:password

Example snippet:

```
tc:
  baseUrl: "http://localhost:8111"
  username: "admin"
  password: "<your-admin-password>"

  projectId: "DslSamples"

  dslRepoUrl: "https://github.com/donesvad/teamcity-dsl-demo.git"
  dslRepoBranch: "refs/heads/main"

  vcsAuthMethod: "PASSWORD"            # ANONYMOUS | PASSWORD | PRIVATE_KEY_DEFAULT | PRIVATE_KEY_FILE
  vcsUsername: "your-vcs-username"
  vcsToken: "${VCS_PAT:}"              # Supply via environment variable
```

Security tip:

- Do NOT hardcode tokens in files. Use environment variables instead:
    - macOS/Linux: export VCS_PAT=ghp_your_personal_token
    - Windows (PowerShell): $env:VCS_PAT = "ghp_your_personal_token"

If your repo is public and supports anonymous read, you can use:

- vcsAuthMethod: "ANONYMOUS"
- vcsUsername: ""
- vcsToken: ""

## What the test does

ImportDslFlowTest performs the following steps:

1. Clean up any existing project with the configured projectId (BeforeEach).
2. Create the project under the _Root parent if it does not exist.
3. Create a Git VCS root for your DSL repository, honoring the configured auth method (PAT supported via PASSWORD + secure:password).
4. Enable Versioned Settings for the project with:
    - format = KOTLIN
    - synchronizationMode = enabled
    - buildSettingsMode = useFromVCS
    - importDecision = importFromVCS
    - vcsRootId = <created VCS root id>
5. Trigger versioned settings load.
6. Verify the project exists. (You can extend assertions to validate build types if desired.)

## Running the tests

- Build (skip tests): mvn -q -DskipTests clean package
- Run tests: mvn -q test

Ensure your TeamCity server is reachable and credentials are correct.

## Docker and Docker Compose

This repository also includes Docker support to spin up a local TeamCity and run tests inside a container.

### 1) Start TeamCity locally via Docker Compose

```
docker compose up -d tc-server tc-agent
```

This will:

- Start TeamCity Server on http://localhost:8111 (data persists in ./tc_data)
- Start one TeamCity Agent
- Wait until the server is healthy (healthcheck included)

Note: The provided tc_data contains configuration to skip the initial maintenance screen. If running for the first time, allow the server a couple of minutes to
initialize.

### 2) Run tests in Docker via Docker Compose

The compose file now has a test runner service that builds the test image using the existing dockerfile and executes the test suite with a docker-specific
configuration file.

```
# Optionally export secrets
export VCS_PAT=ghp_your_token
export VCS_USERNAME=your_vcs_username

# Run tests (will wait for TeamCity to be healthy)
docker compose run --rm test-runner
```

What happens:

- The test container uses src/test/resources/application-docker.yml (selected by environment=docker) where tc.baseUrl points to http://tc-server:8111.
- VCS credentials can be supplied via environment variables VCS_PAT and VCS_USERNAME.
- Test results (Surefire, Allure) are written to ./target on the host (mounted from /app/target in the container).

To view logs of the server:

```
docker compose logs -f tc-server
```

To stop and clean containers:

```
docker compose down
```

## Debugging

You can enable full RestAssured request/response logging to compare the automated calls with manual steps. In src/test/resources/application-dev.yml:

```
log:
  rest-assured-requests: true
  rest-assured-responses: true
  rest-assured-only-on-fail: false
```

### Generating and Viewing Allure Reports

This project uses **Allure** to generate comprehensive and user-friendly test reports. Allure reports provide detailed insights into test execution, including
test results, logs, and visualizations.

#### Viewing the Allure Report

The latest Allure report for this project can be viewed online at the following link:

[View Allure Report Results](https://donesvad.github.io/jetbrains-teamcity-api-test/27/index.html)

This report includes:

- Test Results: Summary and details of passed, failed, and skipped tests.
- Test Suites: Breakdown of test suites and their execution results.
- Test History: Insights into test execution history and trends over time.
- Logs and Attachments: Detailed logs and any additional attachments captured during test execution.

#### How to Generate Allure Report Locally

To generate and view Allure reports locally, follow these steps:

1. **Navigate to the `target` directory:**
   ```bash
   cd target
   ```
2. Serve the Allure report:
   ```bash
   allure serve
   ```

This will start a local server and display the Allure report in your default web browser.

This command will start a local server and open the Allure report in your default web browser, allowing you to visualize the test results with detailed
insights.

Ensure that the Allure CLI is installed on your machine. If not, you can install it using the following commands:

```bash
brew install allure    # For macOS users using Homebrew
scoop install allure    # For Windows users using Scoop
```

Or follow the [installation instructions](https://allurereport.org/docs/install/) from the Allure documentation for other operating systems.

### Test Parallelization

To improve the efficiency and speed of the test execution, especially when dealing with a large number of test scenarios, this framework supports parallel
execution using both **thread-based** (JUnit 5) and **fork-based** (Maven Surefire plugin) parallelization strategies.

#### 1. Thread-Based Parallelization with JUnit 5

JUnit 5 natively supports parallel execution of tests using its configuration settings. You can run test methods or test classes concurrently, which helps speed
up the test suite execution.

**Configuration**
To enable thread-based parallelization with JUnit 5, you need to add the following configuration to your junit-platform.properties file, located in the
`src/test/resources` directory:

```properties
# junit-platform.properties
junit.jupiter.execution.parallel.enabled=true
junit.jupiter.execution.parallel.mode.default=concurrent
junit.jupiter.execution.parallel.config.strategy=dynamic
junit.jupiter.execution.parallel.config.dynamic.factor=2
```

In this configuration:

- **junit.jupiter.execution.parallel.enabled**: Enables parallel execution.
- **junit.jupiter.execution.parallel.mode.default**: Sets the default parallel execution mode. Use concurrent to run test classes and methods in parallel.
- **junit.jupiter.execution.parallel.config.strategy**: Defines the parallel execution strategy. Options include fixed or dynamic.
- **junit.jupiter.execution.parallel.config.dynamic.factor**: For dynamic strategy, this factor is multiplied by the number of available processors to determine
  the maximum number of threads to use. For example, if you have 4 CPUs and a factor of 2, JUnit will use up to 8 threads.

#### 2. Fork-Based Parallelization with Maven Surefire Plugin

Fork-based parallelization runs multiple instances of the JVM, each executing a portion of the test suite. This method is more resource-intensive but provides a
higher degree of isolation between test cases, making it suitable for tests that have significant memory or CPU demands.

**Configuration**
To enable fork-based parallelization in Maven, update the surefire plugin configuration in your `pom.xml` file:

```xml

<configuration>
  <forkCount>2</forkCount>
  <reuseForks>true</reuseForks>
</configuration>
```

In this configuration:

- **forkCount**: Specifies the number of JVM instances to run in parallel. You can use a fixed number (e.g., 2) or a dynamic value based on available CPUs.
- **reuseForks**: When set to true, Maven reuses the JVM instances for subsequent tests, reducing the overhead of JVM startup time.

By combining both thread-based and fork-based parallelization strategies, you can optimize test execution time and resource utilization for your test suite.

### Automatic Retry of Failing Tests

To handle flaky tests or tests that intermittently fail due to non-deterministic issues (such as network timeouts or temporary service unavailability), this
framework supports automatic retries of failing tests using the `rerunFailingTestsCount` feature of the Maven Surefire and Failsafe plugins.
