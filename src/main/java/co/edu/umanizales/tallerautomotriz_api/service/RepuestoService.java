package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.Repuesto;
import co.edu.umanizales.tallerautomotriz_api.repository.RepuestoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepuestoService {
    
    private final RepuestoRepository repuestoRepository;
    
    public Repuesto save(Repuesto repuesto) {
        return repuestoRepository.save(repuesto);
    }
    
    public Optional<Repuesto> findById(Long id) {
        return repuestoRepository.findById(id);
    }
    
    public List<Repuesto> findAll() {
        return repuestoRepository.findAll();
    }
    
    public void deleteById(Long id) {
        repuestoRepository.deleteById(id);
    }
    
    public Repuesto update(Long id, Repuesto repuesto) {
        if (!repuestoRepository.existsById(id)) {
            throw new RuntimeException("Repuesto no encontrado con id: " + id);
        }
        repuesto.setId(id);
        return repuestoRepository.save(repuesto);
    }
}
