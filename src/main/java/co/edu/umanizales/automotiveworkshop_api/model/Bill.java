package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Representa una factura del taller.
 * Contiene referencia a la orden asociada y los valores económicos
 * (subtotales, impuestos, total) junto con el tipo de pago.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Bill {
    private String id;
    private OrderService order;
    private LocalDateTime issuedAt;
    private double subtotalParts;
    private double subtotalServices;
    private double taxes;
    private double total;
    private PaymentType paymentType;
}
