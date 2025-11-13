package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Replacement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para Replacement (repuestos).
 */
@Service
public class ReplacementService {

    private final List<Replacement> replacements;

    public ReplacementService() {
        this.replacements = new ArrayList<>();
    }

    /**
     * Agrega un repuesto si su código no existe.
     */
    public boolean addReplacement(Replacement replacement) {
        if (replacement == null || replacement.getCode() == null) {
            return false;
        }
        Replacement existing = findByCode(replacement.getCode());
        if (existing != null) {
            return false;
        }
        replacements.add(replacement);
        return true;
    }

    /**
     * Lista todos los repuestos.
     */
    public List<Replacement> listAll() {
        return replacements;
    }

    /**
     * Busca un repuesto por código.
     */
    public Replacement findByCode(String code) {
        if (code == null) {
            return null;
        }
        for (Replacement r : replacements) {
            if (code.equalsIgnoreCase(r.getCode())) {
                return r;
            }
        }
        return null;
    }

    /**
     * Actualiza un repuesto por código (no cambia el código).
     */
    public Replacement updateReplacement(String code, Replacement updated) {
        if (code == null || updated == null) {
            return null;
        }
        for (Replacement r : replacements) {
            if (code.equalsIgnoreCase(r.getCode())) {
                r.setName(updated.getName());
                r.setDescription(updated.getDescription());
                r.setQuantity(updated.getQuantity());
                r.setUnitPrice(updated.getUnitPrice());
                return r;
            }
        }
        return null;
    }

    /**
     * Elimina un repuesto por código.
     */
    public boolean deleteByCode(String code) {
        if (code == null) {
            return false;
        }
        for (int i = 0; i < replacements.size(); i++) {
            Replacement r = replacements.get(i);
            if (code.equalsIgnoreCase(r.getCode())) {
                replacements.remove(i);
                return true;
            }
        }
        return false;
    }
}
