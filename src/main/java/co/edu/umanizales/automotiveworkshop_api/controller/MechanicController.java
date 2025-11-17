package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Mechanic;
import co.edu.umanizales.automotiveworkshop_api.service.MechanicService;
import co.edu.umanizales.automotiveworkshop_api.service.EmployeeService;
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
    private final EmployeeService employeeService;

    @Autowired
    public MechanicController(MechanicService mechanicService, EmployeeService employeeService) {
        this.mechanicService = mechanicService;
        this.employeeService = employeeService;
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
        if (mechanic != null && mechanic.getId() != null) {
            List<co.edu.umanizales.automotiveworkshop_api.model.Employee> es = employeeService.listAll();
            for (int i = 0; i < es.size(); i++) {
                co.edu.umanizales.automotiveworkshop_api.model.Employee e = es.get(i);
                if (e != null && e.getId() != null && mechanic.getId().equalsIgnoreCase(e.getId())) {
                    return "Mechanic could not be added (person id already used by an employee)";
                }
            }
        }
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
        if (mechanic != null && mechanic.getId() != null) {
            List<co.edu.umanizales.automotiveworkshop_api.model.Employee> es = employeeService.listAll();
            for (int i = 0; i < es.size(); i++) {
                co.edu.umanizales.automotiveworkshop_api.model.Employee e = es.get(i);
                if (e != null && e.getId() != null && mechanic.getId().equalsIgnoreCase(e.getId())) {
                    return null;
                }
            }
        }
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
