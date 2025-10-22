package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Factura {
    private Long id;
    private Long ordenServicioId;
    private Long clienteId;
    private LocalDateTime fechaEmision;
    private Double subtotal;
    private Double iva;
    private Double total;
    private TipoPago tipoPago;
    private String numeroFactura;
}
