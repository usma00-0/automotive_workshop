package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.VehicleCategory;
import co.edu.umanizales.automotiveworkshop_api.service.VehicleCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para VehicleCategory.
 */
@RestController
@RequestMapping("/api/v1/vehicle-categories")
public class VehicleCategoryController {

    private final VehicleCategoryService vehicleCategoryService;

    @Autowired
    public VehicleCategoryController(VehicleCategoryService vehicleCategoryService) {
        this.vehicleCategoryService = vehicleCategoryService;
    }

    /**
     * Lista todas las categorías.
     */
    @GetMapping
    public List<VehicleCategory> listAll() {
        return vehicleCategoryService.listAll();
    }

    /**
     * Busca una categoría por nombre.
     */
    @GetMapping("/{name}")
    public VehicleCategory findByName(@PathVariable String name) {
        return vehicleCategoryService.findByName(name);
    }

    /**
     * Agrega una nueva categoría.
     */
    @PostMapping
    public String addCategory(@RequestBody VehicleCategory category) {
        boolean added = vehicleCategoryService.addCategory(category);
        if (added) {
            return "Vehicle category added successfully";
        } else {
            return "Vehicle category could not be added (null or name already exists)";
        }
    }

    /**
     * Actualiza una categoría por nombre.
     */
    @PutMapping("/{name}")
    public VehicleCategory updateCategory(@PathVariable String name, @RequestBody VehicleCategory category) {
        return vehicleCategoryService.updateCategory(name, category);
    }

    /**
     * Elimina una categoría por nombre.
     */
    @DeleteMapping("/{name}")
    public String deleteCategory(@PathVariable String name) {
        boolean removed = vehicleCategoryService.deleteByName(name);
        if (removed) {
            return "Vehicle category deleted successfully";
        } else {
            return "Vehicle category not found";
        }
    }
}
