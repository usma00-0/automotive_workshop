package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Car;
import co.edu.umanizales.automotiveworkshop_api.model.VehicleCategory;
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

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public CarService() {
        this.cars = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
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
            c.setOwnerId(parts[4]);
            c.setCategory(new VehicleCategory(parts[5], parts[6]));
            try { c.setNumberOfDoors(Integer.parseInt(parts[7])); } catch (Exception e) { c.setNumberOfDoors(0); }
            c.setFuelType(parts[8]);
            cars.add(c);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("licensePlate,brand,modelYear,color,ownerId,categoryName,categoryDescription,numberOfDoors,fuelType");
        for (Car c : cars) {
            String catName = c.getCategory() == null ? "" : c.getCategory().name();
            String catDesc = c.getCategory() == null ? "" : c.getCategory().description();
            StringBuilder sb = new StringBuilder();
            sb.append(c.getLicensePlate() == null ? "" : c.getLicensePlate()).append(",")
              .append(c.getBrand() == null ? "" : c.getBrand()).append(",")
              .append(c.getModelYear()).append(",")
              .append(c.getColor() == null ? "" : c.getColor()).append(",")
              .append(c.getOwnerId() == null ? "" : c.getOwnerId()).append(",")
              .append(catName == null ? "" : catName).append(",")
              .append(catDesc == null ? "" : catDesc).append(",")
              .append(c.getNumberOfDoors()).append(",")
              .append(c.getFuelType() == null ? "" : c.getFuelType());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
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
        return cars;
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
                return c;
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
                c.setOwnerId(updated.getOwnerId());
                c.setCategory(updated.getCategory());
                c.setNumberOfDoors(updated.getNumberOfDoors());
                c.setFuelType(updated.getFuelType());
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
