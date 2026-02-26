import java.time.Year;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Comprehensive JUnit 5 test class for the {@link BirthYearCalculator} utility class.
 *
 * <p>This class validates both overloads of the {@code calculateBirthYear} method:</p>
 * <ul>
 *   <li>{@code calculateBirthYear(int age)} — assumes the birthday has already occurred</li>
 *   <li>{@code calculateBirthYear(int age, boolean hasBirthdayOccurred)} — handles the
 *       ±1 year ambiguity when the birthday has or has not yet occurred this calendar year</li>
 * </ul>
 *
 * <p><strong>Test categories covered:</strong></p>
 * <ol>
 *   <li><strong>Valid age inputs</strong> — typical ages (1, 25, 50, 100) confirming correct subtraction</li>
 *   <li><strong>Boundary conditions</strong> — extreme valid values (age equal to current year, age 150)</li>
 *   <li><strong>Edge cases</strong> — birthday occurred vs. not-yet-occurred scenarios</li>
 *   <li><strong>Invalid inputs</strong> — negative and zero ages expecting {@code IllegalArgumentException}</li>
 * </ol>
 *
 * <p><strong>Design decisions:</strong></p>
 * <ul>
 *   <li>All expected birth year values are computed dynamically using
 *       {@code Year.now().getValue()}, stored in the {@code CURRENT_YEAR} constant.
 *       No year value is ever hardcoded, ensuring tests remain correct in any calendar year.</li>
 *   <li>No I/O operations are performed — tests validate pure calculation logic only.</li>
 *   <li>The test class resides in the default package (no package declaration) to match
 *       the production source code structure.</li>
 * </ul>
 *
 * @see BirthYearCalculator
 * @see BirthYearCalculator#calculateBirthYear(int)
 * @see BirthYearCalculator#calculateBirthYear(int, boolean)
 */
class BirthYearCalculatorTest {

    /**
     * The current calendar year, retrieved dynamically via {@code Year.now().getValue()}.
     * This constant is used by all test methods to compute expected birth year values,
     * ensuring no hardcoded year values appear anywhere in the test class.
     */
    private static final int CURRENT_YEAR = Year.now().getValue();

    // ========================================================================
    // Tests for Valid Age Inputs
    // ========================================================================

