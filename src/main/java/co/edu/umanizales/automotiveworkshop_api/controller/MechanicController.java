package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Mechanic;
import co.edu.umanizales.automotiveworkshop_api.service.MechanicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para Mechanic.
 */
@RestController
@RequestMapping("/api/v1/mechanics")
public class MechanicController {

    private final MechanicService mechanicService;

    @Autowired
    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    /**
     * Lista todos los mecánicos.
     */
    @GetMapping
    public List<Mechanic> listAll() {
        return mechanicService.listAll();
    }

    /**
     * Busca un mecánico por technicianId.
     */
    @GetMapping("/{id}")
    public Mechanic findById(@PathVariable String id) {
        return mechanicService.findById(id);
    }

    /**
     * Agrega un nuevo mecánico.
     */
    @PostMapping
    public String addMechanic(@RequestBody Mechanic mechanic) {
        boolean added = mechanicService.addMechanic(mechanic);
        if (added) {
            return "Mechanic added successfully";
        } else {
            return "Mechanic could not be added (null or id already exists)";
        }
    }

    /**
     * Actualiza un mecánico por technicianId.
     */
    @PutMapping("/{id}")
    public Mechanic updateMechanic(@PathVariable String id, @RequestBody Mechanic mechanic) {
        return mechanicService.updateMechanic(id, mechanic);
    }

    /**
     * Elimina un mecánico por technicianId.
     */
    @DeleteMapping("/{id}")
    public String deleteMechanic(@PathVariable String id) {
        boolean removed = mechanicService.deleteById(id);
        if (removed) {
            return "Mechanic deleted successfully";
        } else {
            return "Mechanic not found";
        }
    }
}
