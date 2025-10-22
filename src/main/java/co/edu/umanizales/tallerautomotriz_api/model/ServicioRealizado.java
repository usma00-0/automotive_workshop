package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServicioRealizado {
    private Long id;
    private String nombre;
    private String descripcion;
    private Double precio;
    private Integer duracionHoras;
    private String tipoServicio; // MECANICO, ELECTRICO, GENERAL
}
