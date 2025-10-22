package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.Empleado;
import co.edu.umanizales.tallerautomotriz_api.repository.EmpleadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmpleadoService {
    
    private final EmpleadoRepository empleadoRepository;
    
    public Empleado save(Empleado empleado) {
        return empleadoRepository.save(empleado);
    }
    
    public Optional<Empleado> findById(Long id) {
        return empleadoRepository.findById(id);
    }
    
    public List<Empleado> findAll() {
        return empleadoRepository.findAll();
    }
    
    public void deleteById(Long id) {
        empleadoRepository.deleteById(id);
    }
    
    public Empleado update(Long id, Empleado empleado) {
        if (!empleadoRepository.existsById(id)) {
            throw new RuntimeException("Empleado no encontrado con id: " + id);
        }
        empleado.setId(id);
        return empleadoRepository.save(empleado);
    }
}
