package co.edu.umanizales.tallerautomotriz_api.controller;

import co.edu.umanizales.tallerautomotriz_api.model.Vehiculo;
import co.edu.umanizales.tallerautomotriz_api.service.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {
    
    private final VehiculoService vehiculoService;
    
    @PostMapping
    public ResponseEntity<Vehiculo> create(@RequestBody Vehiculo vehiculo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.save(vehiculo));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> findById(@PathVariable Long id) {
        return vehiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Vehiculo>> findAll() {
        return ResponseEntity.ok(vehiculoService.findAll());
    }
    
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Vehiculo>> findByClienteId(@PathVariable Long clienteId) {
        return ResponseEntity.ok(vehiculoService.findByClienteId(clienteId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Vehiculo> update(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        return ResponseEntity.ok(vehiculoService.update(id, vehiculo));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vehiculoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
