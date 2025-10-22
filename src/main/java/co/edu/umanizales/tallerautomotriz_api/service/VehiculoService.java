package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.Vehiculo;
import co.edu.umanizales.tallerautomotriz_api.repository.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculoService {
    
    private final VehiculoRepository vehiculoRepository;
    
    public Vehiculo save(Vehiculo vehiculo) {
        return vehiculoRepository.save(vehiculo);
    }
    
    public Optional<Vehiculo> findById(Long id) {
        return vehiculoRepository.findById(id);
    }
    
    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }
    
    public void deleteById(Long id) {
        vehiculoRepository.deleteById(id);
    }
    
    public Vehiculo update(Long id, Vehiculo vehiculo) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        vehiculo.setId(id);
        return vehiculoRepository.save(vehiculo);
    }
    
    public List<Vehiculo> findByClienteId(Long clienteId) {
        return vehiculoRepository.findByClienteId(clienteId);
    }
}
