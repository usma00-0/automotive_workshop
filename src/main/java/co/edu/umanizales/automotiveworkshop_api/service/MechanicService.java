package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Mechanic;
import co.edu.umanizales.automotiveworkshop_api.model.Address;
import co.edu.umanizales.automotiveworkshop_api.model.MechanicSpecialty;
import co.edu.umanizales.automotiveworkshop_api.model.TechnicianSkill;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de negocio para gestionar mecánicos (Mechanic).
 *
 * Responsabilidades:
 * - Cargar y guardar mecánicos usando CSV y una lista en memoria.
 * - Exponer operaciones CRUD con ciclos simples (for/for-each) sin streams.
 * - Serializar/deserializar listas de habilidades (specializations) como texto separado por '|'.
 */
@Service
public class MechanicService {

    private final List<Mechanic> mechanics;
    private static final String DATA_FILE = "mechanics.csv";
    private final CsvStorage csv;

    /**
     * Constructor por defecto que inicializa la lista y el acceso a CSV.
     */
    public MechanicService() {
        this.mechanics = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    /**
     * Carga el contenido de mechanics.csv. Convierte cada línea en un objeto Mechanic.
     * - Convierte enums desde texto (specialty y specializations).
     * - Separa las habilidades por '|'.
     */
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
            if (parts.length < 15) { continue; }
            Mechanic m = new Mechanic();
            m.setTechnicianId(parts[0]);
            m.setId(parts[1]);
            m.setName(parts[2]);
            m.setEmail(parts[3]);
            m.setPhone(parts[4]);
            Address addr = new Address(parts[5], parts[6], parts[7], parts[8], parts[9]);
            m.setAddress(addr);
            MechanicSpecialty sp = null;
            String spStr = parts[10];
            if (spStr != null && !spStr.isEmpty()) {
                try { sp = MechanicSpecialty.valueOf(spStr.toUpperCase()); } catch (Exception e) { sp = null; }
            }
            m.setSpecialty(sp);
            List<TechnicianSkill> specs = new ArrayList<>();
            String specStr = parts[11];
            if (specStr != null && !specStr.isEmpty()) {
                String[] arr = specStr.split("\\|", -1);
                for (int j = 0; j < arr.length; j++) {
                    String s = arr[j];
                    if (s == null || s.isEmpty()) { continue; }
                    try { specs.add(TechnicianSkill.valueOf(s.toUpperCase())); } catch (Exception e) { /* ignore invalid */ }
                }
            }
            m.setSpecializations(specs);
            try { m.setExperienceYears(Integer.parseInt(parts[12])); } catch (Exception e) { m.setExperienceYears(0); }
            m.setAvailable("true".equalsIgnoreCase(parts[13]));
            try { m.setHourlyRate(Double.parseDouble(parts[14])); } catch (Exception e) { m.setHourlyRate(0); }
            mechanics.add(m);
        }
    }

    /**
     * Serializa la lista de mecánicos a mechanics.csv.
     * - Convierte las habilidades (specializations) a texto unido por '|'.
     */
    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("technicianId,id,name,email,phone,street,city,state,postalCode,country,specialty,specializations,experienceYears,available,hourlyRate");
        for (Mechanic m : mechanics) {
            StringBuilder specsJoined = new StringBuilder();
            List<TechnicianSkill> specs = m.getSpecializations();
            if (specs != null) {
                for (int i = 0; i < specs.size(); i++) {
                    if (i > 0) { specsJoined.append("|"); }
                    TechnicianSkill s = specs.get(i);
                    specsJoined.append(s == null ? "" : s.name());
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(m.getTechnicianId() == null ? "" : m.getTechnicianId()).append(",")
              .append(m.getId() == null ? "" : m.getId()).append(",")
              .append(m.getName() == null ? "" : m.getName()).append(",")
              .append(m.getEmail() == null ? "" : m.getEmail()).append(",")
              .append(m.getPhone() == null ? "" : m.getPhone()).append(",");
            Address a = m.getAddress();
            String street = a == null ? "" : a.street();
            String city = a == null ? "" : a.city();
            String state = a == null ? "" : a.state();
            String postal = a == null ? "" : a.postalCode();
            String country = a == null ? "" : a.country();
            String specName = m.getSpecialty() == null ? "" : m.getSpecialty().name();
            sb.append(street == null ? "" : street).append(",")
              .append(city == null ? "" : city).append(",")
              .append(state == null ? "" : state).append(",")
              .append(postal == null ? "" : postal).append(",")
              .append(country == null ? "" : country).append(",")
              .append(specName).append(",")
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
     * @param mechanic mecánico a agregar (debe traer technicianId no nulo)
     * @return true si se agregó; false si es nulo o ya existía el id
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
     * @return lista de mecánicos en memoria
     */
    public List<Mechanic> listAll() {
        return mechanics;
    }

    /**
     * Busca un mecánico por technicianId.
     */
    /**
     * Busca un mecánico por technicianId.
     * @param id identificador del técnico
     * @return mecánico encontrado o null si no existe
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
    /**
     * Actualiza un mecánico por technicianId (no cambia el ID).
     * @param id technicianId a localizar
     * @param updated datos nuevos
     * @return mecánico actualizado o null si no existe
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
     * @param id identificador del técnico
     * @return true si se eliminó, false en caso contrario
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
