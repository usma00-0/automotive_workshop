package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrdenServicio {
    private Long id;
    private Long vehiculoId;
    private Long tecnicoId;
    private LocalDateTime fechaIngreso;
    private LocalDateTime fechaSalida;
    private String estado; // PENDIENTE, EN_PROCESO, COMPLETADA, CANCELADA
    private String observaciones;
    @Builder.Default
    private List<Long> repuestosIds = new ArrayList<>();
    @Builder.Default
    private List<Long> serviciosIds = new ArrayList<>();
    private Double totalRepuestos;
    private Double totalServicios;
}
