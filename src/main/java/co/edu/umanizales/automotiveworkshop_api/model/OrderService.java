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

    public double getTotal() {
        return getTotalParts() + getTotalServices();
    }
}
