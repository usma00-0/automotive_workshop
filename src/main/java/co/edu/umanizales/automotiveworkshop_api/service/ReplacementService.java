package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Replacement;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de negocio para gestionar repuestos (Replacement).
 *
 * Responsabilidades:
 * - Cargar/guardar los repuestos desde/hacia CSV usando una lista en memoria.
 * - Exponer operaciones CRUD con ciclos simples.
 */
@Service
public class ReplacementService {

    private final List<Replacement> replacements;
    private static final String DATA_FILE = "replacements.csv";
    private final CsvStorage csv;

    /**
     * Constructor por defecto que inicializa la lista y el acceso a CSV.
     */
    public ReplacementService() {
        this.replacements = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    /**
     * Carga el contenido de replacements.csv y lo transforma a objetos Replacement.
     */
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

    /**
     * Serializa la lista de repuestos a replacements.csv.
     */
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
     * @param replacement repuesto a agregar
     * @return true si se agregó, false si es nulo o ya existe el código
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
     * @return lista en memoria de repuestos
     */
    public List<Replacement> listAll() {
        return replacements;
    }

    /**
     * Busca un repuesto por código.
     * @param code código del repuesto
     * @return repuesto encontrado o null
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
     * @param code código a localizar
     * @param updated datos nuevos
     * @return repuesto actualizado o null si no existe
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
     * @param code código a eliminar
     * @return true si se eliminó; false en caso contrario
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
