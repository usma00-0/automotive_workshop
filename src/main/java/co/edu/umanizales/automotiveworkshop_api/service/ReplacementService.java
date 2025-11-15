package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Replacement;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para Replacement (repuestos).
 */
@Service
public class ReplacementService {

    private final List<Replacement> replacements;
    private static final String DATA_FILE = "replacements.csv";
    private final CsvStorage csv;

    public ReplacementService() {
        this.replacements = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        replacements.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) { continue; }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) { continue; }
            if (trimmed.startsWith("code,")) { continue; }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 5) { continue; }
            Replacement r = new Replacement();
            r.setCode(parts[0]);
            r.setName(parts[1]);
            r.setDescription(parts[2]);
            try { r.setQuantity(Integer.parseInt(parts[3])); } catch (Exception e) { r.setQuantity(0); }
            try { r.setUnitPrice(Double.parseDouble(parts[4])); } catch (Exception e) { r.setUnitPrice(0); }
            replacements.add(r);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("code,name,description,quantity,unitPrice");
        for (Replacement r : replacements) {
            StringBuilder sb = new StringBuilder();
            sb.append(r.getCode() == null ? "" : r.getCode()).append(",")
              .append(r.getName() == null ? "" : r.getName()).append(",")
              .append(r.getDescription() == null ? "" : r.getDescription()).append(",")
              .append(r.getQuantity()).append(",")
              .append(r.getUnitPrice());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
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
        saveToCsv();
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
                saveToCsv();
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
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
