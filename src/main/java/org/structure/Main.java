package org.structure;

import org.structure.entity.Employee;
import org.structure.handler.CompanyStructureAnalyzer;

import java.io.IOException;
import java.util.Map;

/**
 * The type Main.
 */
public class Main {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        // Check if the arguments are a missing file path
        String filePath = args[0];

        if (filePath.trim().isEmpty()) {
            System.out.println("The provided file path is empty. Please provide a valid file path.");
            return;
        }

        // Create an instance of the CompanyStructureAnalyzer class
        CompanyStructureAnalyzer companyStructureAnalyzer = new CompanyStructureAnalyzer();

        // load the employees from the CSV file
        try {
            Map<String, Employee> employees = companyStructureAnalyzer.loadEmployeesAndBuildHierarchy(filePath);
            // Call the analyze method with the provided file path
            companyStructureAnalyzer.analyze(employees);

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.err.println("Error : Invalid Data: " + e.getMessage());
        }
    }
}