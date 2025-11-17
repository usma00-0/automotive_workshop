package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.TechnicianSkill;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador simple para exponer los valores del enum TechnicianSkill.
 */
@RestController
@RequestMapping("/api/v1/technician-skills")
public class TechnicianSkillController {

    /**
     * Lista todas las habilidades técnicas.
     */
    @GetMapping
    public List<TechnicianSkill> listAll() {
        List<TechnicianSkill> list = new ArrayList<>();
        for (TechnicianSkill s : TechnicianSkill.values()) {
            list.add(s);
        }
        return list;
    }

    /**
     * Busca una habilidad por su nombre.
     */
    @GetMapping("/{name}")
    public TechnicianSkill findByName(@PathVariable String name) {
        if (name == null) {
            return null;
        }
        for (TechnicianSkill s : TechnicianSkill.values()) {
            if (name.equalsIgnoreCase(s.name())) {
                return s;
            }
        }
        return null;
    }
}
