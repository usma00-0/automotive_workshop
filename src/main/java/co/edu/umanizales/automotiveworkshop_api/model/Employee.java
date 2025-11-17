package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

/**
 * Representa un empleado del taller.
 * Extiende de {@link Person} y agrega información laboral: cargo, salario,
 * fecha de contratación y estado activo.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Employee extends Person {
    private String employeeId;
    private JobPosition position;
    private double salary;
    private LocalDate hireDate;
    private boolean active;
}
