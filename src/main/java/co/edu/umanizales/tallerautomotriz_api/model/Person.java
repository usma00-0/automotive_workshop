package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class representing a person in the system.
 * Follows the inheritance requirement where Client, Employee, and Technician will extend this class.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String address;
}
