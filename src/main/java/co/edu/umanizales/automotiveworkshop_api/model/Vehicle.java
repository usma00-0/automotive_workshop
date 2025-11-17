package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad que representa un vehículo genérico del taller.
 * Contiene atributos comunes (placa, marca, modelo, color) y relaciones con
 * el propietario ({@link Client}), la categoría ({@link VehicleCategory}) y el tipo ({@link VehicleType}).
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
    private Client owner;
    private VehicleCategory category;
    private VehicleType type;

    /**
     * Método de ejemplo: “encender” el vehículo.
     * @return mensaje simple indicando que el vehículo se encendió
     */
    public String start() {
        return "El " + brand + " con placa " + licensePlate + " se ha encendido.";
    }
    
    /**
     * Devuelve una descripción corta del vehículo.
     * @return cadena con información básica del vehículo
     */
    public String getDetails() {
        return String.format("Vehículo: %s %s (Año: %d), Placa: %s, Color: %s", 
                brand, category != null ? category.name() : "N/A", modelYear, licensePlate, color);
    }
}
