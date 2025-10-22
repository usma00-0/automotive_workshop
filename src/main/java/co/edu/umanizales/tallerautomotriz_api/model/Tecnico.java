package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public abstract class Tecnico extends Empleado {
    private String especialidad;
    private Integer aniosExperiencia;
    
    public Tecnico(Long id, String nombre, String apellido, String documento, 
                   String telefono, String email, Double salario, 
                   String especialidad, Integer aniosExperiencia) {
        super(id, nombre, apellido, documento, telefono, email, salario);
        this.especialidad = especialidad;
        this.aniosExperiencia = aniosExperiencia;
    }
    
    public abstract String getTipoTecnico();
}
