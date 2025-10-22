package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.OrdenServicio;
import co.edu.umanizales.tallerautomotriz_api.model.Repuesto;
import co.edu.umanizales.tallerautomotriz_api.model.ServicioRealizado;
import co.edu.umanizales.tallerautomotriz_api.repository.OrdenServicioRepository;
import co.edu.umanizales.tallerautomotriz_api.repository.RepuestoRepository;
import co.edu.umanizales.tallerautomotriz_api.repository.ServicioRealizadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrdenServicioService {
    
    private final OrdenServicioRepository ordenServicioRepository;
    private final RepuestoRepository repuestoRepository;
    private final ServicioRealizadoRepository servicioRealizadoRepository;
    
    public OrdenServicio save(OrdenServicio orden) {
        calcularTotales(orden);
        return ordenServicioRepository.save(orden);
    }
    
    public Optional<OrdenServicio> findById(Long id) {
        return ordenServicioRepository.findById(id);
    }
    
    public List<OrdenServicio> findAll() {
        return ordenServicioRepository.findAll();
    }
    
    public void deleteById(Long id) {
        ordenServicioRepository.deleteById(id);
    }
    
    public OrdenServicio update(Long id, OrdenServicio orden) {
        if (!ordenServicioRepository.existsById(id)) {
            throw new RuntimeException("Orden de servicio no encontrada con id: " + id);
        }
        orden.setId(id);
        calcularTotales(orden);
        return ordenServicioRepository.save(orden);
    }
    
    public List<OrdenServicio> findByVehiculoId(Long vehiculoId) {
        return ordenServicioRepository.findByVehiculoId(vehiculoId);
    }
    
    public List<OrdenServicio> findByTecnicoId(Long tecnicoId) {
        return ordenServicioRepository.findByTecnicoId(tecnicoId);
    }
    
    private void calcularTotales(OrdenServicio orden) {
        double totalRepuestos = orden.getRepuestosIds().stream()
                .map(repuestoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToDouble(Repuesto::getPrecio)
                .sum();
        
        double totalServicios = orden.getServiciosIds().stream()
                .map(servicioRealizadoRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToDouble(ServicioRealizado::getPrecio)
                .sum();
        
        orden.setTotalRepuestos(totalRepuestos);
        orden.setTotalServicios(totalServicios);
    }
}
