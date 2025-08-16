package org.structure.handler;

import org.junit.jupiter.api.Test;
import org.structure.entity.Employee;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The type Company structure analyzer test.
 */
class CompanyStructureAnalyzerTest {

    /**
     * Throws exception when no ceo found.
     */
    @Test
    void throwsExceptionWhenNoCEOFound() {
        Map<String, Employee> employees = Map.of(
                "1", new Employee("1", "John", "Doe", 50000, "2"),
                "2", new Employee("2", "Jane", "Smith", 60000, "3")
        );

        CompanyStructureAnalyzer analyzer = new CompanyStructureAnalyzer();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> analyzer.analyze(employees));
        assertEquals("Error : No CEO found in data", exception.getMessage());
    }

    /**
     * Throws exception when multiple ceos found.
     */
    @Test
    void throwsExceptionWhenMultipleCEOsFound() {
        Map<String, Employee> employees = Map.of(
                "1", new Employee("1", "John", "Doe", 50000, null),
                "2", new Employee("2", "Jane", "Smith", 60000, null)
        );

        CompanyStructureAnalyzer analyzer = new CompanyStructureAnalyzer();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> analyzer.analyze(employees));
        assertEquals("Error : Multiple CEOs found in data", exception.getMessage());
    }

    /**
     * Identifies employees with salary greater than 20 percent of average of direct reportees.
     */
    @Test
    void identifiesManagersWithSalaryLessThan20PercentOfDirectReporteesAverage() {
        Employee manager = new Employee("1", "John", "Doe", 45000, null);
        Employee reportee1 = new Employee("2", "Jane", "Smith", 40000, "1");
        Employee reportee2 = new Employee("3", "Bob", "Brown", 40000, "1");
        manager.getDirectReportees().addAll(List.of(reportee1, reportee2));

        Map<String, Employee> employees = Map.of(
                "1", manager,
                "2", reportee1,
                "3", reportee2
        );

        CompanyStructureAnalyzer analyzer = new CompanyStructureAnalyzer();
        String result = captureOutput(() -> analyzer.analyze(employees));

        assertLinesMatch(
                List.of("Managers with salary less than 20% of average of direct reportees:",
                        "Employee{id='1', firstName='John', lastName='Doe', salary=45000.0, managerId='null'}: lesser 3000.00",
                        "No managers with salary more than 50% of average of direct reportees.",
                        "No employees with reporting lines exceeding 4 levels."),
                result.lines().toList());
    }

    /**
     * Identifies managers with salary more than 50 percent of average of direct reportees.
     */
    @Test
    void identifiesManagersWithSalaryMoreThan50PercentOfDirectReporteesAverage() {
        Employee manager = new Employee("1", "John", "Doe", 90000, null);
        Employee reportee1 = new Employee("2", "Jane", "Smith", 40000, "1");
        Employee reportee2 = new Employee("3", "Bob", "Brown", 40000, "1");
        manager.getDirectReportees().addAll(List.of(reportee1, reportee2));

        Map<String, Employee> employees = Map.of(
                "1", manager,
                "2", reportee1,
                "3", reportee2
        );

        CompanyStructureAnalyzer analyzer = new CompanyStructureAnalyzer();
        String result = captureOutput(() -> analyzer.analyze(employees));

        assertLinesMatch(
                List.of("No managers with salary less than 20% of average of direct reportees.",
                        "Managers with salary more than 50% of average of direct reportees:",
                        "Employee{id='1', firstName='John', lastName='Doe', salary=90000.0, managerId='null'}: more 30000.00",
                        "No employees with reporting lines exceeding 4 levels."),
                result.lines().toList());
    }

    /**
     * Identifies employees with reporting lines exceeding four levels.
     */
    @Test
    void identifiesEmployeesWithReportingLinesExceedingFourLevels() {
        Employee ceo = new Employee("1", "John", "Doe", 59000, null);
        Employee level1 = new Employee("2", "Jane", "Smith", 40000, "1");
        Employee level2 = new Employee("3", "Bob", "Brown", 28000, "2");
        Employee level3 = new Employee("4", "Alice", "White", 20000, "3");
        Employee level4 = new Employee("5", "Charlie", "Black", 14000, "4");
        Employee level5 = new Employee("6", "Eve", "Green", 10000, "5");

        ceo.getDirectReportees().add(level1);
        level1.getDirectReportees().add(level2);
        level2.getDirectReportees().add(level3);
        level3.getDirectReportees().add(level4);
        level4.getDirectReportees().add(level5);

        Map<String, Employee> employees = Map.of(
                "1", ceo,
                "2", level1,
                "3", level2,
                "4", level3,
                "5", level4,
                "6", level5
        );

        CompanyStructureAnalyzer analyzer = new CompanyStructureAnalyzer();
        String result = captureOutput(() -> analyzer.analyze(employees));

        assertLinesMatch(
                List.of("No managers with salary less than 20% of average of direct reportees.",
                        "No managers with salary more than 50% of average of direct reportees.",
                        "Employees with reporting lines exceeding 4 levels:",
                        "Employee{id='6', firstName='Eve', lastName='Green', salary=10000.0, managerId='5'}: exceeds by 1"),
                result.lines().toList());
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
        runnable.run();
        System.setOut(System.out);
        return out.toString();
    }
}