package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Vehicle;
import co.edu.umanizales.automotiveworkshop_api.model.VehicleCategory;
import co.edu.umanizales.automotiveworkshop_api.model.VehicleType;
import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para gestionar Vehicle.
 */
@Service
public class VehicleService {

    private final List<Vehicle> vehicles;
    private static final String DATA_FILE = "vehicles.csv";
    private final CsvStorage csv;
    private final ClientService clientService;

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public VehicleService(ClientService clientService) {
        this.vehicles = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
        this.clientService = clientService;
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        vehicles.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) {
                continue;
            }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.startsWith("licensePlate,")) {
                continue;
            }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 7) {
                continue;
            }
            Vehicle v = new Vehicle();
            v.setLicensePlate(parts[0]);
            v.setBrand(parts[1]);
            try { v.setModelYear(Integer.parseInt(parts[2])); } catch (Exception e) { v.setModelYear(0); }
            v.setColor(parts[3]);
            Client owner = new Client();
            owner.setClientId(parts[4]);
            v.setOwner(owner);
            v.setCategory(new VehicleCategory(parts[5], parts[6]));
            if (parts.length > 7) {
                String typeStr = parts[7];
                if (typeStr != null && !typeStr.isEmpty()) {
                    try {
                        v.setType(VehicleType.valueOf(typeStr.toUpperCase()));
                    } catch (Exception ex) {
                        v.setType(null);
                    }
                }
            }
            vehicles.add(v);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("licensePlate,brand,modelYear,color,ownerClientId,categoryName,categoryDescription,type");
        for (Vehicle v : vehicles) {
            String catName = v.getCategory() == null ? "" : v.getCategory().name();
            String catDesc = v.getCategory() == null ? "" : v.getCategory().description();
            String ownerClientId = "";
            if (v.getOwner() != null) {
                ownerClientId = v.getOwner().getClientId() == null ? "" : v.getOwner().getClientId();
            }
            String typeName = v.getType() == null ? "" : v.getType().name();
            StringBuilder sb = new StringBuilder();
            sb.append(v.getLicensePlate() == null ? "" : v.getLicensePlate()).append(",")
              .append(v.getBrand() == null ? "" : v.getBrand()).append(",")
              .append(v.getModelYear()).append(",")
              .append(v.getColor() == null ? "" : v.getColor()).append(",")
              .append(ownerClientId).append(",")
              .append(catName == null ? "" : catName).append(",")
              .append(catDesc == null ? "" : catDesc).append(",")
              .append(typeName);
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
    }

    private Vehicle hydrate(Vehicle v) {
        if (v == null) { return null; }
        if (v.getOwner() != null && v.getOwner().getClientId() != null) {
            Client full = clientService.findById(v.getOwner().getClientId());
            if (full != null) { v.setOwner(full); }
        }
        return v;
    }

    /**
     * Agrega un vehículo si su placa no existe aún.
     * @param vehicle vehículo a agregar
     * @return true si se agregó, false en caso contrario
     */
    public boolean addVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getLicensePlate() == null) {
            return false;
        }
        Vehicle existing = findByPlate(vehicle.getLicensePlate());
        if (existing != null) {
            return false;
        }
        vehicles.add(vehicle);
        saveToCsv();
        return true;
    }

    /**
     * Lista todos los vehículos.
     */
    public List<Vehicle> listAll() {
        List<Vehicle> result = new ArrayList<>();
        for (Vehicle v : vehicles) {
            result.add(hydrate(v));
        }
        return result;
    }

    /**
     * Busca un vehículo por su placa recorriendo la lista.
     */
    public Vehicle findByPlate(String plate) {
        if (plate == null) {
            return null;
        }
        for (Vehicle v : vehicles) {
            if (plate.equalsIgnoreCase(v.getLicensePlate())) {
                return hydrate(v);
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un vehículo identificado por su placa.
     */
    public Vehicle updateVehicle(String plate, Vehicle updated) {
        if (plate == null || updated == null) {
            return null;
        }
        for (Vehicle v : vehicles) {
            if (plate.equalsIgnoreCase(v.getLicensePlate())) {
                v.setBrand(updated.getBrand());
                v.setModelYear(updated.getModelYear());
                v.setColor(updated.getColor());
                v.setOwner(updated.getOwner());
                v.setCategory(updated.getCategory());
                v.setType(updated.getType());
                saveToCsv();
                return v;
            }
        }
        return null;
    }

    /**
     * Elimina un vehículo por su placa.
     */
    public boolean deleteByPlate(String plate) {
        if (plate == null) {
            return false;
        }
        for (int i = 0; i < vehicles.size(); i++) {
            Vehicle v = vehicles.get(i);
            if (plate.equalsIgnoreCase(v.getLicensePlate())) {
                vehicles.remove(i);
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
