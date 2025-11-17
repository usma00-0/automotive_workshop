package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un servicio realizado dentro de una orden.
 * Incluye horas invertidas y tarifa por hora para calcular su subtotal.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServicePerformed {
    private String code;
    private String name;
    private String description;
    private double hours;
    private double hourlyRate;

    /**
     * Subtotal del servicio: horas x tarifa por hora.
     */
    public double getTotal() {
        return hours * hourlyRate;
    }
}
