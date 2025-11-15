package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.VehicleCategory;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para VehicleCategory.
 */
@Service
public class VehicleCategoryService {

    private final List<VehicleCategory> categories;
    private static final String DATA_FILE = "vehicle_categories.csv";
    private final CsvStorage csv;

    public VehicleCategoryService() {
        this.categories = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        categories.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) { continue; }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) { continue; }
            if (trimmed.startsWith("name,")) { continue; }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 2) { continue; }
            VehicleCategory c = new VehicleCategory(parts[0], parts[1]);
            categories.add(c);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("name,description");
        for (VehicleCategory c : categories) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.name() == null ? "" : c.name()).append(",")
              .append(c.description() == null ? "" : c.description());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
    }

    /**
     * Agrega una categoría si su nombre no existe.
     */
    public boolean addCategory(VehicleCategory category) {
        if (category == null || category.name() == null) {
            return false;
        }
        VehicleCategory existing = findByName(category.name());
        if (existing != null) {
            return false;
        }
        categories.add(category);
        saveToCsv();
        return true;
    }

    /**
     * Lista todas las categorías de vehículo.
     */
    public List<VehicleCategory> listAll() {
        return categories;
    }

    /**
     * Busca una categoría por nombre.
     */
    public VehicleCategory findByName(String name) {
        if (name == null) {
            return null;
        }
        for (VehicleCategory c : categories) {
            if (name.equalsIgnoreCase(c.name())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Actualiza una categoría por nombre (reemplaza el objeto).
     */
    public VehicleCategory updateCategory(String name, VehicleCategory updated) {
        if (name == null || updated == null) {
            return null;
        }
        for (int i = 0; i < categories.size(); i++) {
            VehicleCategory c = categories.get(i);
            if (name.equalsIgnoreCase(c.name())) {
                categories.set(i, updated);
                saveToCsv();
                return updated;
            }
        }
        return null;
    }

    /**
     * Elimina una categoría por nombre.
     */
    public boolean deleteByName(String name) {
        if (name == null) {
            return false;
        }
        for (int i = 0; i < categories.size(); i++) {
            VehicleCategory c = categories.get(i);
            if (name.equalsIgnoreCase(c.name())) {
                categories.remove(i);
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
