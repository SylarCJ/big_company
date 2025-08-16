package org.structure.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Employee.
 */
@Getter
@Setter
public class Employee {

    /**
     * The Salary.
     */
    double salary;
    private String id;
    private String firstName;
    private String lastName;
    private String managerId;
    private List<Employee> directReportees = new ArrayList<>();

    /**
     * Instantiates a new Employee.
     *
     * @param id        the id
     * @param firstName the first name
     * @param lastName  the last name
     * @param salary    the salary
     * @param managerId the manager id
     */
    public Employee(String id, String firstName, String lastName, double salary, String managerId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.managerId = managerId;
    }

    /**
     * The to string method provides a string representation of the Employee object.
     *
     * @return a string representation of the Employee object
     */
    @Override
    public String toString() {
        return "Employee{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", managerId='" + managerId + '\'' +
                '}';
    }
}
