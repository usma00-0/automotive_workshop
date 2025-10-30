package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents a technician in the automotive workshop system.
 * Extends the base Person class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Technician extends Person {
    private String technicianId;
    private List<String> specializations;
    private int experienceYears;
    private boolean available;
    private double hourlyRate;
}
