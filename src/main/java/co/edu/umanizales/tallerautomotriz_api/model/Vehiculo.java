package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo {
    private Long id;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private String color;
    private String tipoVehiculo; // AUTO, MOTO, CAMIONETA
    private Long clienteId;
    private String categoriaVehiculoNombre;
}
