# Birth Year Calculator

A Java console application that calculates a user's birth year based on the age they enter. Built with the modern `java.time` API for accurate year retrieval, **Maven** for build management, and **JUnit 5** for unit testing.

The application follows clean code principles with a clear separation of concerns: user interaction logic resides in `Main.java`, while the reusable calculation logic is encapsulated in `BirthYearCalculator.java`.

---

## Prerequisites

| Requirement | Version | Notes |
|-------------|---------|-------|
| **Java Development Kit (JDK)** | 25 (Latest LTS) | Required for compilation and execution |
| **Apache Maven** | 3.9+ (recommend 3.9.12) | Required for building and testing |

> **Note:** This project has **no external service dependencies** — no databases, no network services, and no third-party runtime libraries are required. It runs entirely as a self-contained console application.

---

## Project Structure

```
birth-year-calculator/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   └── java/
    │       ├── Main.java
    │       └── BirthYearCalculator.java
    └── test/
        └── java/
            └── BirthYearCalculatorTest.java
```

| File | Role |
|------|------|
| `Main.java` | Application entry point — handles console I/O, input validation, and loop control |
| `BirthYearCalculator.java` | Reusable calculation class — computes birth year from age using `java.time.Year` |
| `BirthYearCalculatorTest.java` | JUnit 5 test class — validates calculation logic with various test cases |
| `pom.xml` | Maven project descriptor — declares JDK 25 compilation target and JUnit 5 dependency |

---

## Build Instructions

All build commands should be run from the project root directory.

```bash
# Compile the project
mvn clean compile

# Run unit tests
mvn test

# Package into a JAR
mvn clean package

# Full build (compile, test, package, and install to local repository)
mvn clean install
```

---

## Run Instructions

After building the project, run the application using:

```bash
java -cp target/classes Main
```

---

## Usage Example

Below is a sample interaction with the application:

```
Enter your age: 30
If you are 30 years old, you were born in 1996.

Would you like to calculate again? (yes/no): yes

Enter your age: 5
If you are 5 years old, you were born in 2021.

Would you like to calculate again? (yes/no): no
Goodbye!
```

> **Note:** The birth year is calculated dynamically based on the current year at runtime. The example above assumes the current year is 2026. Your results will reflect the actual current year when you run the application.

---

## Input Validation

The application validates all user input before performing any calculation:

| Input Type | Behavior |
|------------|----------|
| **Negative numbers** (e.g., `-5`) | Rejected with an error message prompting for a valid positive age |
| **Zero** (`0`) | Rejected with an error message — age must be a positive integer |
| **Non-numeric input** (e.g., `abc`) | Caught via `InputMismatchException`; displays a meaningful error and does not crash |

The program never crashes due to invalid input — all exceptions are explicitly caught and handled with user-friendly error messages.

---

## Technical Constraints

This project adheres to the following technical constraints and design decisions:

- **Modern API only** — Uses `java.time.Year` exclusively for current year retrieval. The deprecated `java.util.Date` and `java.util.Calendar` APIs are **never** used.
- **No hardcoded year** — The current year is always retrieved dynamically at runtime via `Year.now().getValue()`.
- **Explicit exception handling** — Every potential exception path is caught and handled with a meaningful error message. The application never terminates unexpectedly from user input.
- **Clean code principles** — Descriptive variable names (`userAge`, `birthYear`, `currentYear`), comprehensive comments explaining logic, and adherence to the Single Responsibility Principle (SRP).
- **Separation of concerns** — `Main.java` handles I/O; `BirthYearCalculator.java` handles computation. The calculator class is independently testable without console dependencies.

---

## License

This project is a **demo/educational application** created to demonstrate clean Java coding practices, Maven project structure, and JUnit 5 testing. It is intended for learning purposes.
