# UcumTerminologyCodesValidator

A Java application for validating and converting UCUM (Unified Code for Units of Measure) codes using the FHIR UCUM library.

## Description

This project provides a command-line tool to validate UCUM codes (e.g., "mg", "kg/m2", "mm[Hg]") and to convert values between UCUM units (e.g., `1 kg -> [lb_av]`). It loads UCUM definitions from an XML file (`ucum-essence.xml`) and uses the FHIR UCUM service to analyze, validate, and convert units.

UCUM is a system for representing units of measure in a standardized way, commonly used in healthcare and scientific applications.

## Prerequisites

- Java 8 or higher
- Maven 3.x

## Dependencies

- FHIR UCUM Library (org.fhir:ucum:1.0.11-SNAPSHOT)
- Apache Log4j 2 (org.apache.logging.log4j:log4j-api:2.23.1 and log4j-core:2.23.1) for logging
- JUnit (for testing)

## Building the Project

### Compiling

1. Clone or download the project.
2. Navigate to the project root directory.
3. Run the following command to compile:

   ```bash
   mvn clean compile
   ```

### Generating Executable JAR

To generate a standalone executable JAR file with all dependencies bundled:

```bash
mvn clean package
```

This command creates two JAR files in the `target/` directory:
- `UcumTerminologyCodesValidator-1.0.jar` - Basic JAR without dependencies
- `UcumTerminologyCodesValidator-1.0-executable.jar` - Executable uber JAR (≈2.3 MB) containing all dependencies (UCUM library, Log4j 2)

The executable JAR can be run on any system with Java 8+ without requiring Maven or additional dependencies.

## Running the Application

### Using Executable JAR (Recommended)

After generating the executable JAR with `mvn clean package`, run it directly:

```bash
java -jar target/UcumTerminologyCodesValidator-1.0-executable.jar <path-to-ucum-file> <ucum-code>
```

**Note**: The `configuration.properties` file must be present in the same directory where you run the command, as it's required for the application to work.

Example:
```bash
java -jar target/UcumTerminologyCodesValidator-1.0-executable.jar src/main/resources/ucum-essence.xml kg
```

The executable JAR includes all necessary dependencies and can be distributed as a standalone application.

### Using Maven

Run the application with default arguments (from configuration.properties):

```bash
mvn exec:java
```

Run validation with custom arguments (path to XML and UCUM code):

```bash
mvn exec:java "-Dexec.args=src/resources/ucum-essence.xml mg"
```

Run conversion with custom arguments (path, value, source unit, destination unit):

```bash
mvn exec:java "-Dexec.args=src/resources/ucum-essence.xml 1 kg [lb_av]"
```

## Configuration

The application supports configuration through a `configuration.properties` file placed in the project root directory. This allows you to customize default values without modifying the code.

### Configuration File Format

Create a file named `configuration.properties` in the project root with the following properties:

```properties
# Default path to UCUM essence XML file (REQUIRED)
ucum.essence.path=src/resources/ucum-essence.xml

# Default UCUM code to validate when no arguments provided (REQUIRED for validation)
ucum.default.code=ml

# Feature selection (REQUIRED)
codeValidation=true
codeConversion=false

# Conversion inputs (REQUIRED when codeConversion=true)
conversion.value=1
conversion.source.unit=kg
conversion.destination.unit=[lb_av]

# Log file configuration (REQUIRED)
log.file.path=target/UcumTerminologyCodesValidator.log.txt

# Application settings (REQUIRED)
app.name=UCUM Terminology Codes Validator
app.version=1.0
```

### Configuration Override

You can override the configuration file location using a system property:

```bash
mvn exec:java -Dconfig.file=/path/to/custom.properties
```

### Priority Order

The application uses the following priority order for configuration:

1. **Command-line arguments** (highest priority, when provided)
2. **Configuration file properties**

No hardcoded defaults are used.

### Examples

- Validation with arguments:
  ```
  mvn exec:java "-Dexec.args=src/resources/ucum-essence.xml mg"
  ```
  Output: "Le code UCUM est VALIDE."

- Conversion with arguments:
  ```
  mvn exec:java "-Dexec.args=src/resources/ucum-essence.xml 1 kg [lb_av]"
  ```
  Output: Logs "Résultat de conversion : 2.20462262184877580722974 [lb_av]".

- Invalid path:
  ```
  mvn exec:java "-Dexec.args=:\\invalid\\path.xml mg"
  ```
  Output: Logs "Chemin d'accès invalide pour le fichier de définition UCUM : :\\invalid\\path.xml", "Execution ended with an exception." and exits.

## Logging and Output

All execution results, including validation outcomes, conversion results, errors, and execution status, are logged to a text file located at `target/UcumTerminologyCodesValidator.log.txt`. The logging uses Apache Log4j 2 with the following features:

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

To view the results of all executions, check the `target/UcumTerminologyCodesValidator.log.txt` file after running the application.

## Running Tests

To run the unit tests:

```bash
mvn test
```

## Project Structure

- `src/main/java/com/abdelaliboussadi/terminology/UcumTerminologyCodesValidator.java`: Main application class (validation + conversion).
- `src/main/java/com/abdelaliboussadi/terminology/PropertiesUtil.java`: Utility class for reading configuration properties.
- `src/resources/ucum-essence.xml`: UCUM unit definitions.
- `src/main/resources/log4j2.xml`: Log4j 2 configuration for logging setup.
- `configuration.properties`: Required configuration file (features + inputs).
- `src/test/java/`: Unit tests.
- `target/UcumTerminologyCodesValidator.log.txt`: Generated log file containing execution results (created after first run).
- `target/UcumTerminologyCodesValidator-1.0-executable.jar`: Standalone executable JAR with all dependencies (generated by `mvn package`).
- `pom.xml`: Maven configuration.

## Author

Abdelali Boussadi

- Email: boussadiabdelali@yahoo.fr
- LinkedIn: https://www.linkedin.com/in/abdelali-boussadi-ph-d-04307713/ 

## License

This project is licensed under the **Apache License 2.0** with an **additional attribution clause**. 

Any use of this code must provide explicit attribution to:
- The original author: Abdelali Boussadi, PhD
- The original repository: https://github.com/3abdel3ali/ucum_client

See the [LICENSE](LICENSE) file for full details.

## Contributing

Feel free to submit issues or pull requests.

## Acknowledgments

- FHIR UCUM Library: https://github.com/FHIR/ucum
- UCUM Standard: https://ucum.org/</content>
<parameter name="filePath">c:\dev\git\ucum_client\ucum-client\README.md