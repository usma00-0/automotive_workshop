package co.edu.umanizales.automotiveworkshop_api.model;

/**
 * Record que representa la categoría de un vehículo (nombre y descripción).
 * Se usa para clasificar el uso del vehículo (p. ej., Particular, Comercial).
 */
public record VehicleCategory(String name, String description) { }
