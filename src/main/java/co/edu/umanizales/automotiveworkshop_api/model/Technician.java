package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Clase abstracta que representa un técnico del taller.
 * Extiende de {@link Person} y define campos comunes a cualquier técnico:
 * - technicianId: identificador lógico del técnico.
 * - specializations: habilidades (enum) que domina.
 * - experienceYears: años de experiencia.
 * - available: disponibilidad actual.
 * - hourlyRate: tarifa por hora.
 * Las implementaciones concretas (p. ej., {@link Mechanic}) agregan información específica.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public abstract class Technician extends Person {
    private String technicianId;
    private List<TechnicianSkill> specializations;
    private int experienceYears;
    private boolean available;
    private double hourlyRate;
}
