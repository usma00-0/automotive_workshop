package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Mechanic;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para Mechanic.
 */
@Service
public class MechanicService {

    private final List<Mechanic> mechanics;

    public MechanicService() {
        this.mechanics = new ArrayList<>();
    }

    /**
     * Agrega un mecánico si su technicianId no existe.
     */
    public boolean addMechanic(Mechanic mechanic) {
        if (mechanic == null || mechanic.getTechnicianId() == null) {
            return false;
        }
        Mechanic existing = findById(mechanic.getTechnicianId());
        if (existing != null) {
            return false;
        }
        mechanics.add(mechanic);
        return true;
    }

    /**
     * Lista todos los mecánicos.
     */
    public List<Mechanic> listAll() {
        return mechanics;
    }

    /**
     * Busca un mecánico por technicianId.
     */
    public Mechanic findById(String id) {
        if (id == null) {
            return null;
        }
        for (Mechanic m : mechanics) {
            if (id.equalsIgnoreCase(m.getTechnicianId())) {
                return m;
            }
        }
        return null;
    }

    /**
     * Actualiza un mecánico por technicianId (no cambia el ID).
     */
    public Mechanic updateMechanic(String id, Mechanic updated) {
        if (id == null || updated == null) {
            return null;
        }
        for (Mechanic m : mechanics) {
            if (id.equalsIgnoreCase(m.getTechnicianId())) {
                // Datos Person
                m.setId(updated.getId());
                m.setName(updated.getName());
                m.setEmail(updated.getEmail());
                m.setPhone(updated.getPhone());
                m.setAddress(updated.getAddress());
                // Datos Technician
                m.setSpecializations(updated.getSpecializations());
                m.setExperienceYears(updated.getExperienceYears());
                m.setAvailable(updated.isAvailable());
                m.setHourlyRate(updated.getHourlyRate());
                // Propios de Mechanic
                m.setSpecialty(updated.getSpecialty());
                return m;
            }
        }
        return null;
    }

    /**
     * Elimina un mecánico por technicianId.
     */
    public boolean deleteById(String id) {
        if (id == null) {
            return false;
        }
        for (int i = 0; i < mechanics.size(); i++) {
            Mechanic m = mechanics.get(i);
            if (id.equalsIgnoreCase(m.getTechnicianId())) {
                mechanics.remove(i);
                return true;
            }
        }
        return false;
    }
}
