package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Employee;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para gestionar Employee.
 */
@Service
public class EmployeeService {

    private final List<Employee> employees;

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public EmployeeService() {
        this.employees = new ArrayList<>();
    }

    /**
     * Agrega un empleado si su employeeId no existe aún.
     */
    public boolean addEmployee(Employee employee) {
        if (employee == null || employee.getEmployeeId() == null) {
            return false;
        }
        Employee existing = findById(employee.getEmployeeId());
        if (existing != null) {
            return false;
        }
        employees.add(employee);
        return true;
    }

    /**
     * Lista todos los empleados.
     */
    public List<Employee> listAll() {
        return employees;
    }

    /**
     * Busca un empleado por employeeId.
     */
    public Employee findById(String id) {
        if (id == null) {
            return null;
        }
        for (Employee e : employees) {
            if (id.equalsIgnoreCase(e.getEmployeeId())) {
                return e;
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un empleado por employeeId (no cambia el ID).
     */
    public Employee updateEmployee(String id, Employee updated) {
        if (id == null || updated == null) {
            return null;
        }
        for (Employee e : employees) {
            if (id.equalsIgnoreCase(e.getEmployeeId())) {
                // Datos de Person
                e.setId(updated.getId());
                e.setName(updated.getName());
                e.setEmail(updated.getEmail());
                e.setPhone(updated.getPhone());
                e.setAddress(updated.getAddress());
                // Específicos de Employee
                e.setPosition(updated.getPosition());
                e.setSalary(updated.getSalary());
                e.setHireDate(updated.getHireDate());
                e.setActive(updated.isActive());
                return e;
            }
        }
        return null;
    }

    /**
     * Elimina un empleado por employeeId.
     */
    public boolean deleteById(String id) {
        if (id == null) {
            return false;
        }
        for (int i = 0; i < employees.size(); i++) {
            Employee e = employees.get(i);
            if (id.equalsIgnoreCase(e.getEmployeeId())) {
                employees.remove(i);
                return true;
            }
        }
        return false;
    }
}
