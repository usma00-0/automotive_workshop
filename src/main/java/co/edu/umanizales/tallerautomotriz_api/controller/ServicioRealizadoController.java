package co.edu.umanizales.tallerautomotriz_api.controller;

import co.edu.umanizales.tallerautomotriz_api.model.ServicioRealizado;
import co.edu.umanizales.tallerautomotriz_api.service.ServicioRealizadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioRealizadoController {
    
    private final ServicioRealizadoService servicioRealizadoService;
    
    @PostMapping
    public ResponseEntity<ServicioRealizado> create(@RequestBody ServicioRealizado servicio) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicioRealizadoService.save(servicio));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ServicioRealizado> findById(@PathVariable Long id) {
        return servicioRealizadoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<ServicioRealizado>> findAll() {
        return ResponseEntity.ok(servicioRealizadoService.findAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ServicioRealizado> update(@PathVariable Long id, @RequestBody ServicioRealizado servicio) {
        return ResponseEntity.ok(servicioRealizadoService.update(id, servicio));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        servicioRealizadoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
