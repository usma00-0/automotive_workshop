package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Represents an employee in the automotive workshop system.
 * Extends the base Person class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends Person {
    private String employeeId;
    private String position;
    private double salary;
    private LocalDate hireDate;
    private boolean active;
}
