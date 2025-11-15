package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Employee;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para gestionar Employee.
 */
@Service
public class EmployeeService {

    private final List<Employee> employees;
    private static final String DATA_FILE = "employees.csv";
    private final CsvStorage csv;

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public EmployeeService() {
        this.employees = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        employees.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) {
                continue;
            }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.startsWith("employeeId,")) {
                continue;
            }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 10) {
                continue;
            }
            Employee e = new Employee();
            e.setEmployeeId(parts[0]);
            e.setId(parts[1]);
            e.setName(parts[2]);
            e.setEmail(parts[3]);
            e.setPhone(parts[4]);
            e.setAddress(parts[5]);
            e.setPosition(parts[6]);
            try { e.setSalary(Double.parseDouble(parts[7])); } catch (Exception ex) { e.setSalary(0); }
            try {
                if (parts[8] != null && !parts[8].isEmpty()) {
                    e.setHireDate(LocalDate.parse(parts[8]));
                }
            } catch (Exception ex) { e.setHireDate(null); }
            e.setActive("true".equalsIgnoreCase(parts[9]));
            employees.add(e);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("employeeId,id,name,email,phone,address,position,salary,hireDate,active");
        for (Employee e : employees) {
            StringBuilder sb = new StringBuilder();
            sb.append(e.getEmployeeId() == null ? "" : e.getEmployeeId()).append(",")
              .append(e.getId() == null ? "" : e.getId()).append(",")
              .append(e.getName() == null ? "" : e.getName()).append(",")
              .append(e.getEmail() == null ? "" : e.getEmail()).append(",")
              .append(e.getPhone() == null ? "" : e.getPhone()).append(",")
              .append(e.getAddress() == null ? "" : e.getAddress()).append(",")
              .append(e.getPosition() == null ? "" : e.getPosition()).append(",")
              .append(e.getSalary()).append(",")
              .append(e.getHireDate() == null ? "" : e.getHireDate().toString()).append(",")
              .append(e.isActive());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
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
        saveToCsv();
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
                saveToCsv();
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
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
