package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Car;
import co.edu.umanizales.automotiveworkshop_api.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para gestionar Car usando una lista en memoria.
 * Ofrece endpoints CRUD básicos y un endpoint de búsqueda por placa.
 */
@RestController
@RequestMapping("/api/v1/cars")
public class CarController {

    private final CarService carService;

    /**
     * Inyección del servicio mediante constructor.
     */
    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    /**
     * Lista todos los carros almacenados.
     * GET /api/v1/cars
     */
    @GetMapping
    public List<Car> getAllCars() {
        return carService.listAll();
    }

    /**
     * Busca un carro por su placa.
     * GET /api/v1/cars/find/{plate}
     */
    @GetMapping("/find/{plate}")
    public Car findByPlate(@PathVariable String plate) {
        return carService.findCarByPlate(plate);
    }

    /**
     * Agrega un nuevo carro si su placa no existe todavía.
     * POST /api/v1/cars
     */
    @PostMapping
    public String addCar(@RequestBody Car car) {
        boolean added = carService.addCar(car);
        if (added) {
            return "Car added successfully";
        } else {
            return "Car could not be added (null or plate already exists)";
        }
    }

    /**
     * Actualiza los datos de un carro identificado por placa.
     * PUT /api/v1/cars/{plate}
     */
    @PutMapping("/{plate}")
    public Car updateCar(@PathVariable String plate, @RequestBody Car car) {
        return carService.updateCar(plate, car);
    }

    /**
     * Elimina un carro por su placa.
     * DELETE /api/v1/cars/{plate}
     */
    @DeleteMapping("/{plate}")
    public String deleteCar(@PathVariable String plate) {
        boolean removed = carService.deleteByPlate(plate);
        if (removed) {
            return "Car deleted successfully";
        } else {
            return "Car not found";
        }
    }
}
