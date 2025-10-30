package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    public double getTotal() {
        return hours * hourlyRate;
    }
}
