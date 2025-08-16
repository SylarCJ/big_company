package org.structure;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * The type Main test.
 */
public class MainTest {
    /**
     * Prints error message when file path is empty.
     */
    @Test
    void testPrintsErrorMessageWhenFilePathIsEmpty() {
        String[] args = {""};
        String result = captureOutput(() -> Main.main(args));
        assertEquals("The provided file path is empty. Please provide a valid file path.\r\n", result);
    }

    /**
     * Prints error message when file path is missing.
     */
    @Test
    void testPrintsErrorMessageWhenFilePathIsMissing() {
        String[] args = {};
        Exception exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> Main.main(args));
        assertEquals("Index 0 out of bounds for length 0", exception.getMessage());
    }

    /**
     * Prints error message for invalid data.
     */
    @Test
    void testPrintsErrorMessageForInvalidData() {
        String[] args = {"invalid_file_path.csv"};
        String result = captureOutput(() -> Main.main(args));
        assertEquals("Error : File not found: invalid_file_path.csv\r\n", result);
    }

    /**
     * Captures the output of a runnable.
     *
     * @param runnable the runnable to execute
     * @return the captured output as a string
     */
    private String captureOutput(Runnable runnable) {
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        System.setErr(new java.io.PrintStream(out));
        runnable.run();
        System.setOut(System.out);
        System.setErr(System.err);
        return out.toString();
    }
}