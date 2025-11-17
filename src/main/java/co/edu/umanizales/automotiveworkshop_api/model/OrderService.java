package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Representa una orden de servicio del taller.
 * Contiene las referencias al cliente, vehículo y mecánico que atiende,
 * además de los repuestos y servicios realizados. Expone métodos de ayuda
 * para calcular totales de repuestos, servicios y el total general.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderService {
    private String id;
    private Client client; // aggregation to Client
    private Vehicle vehicle; // aggregation to Vehicle
    private Mechanic technician; // use concrete type to enable JSON deserialization
    private LocalDateTime createdAt;
    private ServiceStatus status; // e.g., PENDING, IN_PROGRESS, COMPLETED
    private List<Replacement> parts;
    private List<ServicePerformed> services;
    private String notes;

    /**
     * Calcula el total de repuestos (cantidad x precio unitario).
     * @return suma de subtotales de cada repuesto
     */
    public double getTotalParts() {
        if (parts == null) {
            return 0d;
        }
        double total = 0d;
        for (Replacement r : parts) {
            if (r != null) {
                total += r.getTotal();
            }
        }
        return total;
    }

    /**
     * Calcula el total de servicios (horas x tarifa).
     * @return suma de subtotales de cada servicio
     */
    public double getTotalServices() {
        if (services == null) {
            return 0d;
        }
        double total = 0d;
        for (ServicePerformed s : services) {
            if (s != null) {
                total += s.getTotal();
            }
        }
        return total;
    }

    /**
     * Total general de la orden: repuestos + servicios.
     */
    public double getTotal() {
        return getTotalParts() + getTotalServices();
    }
}
