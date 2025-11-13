package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Car;
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

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public CarService() {
        this.cars = new ArrayList<>();
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
                return true;
            }
        }
        return false;
    }
}
