package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base class for vehicles in the workshop.
 * Represents a generic vehicle with common attributes and behaviors.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private String licensePlate;
    private String brand;
    private int modelYear;
    private String color;
    private String ownerId;
    private VehicleCategory category;

    /**
     * Starts the vehicle's engine.
     * @return A message indicating the vehicle has started.
     */
    public String start() {
        return "The " + brand + " with license plate " + licensePlate + " has started.";
    }
    
    /**
     * Gets the vehicle's details.
     * @return A string containing the vehicle's information.
     */
    public String getDetails() {
        return String.format("Vehicle: %s %s (Year: %d), License: %s, Color: %s", 
                brand, category != null ? category.name() : "N/A", modelYear, licensePlate, color);
    }
}
