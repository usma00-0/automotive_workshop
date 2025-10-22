package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.ServicioRealizado;
import co.edu.umanizales.tallerautomotriz_api.repository.ServicioRealizadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ServicioRealizadoService {
    
    private final ServicioRealizadoRepository servicioRealizadoRepository;
    
    public ServicioRealizado save(ServicioRealizado servicio) {
        return servicioRealizadoRepository.save(servicio);
    }
    
    public Optional<ServicioRealizado> findById(Long id) {
        return servicioRealizadoRepository.findById(id);
    }
    
    public List<ServicioRealizado> findAll() {
        return servicioRealizadoRepository.findAll();
    }
    
    public void deleteById(Long id) {
        servicioRealizadoRepository.deleteById(id);
    }
    
    public ServicioRealizado update(Long id, ServicioRealizado servicio) {
        if (!servicioRealizadoRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado con id: " + id);
        }
        servicio.setId(id);
        return servicioRealizadoRepository.save(servicio);
    }
}
