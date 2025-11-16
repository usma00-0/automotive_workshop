package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

/**
 * Represents a client in the automotive workshop system.
 * Extends the base Person class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends Person {
    private String clientId;
    private boolean active;
    private List<Vehicle> vehicles;
}
