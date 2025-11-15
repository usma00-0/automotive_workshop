package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.ServicePerformed;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para ServicePerformed.
 */
@Service
public class ServicePerformedService {

    private final List<ServicePerformed> servicesPerformed;
    private static final String DATA_FILE = "services_performed.csv";
    private final CsvStorage csv;

    public ServicePerformedService() {
        this.servicesPerformed = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        servicesPerformed.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) { continue; }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) { continue; }
            if (trimmed.startsWith("code,")) { continue; }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 5) { continue; }
            ServicePerformed s = new ServicePerformed();
            s.setCode(parts[0]);
            s.setName(parts[1]);
            s.setDescription(parts[2]);
            try { s.setHours(Double.parseDouble(parts[3])); } catch (Exception e) { s.setHours(0); }
            try { s.setHourlyRate(Double.parseDouble(parts[4])); } catch (Exception e) { s.setHourlyRate(0); }
            servicesPerformed.add(s);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("code,name,description,hours,hourlyRate");
        for (ServicePerformed s : servicesPerformed) {
            StringBuilder sb = new StringBuilder();
            sb.append(s.getCode() == null ? "" : s.getCode()).append(",")
              .append(s.getName() == null ? "" : s.getName()).append(",")
              .append(s.getDescription() == null ? "" : s.getDescription()).append(",")
              .append(s.getHours()).append(",")
              .append(s.getHourlyRate());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
    }

    /**
     * Agrega un servicio realizado si su código no existe.
     */
    public boolean addService(ServicePerformed service) {
        if (service == null || service.getCode() == null) {
            return false;
        }
        ServicePerformed existing = findByCode(service.getCode());
        if (existing != null) {
            return false;
        }
        servicesPerformed.add(service);
        saveToCsv();
        return true;
    }

    /**
     * Lista todos los servicios realizados.
     */
    public List<ServicePerformed> listAll() {
        return servicesPerformed;
    }

    /**
     * Busca un servicio realizado por código.
     */
    public ServicePerformed findByCode(String code) {
        if (code == null) {
            return null;
        }
        for (ServicePerformed s : servicesPerformed) {
            if (code.equalsIgnoreCase(s.getCode())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Actualiza un servicio realizado por código (no cambia el código).
     */
    public ServicePerformed updateService(String code, ServicePerformed updated) {
        if (code == null || updated == null) {
            return null;
        }
        for (ServicePerformed s : servicesPerformed) {
            if (code.equalsIgnoreCase(s.getCode())) {
                s.setName(updated.getName());
                s.setDescription(updated.getDescription());
                s.setHours(updated.getHours());
                s.setHourlyRate(updated.getHourlyRate());
                saveToCsv();
                return s;
            }
        }
        return null;
    }

    /**
     * Elimina un servicio realizado por código.
     */
    public boolean deleteByCode(String code) {
        if (code == null) {
            return false;
        }
        for (int i = 0; i < servicesPerformed.size(); i++) {
            ServicePerformed s = servicesPerformed.get(i);
            if (code.equalsIgnoreCase(s.getCode())) {
                servicesPerformed.remove(i);
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