    /**
     * Verifies that a typical age of 25 produces the correct birth year.
     * Expected result: {@code CURRENT_YEAR - 25}.
     */
    @Test
    @DisplayName("Calculate birth year for age 25")
    void testCalculateBirthYearAge25() {
        // Arrange: compute the expected birth year dynamically
        int expected = CURRENT_YEAR - 25;

        // Act & Assert: verify the calculator returns the correct birth year
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(25));
    }

    /**
     * Verifies that the minimum valid age of 1 produces the correct birth year.
     * A person who is 1 year old was born in the previous calendar year.
     * Expected result: {@code CURRENT_YEAR - 1}.
     */
    @Test
    @DisplayName("Calculate birth year for age 1")
    void testCalculateBirthYearAge1() {
        // Arrange: compute the expected birth year for the youngest valid age
        int expected = CURRENT_YEAR - 1;

        // Act & Assert: verify correct calculation for minimum valid age
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(1));
    }

    /**
     * Verifies that a centenarian age of 100 produces the correct birth year.
     * This tests the calculation with a large but realistic age value.
     * Expected result: {@code CURRENT_YEAR - 100}.
     */
    @Test
    @DisplayName("Calculate birth year for age 100")
    void testCalculateBirthYearAge100() {
        // Arrange: compute the expected birth year for a centenarian
        int expected = CURRENT_YEAR - 100;

        // Act & Assert: verify the calculation handles large realistic ages correctly
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(100));
    }

    /**
     * Verifies that a middle-range age of 50 produces the correct birth year.
     * This provides coverage for a common mid-life age value.
     * Expected result: {@code CURRENT_YEAR - 50}.
     */
    @Test
    @DisplayName("Calculate birth year for age 50")
    void testCalculateBirthYearAge50() {
        // Arrange: compute the expected birth year for a mid-range age
        int expected = CURRENT_YEAR - 50;

        // Act & Assert: verify the calculator works for middle-range ages
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(50));
    }

    // ========================================================================
    // Tests for Boundary Conditions
    // ========================================================================

    /**
     * Verifies the mathematical boundary where the age equals the current year.
     * This represents a theoretical person born in year 0 (the maximum possible
     * age for the subtraction formula). While unrealistic, this test ensures the
     * calculation handles the extreme boundary correctly.
     * Expected result: {@code CURRENT_YEAR - CURRENT_YEAR = 0}.
     */
    @Test
    @DisplayName("Calculate birth year for age equal to current year")
    void testCalculateBirthYearAgeEqualsCurrentYear() {
        // Arrange: the expected result when age equals the current year is 0
        int expected = 0;

        // Act & Assert: verify the extreme boundary calculation produces year 0
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(CURRENT_YEAR));
    }

    /**
     * Verifies that a very large age of 150 produces the correct birth year.
     * While exceeding typical human lifespan, this value is still a valid positive
     * integer and must be processed correctly by the calculator.
     * Expected result: {@code CURRENT_YEAR - 150}.
     */
    @Test
    @DisplayName("Calculate birth year for age 150")
    void testCalculateBirthYearAge150() {
        // Arrange: compute the expected birth year for an exceptionally large age
        int expected = CURRENT_YEAR - 150;

        // Act & Assert: verify the calculation handles very large positive ages
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(150));
    }

    // ========================================================================
    // Tests for Edge Case — Birthday Occurrence
    // ========================================================================

    /**
     * Verifies the overloaded method when the user's birthday HAS already occurred
     * this calendar year. In this case, the standard subtraction applies:
     * {@code currentYear - age}.
     *
     * <p>Example: If the current year is 2026 and a 30-year-old's birthday was in January
     * (already passed), they were born in 2026 - 30 = 1996.</p>
     *
     * Expected result: {@code CURRENT_YEAR - 30}.
     */
    @Test
    @DisplayName("Calculate birth year when birthday has already occurred this year")
    void testCalculateBirthYearBirthdayOccurred() {
        // Arrange: when birthday has occurred, the standard formula applies
        int expected = CURRENT_YEAR - 30;

        // Act & Assert: verify the overloaded method with hasBirthdayOccurred = true
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(30, true));
    }

    /**
     * Verifies the overloaded method when the user's birthday has NOT yet occurred
     * this calendar year. In this case, the user was actually born one year earlier
     * than the simple subtraction suggests: {@code currentYear - age - 1}.
     *
     * <p>Example: If the current year is 2026 and a 30-year-old's birthday is in December
     * (not yet passed in February), they were born in 2026 - 30 - 1 = 1995, not 1996.</p>
     *
     * Expected result: {@code CURRENT_YEAR - 30 - 1}.
     */
    @Test
    @DisplayName("Calculate birth year when birthday has not yet occurred this year")
    void testCalculateBirthYearBirthdayNotOccurred() {
        // Arrange: when birthday hasn't occurred yet, subtract an additional year
        // because the person was born one year earlier than simple subtraction implies
        int expected = CURRENT_YEAR - 30 - 1;

        // Act & Assert: verify the overloaded method with hasBirthdayOccurred = false
        assertEquals(expected, BirthYearCalculator.calculateBirthYear(30, false));
    }

    // ========================================================================
    // Tests for Invalid Inputs
    // ========================================================================

    /**
     * Verifies that passing a negative age (-1) to the calculator throws an
     * {@code IllegalArgumentException}. Negative ages are physically impossible
     * and must be rejected by the validation logic.
     */
    @Test
    @DisplayName("Negative age throws IllegalArgumentException")
    void testNegativeAgeThrowsException() {
        // Act & Assert: verify that a small negative age is rejected with the correct exception
        assertThrows(IllegalArgumentException.class, () -> {
            BirthYearCalculator.calculateBirthYear(-1);
        });
    }

    /**
     * Verifies that passing a large negative age (-100) to the calculator throws an
     * {@code IllegalArgumentException}. This ensures the validation is not limited
     * to small negative numbers — all negative values must be rejected.
     */
    @Test
    @DisplayName("Large negative age throws IllegalArgumentException")
    void testLargeNegativeAgeThrowsException() {
        // Act & Assert: verify that a large negative age is also rejected
        assertThrows(IllegalArgumentException.class, () -> {
            BirthYearCalculator.calculateBirthYear(-100);
        });
    }

    /**
     * Verifies that passing zero as the age throws an {@code IllegalArgumentException}.
     * Zero is not a valid age — a person must be at least 1 year old for the birth
     * year calculation to be meaningful. The calculator's contract specifies that
     * {@code age} must be a positive integer (greater than zero).
     */
    @Test
    @DisplayName("Zero age throws IllegalArgumentException")
    void testZeroAgeThrowsException() {
        // Act & Assert: verify that zero age is treated as invalid input
        assertThrows(IllegalArgumentException.class, () -> {
            BirthYearCalculator.calculateBirthYear(0);
        });
    }
}
