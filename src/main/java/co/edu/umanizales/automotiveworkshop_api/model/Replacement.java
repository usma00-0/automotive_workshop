package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public double getTotal() {
        return quantity * unitPrice;
    }
}
