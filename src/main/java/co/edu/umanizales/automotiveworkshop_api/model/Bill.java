package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private String id;
    private String orderId;
    private LocalDateTime issuedAt;
    private double subtotalParts;
    private double subtotalServices;
    private double taxes;
    private double total;
    private PaymentType paymentType;
}
