package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa un repuesto utilizado en una orden.
 * Incluye cantidad y precio unitario para calcular su subtotal.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Replacement {
    private String code;
    private String name;
    private String description;
    private int quantity;
    private double unitPrice;

    /**
     * Subtotal del repuesto: cantidad x precio unitario.
     */
    public double getTotal() {
        return quantity * unitPrice;
    }
}
