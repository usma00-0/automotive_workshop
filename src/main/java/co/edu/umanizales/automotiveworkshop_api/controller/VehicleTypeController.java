package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.VehicleType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador simple para exponer los valores del enum VehicleType.
 */
@RestController
@RequestMapping("/api/v1/vehicle-types")
public class VehicleTypeController {

    /**
     * Lista todos los tipos de vehículo.
     */
    @GetMapping
    public List<VehicleType> listAll() {
        List<VehicleType> list = new ArrayList<>();
        for (VehicleType v : VehicleType.values()) {
            list.add(v);
        }
        return list;
    }

    /**
     * Busca un tipo de vehículo por nombre.
     */
    @GetMapping("/{name}")
    public VehicleType findByName(@PathVariable String name) {
        if (name == null) {
            return null;
        }
        for (VehicleType v : VehicleType.values()) {
            if (name.equalsIgnoreCase(v.name())) {
                return v;
            }
        }
        return null;
    }
}
