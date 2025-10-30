package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderService {
    private String id;
    private String clientId; // reference to Client
    private String vehiclePlate; // association to Vehicle by plate
    private String technicianId; // reference to Technician
    private LocalDateTime createdAt;
    private String status; // e.g., OPEN, IN_PROGRESS, CLOSED
    private List<Replacement> parts;
    private List<ServicePerformed> services;
    private String notes;

    public double getTotalParts() {
        return parts == null ? 0d : parts.stream().mapToDouble(Replacement::getTotal).sum();
    }

    public double getTotalServices() {
        return services == null ? 0d : services.stream().mapToDouble(ServicePerformed::getTotal).sum();
    }

    public double getTotal() {
        return getTotalParts() + getTotalServices();
    }
}
