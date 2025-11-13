package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Vehicle;
import co.edu.umanizales.automotiveworkshop_api.service.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para gestionar Vehicle usando una lista en memoria.
 */
@RestController
@RequestMapping("/api/v1/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    /**
     * Inyección del servicio mediante constructor.
     */
    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    /**
     * Lista todos los vehículos.
     * GET /api/v1/vehicles
     */
    @GetMapping
    public List<Vehicle> listAll() {
        return vehicleService.listAll();
    }

    /**
     * Busca un vehículo por su placa.
     * GET /api/v1/vehicles/find/{plate}
     */
    @GetMapping("/find/{plate}")
    public Vehicle findByPlate(@PathVariable String plate) {
        return vehicleService.findByPlate(plate);
    }

    /**
     * Agrega un nuevo vehículo.
     * POST /api/v1/vehicles
     */
    @PostMapping
    public String addVehicle(@RequestBody Vehicle vehicle) {
        boolean added = vehicleService.addVehicle(vehicle);
        if (added) {
            return "Vehicle added successfully";
        } else {
            return "Vehicle could not be added (null or plate already exists)";
        }
    }

    /**
     * Actualiza un vehículo por placa.
     * PUT /api/v1/vehicles/{plate}
     */
    @PutMapping("/{plate}")
    public Vehicle updateVehicle(@PathVariable String plate, @RequestBody Vehicle vehicle) {
        return vehicleService.updateVehicle(plate, vehicle);
    }

    /**
     * Elimina un vehículo por placa.
     * DELETE /api/v1/vehicles/{plate}
     */
    @DeleteMapping("/{plate}")
    public String deleteVehicle(@PathVariable String plate) {
        boolean removed = vehicleService.deleteByPlate(plate);
        if (removed) {
            return "Vehicle deleted successfully";
        } else {
            return "Vehicle not found";
        }
    }
}
