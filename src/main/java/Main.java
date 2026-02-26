import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * Entry point for the Birth Year Calculator console application.
 *
 * <p>This class handles all user interaction including input prompting,
 * input validation, result display, and loop control for repeated
 * calculations. All computation logic is delegated to
 * {@link BirthYearCalculator} following the Single Responsibility Principle.</p>
 *
 * <p>The application reads the user's age from the console, validates it
 * (rejecting non-numeric input, negative numbers, and zero), calculates
 * the estimated birth year via {@link BirthYearCalculator#calculateBirthYear(int)},
 * and displays the result. Users can perform multiple calculations without
 * restarting the program.</p>
 *
 * <p><strong>Input validation rules enforced:</strong></p>
 * <ul>
 *   <li>Non-numeric input is rejected with a meaningful error message</li>
 *   <li>Negative numbers are rejected — age cannot be negative</li>
 *   <li>Zero is rejected — age must be greater than zero</li>
 * </ul>
 *
 * <p>Usage: Run the application from the command line and follow the prompts.</p>
 * <pre>{@code
 *     java Main
 *     Enter your age: 30
 *     If you are 30 years old, you were born in 1996.
 *     Would you like to calculate again? (yes/no): no
 *     Goodbye!
 * }</pre>
 *
 * @see BirthYearCalculator
 */
public class Main {

    /**
     * Application entry point. Starts the interactive Birth Year Calculator loop.
     *
     * <p>This method creates a {@link Scanner} bound to {@code System.in} for
     * reading user input, runs a loop that accepts age input, validates it,
     * computes the birth year via {@link BirthYearCalculator#calculateBirthYear(int)},
     * and displays the result in the format:
     * {@code If you are <age> years old, you were born in <birthYear>.}</p>
     *
     * <p>After each successful calculation, the user is prompted whether they
     * wish to perform another calculation. The loop continues until the user
     * declines. Invalid inputs (non-numeric, negative, zero) are caught and
     * reported with meaningful error messages — the program never crashes
     * from user input.</p>
     *
     * @param args command-line arguments (not used by this application)
     */
    public static void main(String[] args) {
        // Create a Scanner instance for reading user input from the console (System.in)
        Scanner scanner = new Scanner(System.in);

        // Loop control variable — remains true while the user wants to continue calculating
        boolean continueCalculating = true;

        // Main calculation loop — allows repeated calculations without restarting the program
        while (continueCalculating) {
            // Prompt the user to enter their age
            System.out.print("Enter your age: ");

            // --- Input Validation: Non-numeric input detection ---
            // Pre-check whether the next token in the input stream is a valid integer.
            // If not, display an error message and restart the loop immediately.
            if (!scanner.hasNextInt()) {
                // Check if stdin is exhausted (EOF/pipe closed) — if so, exit gracefully.
                // This prevents NoSuchElementException when running with piped or redirected input.
                if (!scanner.hasNext()) {
                    break;
                }
                System.out.println("Invalid input. Please enter a valid number.");
                // Consume the invalid (non-integer) token to prevent an infinite loop
                scanner.next();
                continue;
            }

            // --- Read the age value with a safety-net try-catch ---
            // Even though hasNextInt() pre-checks the input, wrapping nextInt() in a
            // try-catch for InputMismatchException provides defense-in-depth against
            // any unexpected edge cases in the Scanner's input stream.
            int age;
            try {
                age = scanner.nextInt();
            } catch (InputMismatchException e) {
                // Safety-net: handle any unexpected non-numeric input that bypassed hasNextInt()
                System.out.println("Invalid input. Please enter a valid number.");
                // Consume the invalid token to clear the scanner buffer, if available.
                // Guard with hasNext() to prevent NoSuchElementException on stdin EOF.
                if (scanner.hasNext()) {
                    scanner.next();
                }
                continue;
            }

            // Consume the trailing newline character left in the buffer after nextInt().
            // This prevents the newline from being inadvertently read by subsequent
            // nextLine() calls (e.g., the continue/exit prompt).
            // Guard with hasNextLine() to prevent NoSuchElementException on stdin EOF.
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }

            // --- Input Validation: Reject negative numbers ---
            // A negative age is not meaningful for birth year calculation.
            if (age < 0) {
                System.out.println("Invalid input. Age cannot be negative.");
                continue;
            }

            // --- Input Validation: Reject zero ---
            // An age of zero is not valid for birth year calculation since it would
            // simply return the current year, which is not a meaningful result.
            if (age == 0) {
                System.out.println("Invalid input. Age must be greater than zero.");
                continue;
            }

            // --- Input Validation: Reject unreasonably large ages ---
            // Prevent integer overflow in birth year calculation by capping age at
            // a realistic maximum. The oldest verified human lived to 122 years;
            // 150 provides a generous upper bound while preventing overflow.
            if (age > 150) {
                System.out.println("Invalid input. Please enter a realistic age (1-150).");
                continue;
            }

            // --- Calculation: Delegate to BirthYearCalculator ---
            // Call the reusable static calculation method to compute the birth year.
            // BirthYearCalculator uses java.time.Year.now() internally to retrieve the
            // current year dynamically — no hardcoded year values exist in this application.
            int birthYear = BirthYearCalculator.calculateBirthYear(age);

            // --- Output: Display result in the exact required format ---
            // The output format "If you are <age> years old, you were born in <birthYear>."
            // is a hard requirement specified in the project requirements.
            System.out.println("If you are " + age + " years old, you were born in " + birthYear + ".");

            // --- Continue Prompt: Ask if the user wants to perform another calculation ---
            System.out.print("Would you like to calculate again? (yes/no): ");

            // Guard against NoSuchElementException when stdin is exhausted (piped/redirected input).
            // If no more input is available, exit the loop gracefully instead of crashing.
            if (!scanner.hasNextLine()) {
                break;
            }
            String response = scanner.nextLine().trim();

            // If the user enters anything other than "yes" (case-insensitive), exit the loop.
            // This means "no", "No", "exit", or any other non-"yes" input will end the program.
            if (!response.equalsIgnoreCase("yes")) {
                continueCalculating = false;
            }
        }

        // Display a farewell message before exiting the application
        System.out.println("Goodbye!");

        // Close the Scanner to release the System.in resource
        scanner.close();
    }
}
