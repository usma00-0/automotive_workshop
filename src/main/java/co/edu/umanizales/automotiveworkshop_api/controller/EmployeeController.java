package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Employee;
import co.edu.umanizales.automotiveworkshop_api.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para Employee.
 */
@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    /**
     * Inyección del servicio mediante constructor.
     */
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Lista todos los empleados.
     */
    @GetMapping
    public List<Employee> listAll() {
        return employeeService.listAll();
    }

    /**
     * Busca un empleado por employeeId.
     */
    @GetMapping("/{id}")
    public Employee findById(@PathVariable String id) {
        return employeeService.findById(id);
    }

    /**
     * Agrega un nuevo empleado.
     */
    @PostMapping
    public String addEmployee(@RequestBody Employee employee) {
        boolean added = employeeService.addEmployee(employee);
        if (added) {
            return "Employee added successfully";
        } else {
            return "Employee could not be added (null or id already exists)";
        }
    }

    /**
     * Actualiza un empleado identificado por employeeId.
     */
    @PutMapping("/{id}")
    public Employee updateEmployee(@PathVariable String id, @RequestBody Employee employee) {
        return employeeService.updateEmployee(id, employee);
    }

    /**
     * Elimina un empleado por employeeId.
     */
    @DeleteMapping("/{id}")
    public String deleteEmployee(@PathVariable String id) {
        boolean removed = employeeService.deleteById(id);
        if (removed) {
            return "Employee deleted successfully";
        } else {
            return "Employee not found";
        }
    }
}
