package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Vehicle;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para gestionar Vehicle.
 */
@Service
public class VehicleService {

    private final List<Vehicle> vehicles;

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public VehicleService() {
        this.vehicles = new ArrayList<>();
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
        return true;
    }

    /**
     * Lista todos los vehículos.
     */
    public List<Vehicle> listAll() {
        return vehicles;
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
                return v;
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
                v.setOwnerId(updated.getOwnerId());
                v.setCategory(updated.getCategory());
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
                return true;
            }
        }
        return false;
    }
}
