package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a car in the workshop domain.
 * Extends the base Vehicle class and adds car-specific attributes.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Car extends Vehicle {
    private int numberOfDoors;
    private FuelType fuelType;

    /**
     * No-args constructor required by frameworks and tools, and for simple instantiation.
     */
    public Car() {
        super();
    }

    /**
     * Full-args constructor including base Vehicle attributes and Car-specific ones.
     *
     * @param licensePlate Vehicle license plate
     * @param brand Vehicle brand/manufacturer
     * @param modelYear Vehicle model year
     * @param color Vehicle color
     * @param owner Owner
     * @param category Vehicle category (e.g., private, commercial)
     * @param type Vehicle type (e.g., SEDAN, SUV)
     * @param numberOfDoors Number of doors the car has
     * @param fuelType Fuel type the car uses
     */
    public Car(String licensePlate,
               String brand,
               int modelYear,
               String color,
               Client owner,
               VehicleCategory category,
               VehicleType type,
               int numberOfDoors,
               FuelType fuelType) {
        super(licensePlate, brand, modelYear, color, owner, category, type);
        this.numberOfDoors = numberOfDoors;
        this.fuelType = fuelType;
    }

    /**
     * Car-specific behavior example: honk the horn.
     *
     * @return a friendly honk message
     */
    public String honk() {
        return "Beep beep!";
    }
}
