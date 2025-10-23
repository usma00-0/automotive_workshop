package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Represents a client in the automotive workshop system.
 * Extends the base Person class.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Client extends Person {
    private String clientId;
    private boolean active;
    private String vehicleInfo;
}
