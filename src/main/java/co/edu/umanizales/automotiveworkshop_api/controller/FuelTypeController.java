package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.FuelType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/fuel-types")
public class FuelTypeController {

    @GetMapping
    public List<FuelType> listAll() {
        List<FuelType> list = new ArrayList<>();
        for (FuelType f : FuelType.values()) {
            list.add(f);
        }
        return list;
    }

    @GetMapping("/{name}")
    public FuelType findByName(@PathVariable String name) {
        if (name == null) {
            return null;
        }
        for (FuelType f : FuelType.values()) {
            if (name.equalsIgnoreCase(f.name())) {
                return f;
            }
        }
        return null;
    }
}
