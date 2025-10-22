package co.edu.umanizales.tallerautomotriz_api.controller;

import co.edu.umanizales.tallerautomotriz_api.model.Repuesto;
import co.edu.umanizales.tallerautomotriz_api.service.RepuestoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repuestos")
@RequiredArgsConstructor
public class RepuestoController {
    
    private final RepuestoService repuestoService;
    
    @PostMapping
    public ResponseEntity<Repuesto> create(@RequestBody Repuesto repuesto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repuestoService.save(repuesto));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Repuesto> findById(@PathVariable Long id) {
        return repuestoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping
    public ResponseEntity<List<Repuesto>> findAll() {
        return ResponseEntity.ok(repuestoService.findAll());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Repuesto> update(@PathVariable Long id, @RequestBody Repuesto repuesto) {
        return ResponseEntity.ok(repuestoService.update(id, repuesto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        repuestoService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
