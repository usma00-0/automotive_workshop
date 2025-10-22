package co.edu.umanizales.tallerautomotriz_api.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Mecanico extends Tecnico {
    
    public Mecanico(Long id, String nombre, String apellido, String documento, 
                    String telefono, String email, Double salario, 
                    String especialidad, Integer aniosExperiencia) {
        super(id, nombre, apellido, documento, telefono, email, salario, especialidad, aniosExperiencia);
    }
    
    @Override
    public String getTipoTecnico() {
        return "MECANICO";
    }
}
