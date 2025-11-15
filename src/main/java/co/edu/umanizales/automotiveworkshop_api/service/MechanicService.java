package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Mechanic;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para Mechanic.
 */
@Service
public class MechanicService {

    private final List<Mechanic> mechanics;
    private static final String DATA_FILE = "mechanics.csv";
    private final CsvStorage csv;

    public MechanicService() {
        this.mechanics = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        mechanics.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) { continue; }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) { continue; }
            if (trimmed.startsWith("technicianId,")) { continue; }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 11) { continue; }
            Mechanic m = new Mechanic();
            m.setTechnicianId(parts[0]);
            m.setId(parts[1]);
            m.setName(parts[2]);
            m.setEmail(parts[3]);
            m.setPhone(parts[4]);
            m.setAddress(parts[5]);
            m.setSpecialty(parts[6]);
            List<String> specs = new ArrayList<>();
            String specStr = parts[7];
            if (specStr != null && !specStr.isEmpty()) {
                String[] arr = specStr.split("\\|", -1);
                for (int j = 0; j < arr.length; j++) {
                    String s = arr[j];
                    if (s != null && !s.isEmpty()) {
                        specs.add(s);
                    }
                }
            }
            m.setSpecializations(specs);
            try { m.setExperienceYears(Integer.parseInt(parts[8])); } catch (Exception e) { m.setExperienceYears(0); }
            m.setAvailable("true".equalsIgnoreCase(parts[9]));
            try { m.setHourlyRate(Double.parseDouble(parts[10])); } catch (Exception e) { m.setHourlyRate(0); }
            mechanics.add(m);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("technicianId,id,name,email,phone,address,specialty,specializations,experienceYears,available,hourlyRate");
        for (Mechanic m : mechanics) {
            StringBuilder specsJoined = new StringBuilder();
            List<String> specs = m.getSpecializations();
            if (specs != null) {
                for (int i = 0; i < specs.size(); i++) {
                    if (i > 0) { specsJoined.append("|"); }
                    String s = specs.get(i);
                    specsJoined.append(s == null ? "" : s);
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(m.getTechnicianId() == null ? "" : m.getTechnicianId()).append(",")
              .append(m.getId() == null ? "" : m.getId()).append(",")
              .append(m.getName() == null ? "" : m.getName()).append(",")
              .append(m.getEmail() == null ? "" : m.getEmail()).append(",")
              .append(m.getPhone() == null ? "" : m.getPhone()).append(",")
              .append(m.getAddress() == null ? "" : m.getAddress()).append(",")
              .append(m.getSpecialty() == null ? "" : m.getSpecialty()).append(",")
              .append(specsJoined.toString()).append(",")
              .append(m.getExperienceYears()).append(",")
              .append(m.isAvailable()).append(",")
              .append(m.getHourlyRate());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
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
        saveToCsv();
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
                saveToCsv();
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
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
