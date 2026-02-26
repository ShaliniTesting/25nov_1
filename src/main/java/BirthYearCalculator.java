import java.time.Year;

/**
 * Provides utility methods for calculating a user's birth year based on their age.
 *
 * <p>This class uses {@link java.time.Year} for dynamic current year retrieval,
 * ensuring the calculation is always accurate regardless of when the program is run.
 * No deprecated {@code java.util.Date} or {@code java.util.Calendar} APIs are used.</p>
 *
 * <p>Following the <strong>Single Responsibility Principle</strong>, this class handles
 * birth year calculation logic only — it contains no I/O operations, no console
 * interaction, and no dependencies on external services. This makes it independently
 * testable via JUnit without any mocking or stubbing of I/O.</p>
 *
 * <p>Usage example:</p>
 * <pre>{@code
 *     int birthYear = BirthYearCalculator.calculateBirthYear(30);
 *     // If current year is 2026, birthYear will be 1996
 * }</pre>
 *
 * @see java.time.Year
 */
public class BirthYearCalculator {

    /**
     * Calculates the estimated birth year based on the given age.
     *
     * <p>This method retrieves the current year dynamically using
     * {@code Year.now().getValue()} and subtracts the provided age to determine
     * the birth year. The result assumes the user's birthday has already occurred
     * in the current calendar year.</p>
     *
     * <p><strong>Note on edge case:</strong> This calculation assumes the user's
     * birthday has already occurred this year. If the birthday has not yet occurred,
     * the actual birth year would be {@code (currentYear - age - 1)}. For precise
     * handling of this edge case, use the overloaded method
     * {@link #calculateBirthYear(int, boolean)}.</p>
     *
     * @param age the user's current age; must be a positive integer (greater than zero)
     * @return the calculated birth year as an {@code int}
     * @throws IllegalArgumentException if {@code age} is zero or negative
     */
    public static int calculateBirthYear(int age) {
        // Validate input — defense in depth: even though Main.java validates user input,
        // this method enforces its own contract to prevent misuse from any caller.
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be a positive integer.");
        }

        // Retrieve the current year dynamically from the system clock.
        // CRITICAL: The year is NEVER hardcoded — Year.now().getValue() ensures
        // correctness regardless of when this program is executed.
        int currentYear = Year.now().getValue();

        // Calculate the birth year by subtracting the user's age from the current year.
        // For example, if the current year is 2026 and the user is 30 years old,
        // the estimated birth year is 2026 - 30 = 1996.
        int birthYear = currentYear - age;

        return birthYear;
    }

    /**
     * Calculates the estimated birth year based on the given age, with an option
     * to account for whether the user's birthday has already occurred this year.
     *
     * <p>This overloaded method addresses the ±1 year ambiguity that arises when
     * a user's birthday has not yet occurred in the current calendar year. For example,
     * if a user says they are 30 years old but their birthday is next month, they were
     * actually born in the year {@code currentYear - age - 1}, not {@code currentYear - age}.</p>
     *
     * <p><strong>Interpretation:</strong></p>
     * <ul>
     *   <li>If {@code hasBirthdayOccurred} is {@code true}: the user has already turned
     *       {@code age} this year, so the birth year is {@code currentYear - age}.</li>
     *   <li>If {@code hasBirthdayOccurred} is {@code false}: the user has not yet turned
     *       {@code age + 1} this year, meaning they were born one year earlier than the
     *       simple subtraction suggests, so the birth year is {@code currentYear - age - 1}.</li>
     * </ul>
     *
     * @param age                 the user's current age; must be a positive integer (greater than zero)
     * @param hasBirthdayOccurred {@code true} if the user's birthday has already occurred
     *                            in the current calendar year; {@code false} otherwise
     * @return the calculated birth year as an {@code int}
     * @throws IllegalArgumentException if {@code age} is zero or negative
     */
    public static int calculateBirthYear(int age, boolean hasBirthdayOccurred) {
        // Validate input — same defense-in-depth approach as the primary method.
        if (age <= 0) {
            throw new IllegalArgumentException("Age must be a positive integer.");
        }

        // Retrieve the current year dynamically — never hardcoded.
        int currentYear = Year.now().getValue();

        // Calculate the birth year, adjusting for whether the birthday has occurred.
        // If the birthday has already occurred this year, the standard subtraction applies.
        // If the birthday has NOT yet occurred, we subtract an additional year because
        // the user was born one year earlier than the simple (currentYear - age) formula.
        int birthYear;
        if (hasBirthdayOccurred) {
            // Birthday already happened this year — standard calculation applies.
            birthYear = currentYear - age;
        } else {
            // Birthday has NOT yet occurred this year — adjust by subtracting one more year.
            // Example: current year 2026, age 30, birthday in December (not yet occurred in February)
            // → actual birth year is 2026 - 30 - 1 = 1995, not 1996.
            birthYear = currentYear - age - 1;
        }

        return birthYear;
    }
}
