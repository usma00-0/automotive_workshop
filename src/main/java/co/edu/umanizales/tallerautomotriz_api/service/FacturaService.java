package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.Factura;
import co.edu.umanizales.tallerautomotriz_api.model.OrdenServicio;
import co.edu.umanizales.tallerautomotriz_api.repository.FacturaRepository;
import co.edu.umanizales.tallerautomotriz_api.repository.OrdenServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacturaService {
    
    private final FacturaRepository facturaRepository;
    private final OrdenServicioRepository ordenServicioRepository;
    
    public Factura save(Factura factura) {
        calcularTotales(factura);
        generarNumeroFactura(factura);
        return facturaRepository.save(factura);
    }
    
    public Optional<Factura> findById(Long id) {
        return facturaRepository.findById(id);
    }
    
    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }
    
    public void deleteById(Long id) {
        facturaRepository.deleteById(id);
    }
    
    public Factura update(Long id, Factura factura) {
        if (!facturaRepository.existsById(id)) {
            throw new RuntimeException("Factura no encontrada con id: " + id);
        }
        factura.setId(id);
        calcularTotales(factura);
        return facturaRepository.save(factura);
    }
    
    public List<Factura> findByClienteId(Long clienteId) {
        return facturaRepository.findByClienteId(clienteId);
    }
    
    private void calcularTotales(Factura factura) {
        Optional<OrdenServicio> ordenOpt = ordenServicioRepository.findById(factura.getOrdenServicioId());
        if (ordenOpt.isPresent()) {
            OrdenServicio orden = ordenOpt.get();
            double subtotal = orden.getTotalRepuestos() + orden.getTotalServicios();
            double iva = subtotal * 0.19;
            double total = subtotal + iva;
            
            factura.setSubtotal(subtotal);
            factura.setIva(iva);
            factura.setTotal(total);
        }
    }
    
    private void generarNumeroFactura(Factura factura) {
        if (factura.getNumeroFactura() == null || factura.getNumeroFactura().isEmpty()) {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            factura.setNumeroFactura("FAC-" + timestamp);
        }
    }
}
