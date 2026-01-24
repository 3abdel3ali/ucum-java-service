# UCUM Client

A Java application for validating UCUM (Unified Code for Units of Measure) codes using the FHIR UCUM library.

## Description

This project provides a simple command-line tool to check if a given UCUM code (e.g., "mg", "kg/m2", "mm[Hg]") is valid according to the official UCUM standard. It loads UCUM definitions from an XML file (`ucum-essence.xml`) and uses the FHIR UCUM service to analyze and validate units.

UCUM is a system for representing units of measure in a standardized way, commonly used in healthcare and scientific applications.

## Prerequisites

- Java 8 or higher
- Maven 3.x

## Dependencies

- FHIR UCUM Library (org.fhir:ucum:1.0.11-SNAPSHOT)
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

## Usage

The application takes two command-line arguments:

1. **Path to UCUM essence XML file**: The file containing UCUM unit definitions (default: `src/main/resources/ucum-essence.xml`).
2. **UCUM code to validate**: The unit code to check (e.g., "mg", "kg/m2").

If no arguments are provided, it uses default values (the XML in resources and a test code "mgHH").

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

## Running Tests

To run the unit tests:

```bash
mvn test
```

## Project Structure

- `src/main/java/com/abdelaliboussadi/terminology/App.java`: Main application class.
- `src/main/resources/ucum-essence.xml`: UCUM unit definitions.
- `src/test/java/`: Unit tests.
- `pom.xml`: Maven configuration.

## License

This project is licensed under the MIT License.

## Contributing

Feel free to submit issues or pull requests.

## Acknowledgments

- FHIR UCUM Library: https://github.com/FHIR/ucum
- UCUM Standard: https://ucum.org/</content>
<parameter name="filePath">c:\dev\git\ucum_client\ucum-client\README.md