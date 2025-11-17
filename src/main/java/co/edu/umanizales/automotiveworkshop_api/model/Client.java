package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Representa a un cliente del taller automotriz.
 * Extiende de {@link Person} para heredar datos comunes (id, nombre, contacto, dirección).
 * Atributos propios:
 * - clientId: identificador lógico del cliente usado en CSV y relaciones.
 * - active: indica si el cliente está activo.
 * - vehicles: lista de vehículos asociados al cliente.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends Person {
    private String clientId;
    private boolean active;
    private List<Vehicle> vehicles;
}
