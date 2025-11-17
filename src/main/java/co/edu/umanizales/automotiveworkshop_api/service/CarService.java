package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Car;
import co.edu.umanizales.automotiveworkshop_api.model.VehicleCategory;
import co.edu.umanizales.automotiveworkshop_api.model.VehicleType;
import co.edu.umanizales.automotiveworkshop_api.model.FuelType;
import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para gestionar Car.
 * No usa persistencia ni repositorios, solo una lista en memoria.
 */
@Service
public class CarService {

    private final List<Car> cars;
    private static final String DATA_FILE = "cars.csv";
    private final CsvStorage csv;
    private final ClientService clientService;

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public CarService(ClientService clientService) {
        this.cars = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
        this.clientService = clientService;
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        cars.clear();
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
            if (parts.length < 9) {
                continue;
            }
            Car c = new Car();
            c.setLicensePlate(parts[0]);
            c.setBrand(parts[1]);
            try { c.setModelYear(Integer.parseInt(parts[2])); } catch (Exception e) { c.setModelYear(0); }
            c.setColor(parts[3]);
            Client owner = new Client();
            owner.setClientId(parts[4]);
            c.setOwner(owner);
            c.setCategory(new VehicleCategory(parts[5], parts[6]));
            try { c.setNumberOfDoors(Integer.parseInt(parts[7])); } catch (Exception e) { c.setNumberOfDoors(0); }
            String fuelStr = parts[8];
            if (fuelStr != null && !fuelStr.isEmpty()) {
                try { c.setFuelType(FuelType.valueOf(fuelStr.toUpperCase())); } catch (Exception e) { c.setFuelType(null); }
            }
            if (parts.length > 9) {
                String typeStr = parts[9];
                if (typeStr != null && !typeStr.isEmpty()) {
                    try { c.setType(VehicleType.valueOf(typeStr.toUpperCase())); } catch (Exception e) { c.setType(null); }
                }
            }
            cars.add(c);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("licensePlate,brand,modelYear,color,ownerClientId,categoryName,categoryDescription,numberOfDoors,fuelType,type");
        for (Car c : cars) {
            String catName = c.getCategory() == null ? "" : c.getCategory().name();
            String catDesc = c.getCategory() == null ? "" : c.getCategory().description();
            String ownerClientId = "";
            if (c.getOwner() != null) {
                ownerClientId = c.getOwner().getClientId() == null ? "" : c.getOwner().getClientId();
            }
            String fuel = c.getFuelType() == null ? "" : c.getFuelType().name();
            String type = c.getType() == null ? "" : c.getType().name();
            StringBuilder sb = new StringBuilder();
            sb.append(c.getLicensePlate() == null ? "" : c.getLicensePlate()).append(",")
              .append(c.getBrand() == null ? "" : c.getBrand()).append(",")
              .append(c.getModelYear()).append(",")
              .append(c.getColor() == null ? "" : c.getColor()).append(",")
              .append(ownerClientId).append(",")
              .append(catName == null ? "" : catName).append(",")
              .append(catDesc == null ? "" : catDesc).append(",")
              .append(c.getNumberOfDoors()).append(",")
              .append(fuel).append(",")
              .append(type);
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
    }

    private Car hydrate(Car c) {
        if (c == null) { return null; }
        if (c.getOwner() != null && c.getOwner().getClientId() != null) {
            Client full = clientService.findById(c.getOwner().getClientId());
            if (full != null) { c.setOwner(full); }
        }
        return c;
    }

    /**
     * Agrega un carro si su placa no existe aún.
     * @param car carro a agregar
     * @return true si se agregó, false si ya existía una placa igual o los datos son inválidos
     */
    public boolean addCar(Car car) {
        if (car == null || car.getLicensePlate() == null) {
            return false;
        }
        Car existing = findCarByPlate(car.getLicensePlate());
        if (existing != null) {
            return false;
        }
        cars.add(car);
        saveToCsv();
        return true;
    }

    /**
     * Lista todos los carros almacenados.
     * @return lista de carros
     */
    public List<Car> listAll() {
        List<Car> result = new ArrayList<>();
        for (Car c : cars) {
            result.add(hydrate(c));
        }
        return result;
    }

    /**
     * Busca un carro por su placa recorriendo la lista con for-each.
     * @param plate placa a buscar
     * @return el carro encontrado o null si no existe
     */
    public Car findCarByPlate(String plate) {
        if (plate == null) {
            return null;
        }
        for (Car c : cars) {
            if (plate.equalsIgnoreCase(c.getLicensePlate())) {
                return hydrate(c);
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un carro encontrado por placa.
     * No cambia la placa; solo actualiza los demás atributos.
     * @param plate placa a localizar
     * @param updated datos nuevos
     * @return el carro actualizado o null si no se encontró
     */
    public Car updateCar(String plate, Car updated) {
        if (plate == null || updated == null) {
            return null;
        }
        for (Car c : cars) {
            if (plate.equalsIgnoreCase(c.getLicensePlate())) {
                c.setBrand(updated.getBrand());
                c.setModelYear(updated.getModelYear());
                c.setColor(updated.getColor());
                c.setOwner(updated.getOwner());
                c.setCategory(updated.getCategory());
                c.setNumberOfDoors(updated.getNumberOfDoors());
                c.setFuelType(updated.getFuelType());
                c.setType(updated.getType());
                saveToCsv();
                return c;
            }
        }
        return null;
    }

    /**
     * Elimina un carro por su placa recorriendo la lista.
     * @param plate placa a eliminar
     * @return true si se eliminó, false si no existía
     */
    public boolean deleteByPlate(String plate) {
        if (plate == null) {
            return false;
        }
        for (int i = 0; i < cars.size(); i++) {
            Car c = cars.get(i);
            if (plate.equalsIgnoreCase(c.getLicensePlate())) {
                cars.remove(i);
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
