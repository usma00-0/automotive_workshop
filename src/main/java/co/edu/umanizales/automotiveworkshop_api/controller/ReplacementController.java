package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Replacement;
import co.edu.umanizales.automotiveworkshop_api.service.ReplacementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para Replacement.
 */
@RestController
@RequestMapping("/api/v1/replacements")
public class ReplacementController {

    private final ReplacementService replacementService;

    @Autowired
    public ReplacementController(ReplacementService replacementService) {
        this.replacementService = replacementService;
    }

    /**
     * Lista todos los repuestos.
     */
    @GetMapping
    public List<Replacement> listAll() {
        return replacementService.listAll();
    }

    /**
     * Busca un repuesto por código.
     */
    @GetMapping("/{code}")
    public Replacement findByCode(@PathVariable String code) {
        return replacementService.findByCode(code);
    }

    /**
     * Agrega un nuevo repuesto.
     */
    @PostMapping
    public String addReplacement(@RequestBody Replacement replacement) {
        boolean added = replacementService.addReplacement(replacement);
        if (added) {
            return "Replacement added successfully";
        } else {
            return "Replacement could not be added (null or code already exists)";
        }
    }

    /**
     * Actualiza un repuesto por código.
     */
    @PutMapping("/{code}")
    public Replacement updateReplacement(@PathVariable String code, @RequestBody Replacement replacement) {
        return replacementService.updateReplacement(code, replacement);
    }

    /**
     * Elimina un repuesto por código.
     */
    @DeleteMapping("/{code}")
    public String deleteReplacement(@PathVariable String code) {
        boolean removed = replacementService.deleteByCode(code);
        if (removed) {
            return "Replacement deleted successfully";
        } else {
            return "Replacement not found";
        }
    }
}
