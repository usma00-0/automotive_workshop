package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Representa un mecánico del taller.
 * Es una especialización concreta de {@link Technician} que añade la
 * {@link MechanicSpecialty} principal del técnico.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Mechanic extends Technician {
    private MechanicSpecialty specialty;
}
