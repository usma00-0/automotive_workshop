package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Abstract base class for vehicles in the workshop.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public abstract class Vehicle {
    private String plate;
    private String brand;
    private String model;
    private int year;
    private String color;
    private String ownerId; // reference to Client (aggregation)
    private VehicleCategory category; // record for category metadata
}
