# UcumTerminologyCodesValidator

A Java application for validating UCUM (Unified Code for Units of Measure) codes using the FHIR UCUM library.

## Description

This project provides a simple command-line tool to check if a given UCUM code (e.g., "mg", "kg/m2", "mm[Hg]") is valid according to the official UCUM standard. It loads UCUM definitions from an XML file (`ucum-essence.xml`) and uses the FHIR UCUM service to analyze and validate units.

UCUM is a system for representing units of measure in a standardized way, commonly used in healthcare and scientific applications.

## Prerequisites

- Java 8 or higher
- Maven 3.x

## Dependencies

- FHIR UCUM Library (org.fhir:ucum:1.0.11-SNAPSHOT)
- Apache Log4j 2 (org.apache.logging.log4j:log4j-api:2.23.1 and log4j-core:2.23.1) for logging
- JUnit (for testing)

## Building the Project

1. Clone or download the project.
2. Navigate to the project root directory.
3. Run the following command to compile:

   ```bash
   mvn clean compile
   ```

## Running the Application

### Using Maven (Recommended)

Run the application with default arguments:

```bash
mvn exec:java
```

Run with custom arguments (path to XML and UCUM code):

```bash
mvn exec:java -Dexec.args="path/to/ucum-essence.xml your_ucum_code"
```

### Using Java Directly

After building, you can run it with:

```bash
java -cp target/classes:path/to/dependencies com.abdelaliboussadi.terminology.App path/to/ucum-essence.xml your_ucum_code
```

## Configuration

The application supports configuration through a `configuration.properties` file placed in the project root directory. This allows you to customize default values without modifying the code.

### Configuration File Format

Create a file named `configuration.properties` in the project root with the following properties:

```properties
# Default path to UCUM essence XML file
ucum.essence.path=C:/path/to/ucum-essence.xml

# Default UCUM code to validate when no arguments provided
ucum.default.code=ml

# Log file configuration
log.file.path=target/UcumTerminologyCodesValidator.txt

# Application settings
app.name=UCUM Terminology Codes Validator
app.version=1.0

# Validation settings
validation.timeout.seconds=30
```

### Configuration Override

You can override the configuration file location using a system property:

```bash
mvn exec:java -Dconfig.file=/path/to/custom.properties
```

### Priority Order

The application uses the following priority order for configuration:

1. **Command-line arguments** (highest priority)
2. **Configuration file properties**
3. **Hardcoded defaults** (lowest priority)

### Examples

- Valid code:
  ```
  mvn exec:java -Dexec.args="src/main/resources/ucum-essence.xml mg"
  ```
  Output: "Le code UCUM est VALIDE."

- Invalid code:
  ```
  mvn exec:java -Dexec.args="src/main/resources/ucum-essence.xml invalid_unit"
  ```
  Output: "Le code UCUM est INVALIDE ou INCONNU."

- Invalid arguments (empty):
  ```
  mvn exec:java -Dexec.args="src/main/resources/ucum-essence.xml ''"
  ```
  Output: Logs "the argument is missing", "Execution ended with an exception." and exits.

- Invalid path:
  ```
  mvn exec:java -Dexec.args=":\\invalid\\path.xml ml"
  ```
  Output: Logs "Chemin d'accès invalide pour le fichier de définition UCUM : :\\invalid\\path.xml", "Execution ended with an exception." and exits.

## Logging and Output

All execution results, including validation outcomes, errors, and execution status, are logged to a text file located at `target/UcumTerminologyCodesValidator.txt`. The logging uses Apache Log4j 2 with the following features:

- **Append Mode**: Each execution appends new log entries to the file without overwriting previous runs.
- **Execution Separation**: Each new execution is separated by a dedicated separator line for clarity.
- **Log Levels**: Includes INFO level messages for normal operations and ERROR level for issues.
- **Format**: Logs include timestamps, thread information, log level, logger name, and message.

The log file captures:
- Start of execution
- UCUM definition loading status
- Code analysis process
- Validation results
- Execution completion status (successful or with exception)
- Any errors or exceptions

For executions ending with exceptions, the log includes an "Execution ended with an exception." message and a separator line.

To view the results of all executions, check the `target/UcumTerminologyCodesValidator.txt` file after running the application.

## Running Tests

To run the unit tests:

```bash
mvn test
```

## Project Structure

- `src/main/java/com/abdelaliboussadi/terminology/UcumTerminologyCodesValidator.java`: Main application class.
- `src/main/java/com/abdelaliboussadi/terminology/PropertiesUtil.java`: Utility class for reading configuration properties.
- `src/main/resources/ucum-essence.xml`: UCUM unit definitions.
- `src/main/resources/log4j2.xml`: Log4j 2 configuration for logging setup.
- `configuration.properties`: Optional configuration file for customizing default values.
- `src/test/java/`: Unit tests.
- `target/UcumTerminologyCodesValidator.txt`: Generated log file containing execution results (created after first run).
- `pom.xml`: Maven configuration.

## Author

Abdelali Boussadi

- Email: boussadiabdelali@yahoo.fr
- LinkedIn: https://www.linkedin.com/in/abdelali-boussadi-ph-d-04307713/ 

## License

This project is licensed under the MIT License.

## Contributing

Feel free to submit issues or pull requests.

## Acknowledgments

- FHIR UCUM Library: https://github.com/FHIR/ucum
- UCUM Standard: https://ucum.org/</content>
<parameter name="filePath">c:\dev\git\ucum_client\ucum-client\README.md