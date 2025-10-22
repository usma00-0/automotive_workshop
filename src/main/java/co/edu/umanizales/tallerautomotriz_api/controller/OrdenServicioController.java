package co.edu.umanizales.tallerautomotriz_api.controller;

import co.edu.umanizales.tallerautomotriz_api.model.OrdenServicio;
import co.edu.umanizales.tallerautomotriz_api.service.OrdenServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
@RequiredArgsConstructor
public class OrdenServicioController {
    
    private final OrdenServicioService ordenServicioService;
    
    @PostMapping
    public ResponseEntity<OrdenServicio> create(@RequestBody OrdenServicio orden) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ordenServicioService.save(orden));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<OrdenServicio> findById(@PathVariable Long id) {
        return ordenServicioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<OrdenServicio>> findAll() {
        return ResponseEntity.ok(ordenServicioService.findAll());
    }
    
    @GetMapping("/vehiculo/{vehiculoId}")
    public ResponseEntity<List<OrdenServicio>> findByVehiculoId(@PathVariable Long vehiculoId) {
        return ResponseEntity.ok(ordenServicioService.findByVehiculoId(vehiculoId));
    }
    
    @GetMapping("/tecnico/{tecnicoId}")
    public ResponseEntity<List<OrdenServicio>> findByTecnicoId(@PathVariable Long tecnicoId) {
        return ResponseEntity.ok(ordenServicioService.findByTecnicoId(tecnicoId));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<OrdenServicio> update(@PathVariable Long id, @RequestBody OrdenServicio orden) {
        return ResponseEntity.ok(ordenServicioService.update(id, orden));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ordenServicioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
