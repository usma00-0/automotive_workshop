package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Mechanic extends Technician {
    private String specialty; // e.g., engine, transmission
}
