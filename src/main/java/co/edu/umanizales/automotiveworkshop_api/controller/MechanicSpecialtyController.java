package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.MechanicSpecialty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador simple para exponer los valores del enum MechanicSpecialty.
 */
@RestController
@RequestMapping("/api/v1/mechanic-specialties")
public class MechanicSpecialtyController {

    /**
     * Lista todas las especialidades de mecánico.
     */
    @GetMapping
    public List<MechanicSpecialty> listAll() {
        List<MechanicSpecialty> list = new ArrayList<>();
        for (MechanicSpecialty s : MechanicSpecialty.values()) {
            list.add(s);
        }
        return list;
    }

    /**
     * Busca una especialidad por su nombre.
     */
    @GetMapping("/{name}")
    public MechanicSpecialty findByName(@PathVariable String name) {
        if (name == null) {
            return null;
        }
        for (MechanicSpecialty s : MechanicSpecialty.values()) {
            if (name.equalsIgnoreCase(s.name())) {
                return s;
            }
        }
        return null;
    }
}
