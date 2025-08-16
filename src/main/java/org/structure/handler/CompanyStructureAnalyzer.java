package org.structure.handler;

import org.structure.entity.Employee;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * The type Company structure analyzer.
 */
public class CompanyStructureAnalyzer {
    /**
     * Load employees and build hierarchy map.
     *
     * @param filePath the file path
     * @return the map
     */
    public Map<String, Employee> loadEmployeesAndBuildHierarchy(String filePath) throws IOException {
        Map<String, Employee> employeeMap = new HashMap<>();

        int lineNumber = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Skipping header line
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] record = line.trim().split(",", -1);
                System.out.println(Arrays.toString(record));

                if (record.length < 4) {
                    throw new IllegalArgumentException("Error : Malformed CSV at line " + lineNumber);
                }

                String id = record[0].trim();
                String firstName = record[1].trim();
                String lastName = record[2].trim();
                double salary = Double.parseDouble(record[3].trim());
                String managerId = record[4].trim().isEmpty() ? null : record[4].trim();

                employeeMap.put(id, new Employee(id, firstName, lastName, salary, managerId));
            }

            // Build the hierarchy
            for (Employee emp : employeeMap.values()) {
                if (emp.getManagerId() != null) {
                    Employee manager = employeeMap.get(emp.getManagerId());
                    if (manager != null) {
                        manager.getDirectReportees().add(emp);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Error : File not found: " + filePath);
        } catch (IOException e) {
            throw new IOException("Error : Reading the file: " + filePath);
        } catch (NumberFormatException e) {
            System.err.println("Error : Invalid salary format at line: " + lineNumber);
        }

        return employeeMap;
    }

    /**
     * Analyze.
     *
     * @param employees the employees
     */
    public void analyze(Map<String, Employee> employees) {
        List<String> mgrSalaryLess = new ArrayList<>();
        List<String> mgrSalaryMore = new ArrayList<>();
        List<String> empLongReportingLines = new ArrayList<>();

        var ceos = employees.values().stream()
                .filter(e -> e.getManagerId() == null)
                .toList();

        if (ceos.isEmpty()) {
            throw new IllegalStateException("Error : No CEO found in data");
        }
        if (ceos.size() > 1) {
            throw new IllegalStateException("Error : Multiple CEOs found in data");
        }

        Employee ceo = ceos.get(0);

        for (Employee emp : employees.values()) {
            if (!emp.getDirectReportees().isEmpty()) {
                // Calculate the average salary of direct reportees
                double avgSal = emp.getDirectReportees().stream().mapToDouble(Employee::getSalary).average().orElse(0);
                double lowerBoundSal = avgSal * 1.2;
                double upperBoundSal = avgSal * 1.5;

                if (emp.getSalary() < lowerBoundSal) {
                    mgrSalaryLess.add(String.format("%s: lesser %.2f", emp, lowerBoundSal - emp.getSalary()));
                } else if (emp.getSalary() > upperBoundSal) {
                    mgrSalaryMore.add(String.format("%s: more %.2f", emp, emp.getSalary() - upperBoundSal));
                }
            }
        }

        checkDepth(ceo, 0, empLongReportingLines);

        // Print results for managers with salary less than 120% of average of direct reportees
        System.out.println(mgrSalaryLess.isEmpty()
                ? "No managers with salary less than 120% of average of direct reportees."
                : "Managers with salary less than 120% of average of direct reportees:");
        mgrSalaryLess.forEach(System.out::println);

        // Print results for managers with salary more than 150% of average of direct reportees
        System.out.println(mgrSalaryMore.isEmpty()
                ? "No managers with salary more than 150% of average of direct reportees."
                : "Managers with salary more than 150% of average of direct reportees:");
        mgrSalaryMore.forEach(System.out::println);

        // Print results for employees with reporting lines exceeding 4 levels
        System.out.println(empLongReportingLines.isEmpty()
                ? "No employees with reporting lines exceeding 4 levels."
                : "Employees with reporting lines exceeding 4 levels:");
        empLongReportingLines.forEach(System.out::println);
    }

    private void checkDepth(Employee employee, int depth, List<String> empLongReportingLines) {
        for (Employee directReportee : employee.getDirectReportees()) {

            int nextDepth = depth + 1;

            if (nextDepth > 4) {
                empLongReportingLines.add(String.format("%s: exceeds by %d", directReportee, nextDepth - 4));
            }

            checkDepth(directReportee, nextDepth, empLongReportingLines);
        }
    }
}
