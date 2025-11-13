package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.ServicePerformed;
import co.edu.umanizales.automotiveworkshop_api.service.ServicePerformedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para ServicePerformed.
 */
@RestController
@RequestMapping("/api/v1/services-performed")
public class ServicePerformedController {

    private final ServicePerformedService servicePerformedService;

    @Autowired
    public ServicePerformedController(ServicePerformedService servicePerformedService) {
        this.servicePerformedService = servicePerformedService;
    }

    /**
     * Lista todos los servicios realizados.
     */
    @GetMapping
    public List<ServicePerformed> listAll() {
        return servicePerformedService.listAll();
    }

    /**
     * Busca un servicio realizado por código.
     */
    @GetMapping("/{code}")
    public ServicePerformed findByCode(@PathVariable String code) {
        return servicePerformedService.findByCode(code);
    }

    /**
     * Agrega un nuevo servicio realizado.
     */
    @PostMapping
    public String addService(@RequestBody ServicePerformed service) {
        boolean added = servicePerformedService.addService(service);
        if (added) {
            return "Service performed added successfully";
        } else {
            return "Service performed could not be added (null or code already exists)";
        }
    }

    /**
     * Actualiza un servicio realizado por código.
     */
    @PutMapping("/{code}")
    public ServicePerformed updateService(@PathVariable String code, @RequestBody ServicePerformed service) {
        return servicePerformedService.updateService(code, service);
    }

    /**
     * Elimina un servicio realizado por código.
     */
    @DeleteMapping("/{code}")
    public String deleteService(@PathVariable String code) {
        boolean removed = servicePerformedService.deleteByCode(code);
        if (removed) {
            return "Service performed deleted successfully";
        } else {
            return "Service performed not found";
        }
    }
}
