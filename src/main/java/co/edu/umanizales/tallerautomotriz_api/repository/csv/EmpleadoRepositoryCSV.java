package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.Empleado;
import co.edu.umanizales.tallerautomotriz_api.repository.EmpleadoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class EmpleadoRepositoryCSV implements EmpleadoRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "empleados.csv";
    
    private String getFilePath() {
        return storagePath + File.separator + fileName;
    }
    
    private void ensureFileExists() {
        try {
            Files.createDirectories(Paths.get(storagePath));
            File file = new File(getFilePath());
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error creating CSV file", e);
        }
    }
    
    @Override
    public Empleado save(Empleado entity) {
        ensureFileExists();
        List<Empleado> empleados = findAll();
        
        if (entity.getId() == null) {
            Long newId = empleados.stream()
                    .mapToLong(Empleado::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            empleados.add(entity);
        } else {
            empleados.removeIf(e -> e.getId().equals(entity.getId()));
            empleados.add(entity);
        }
        
        saveAll(empleados);
        return entity;
    }
    
    @Override
    public Optional<Empleado> findById(Long id) {
        return findAll().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Empleado> findAll() {
        ensureFileExists();
        List<Empleado> empleados = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                empleados.add(parseEmpleado(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return empleados;
    }
    
    @Override
    public void deleteById(Long id) {
        List<Empleado> empleados = findAll();
        empleados.removeIf(e -> e.getId().equals(id));
        saveAll(empleados);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    private void saveAll(List<Empleado> empleados) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Empleado empleado : empleados) {
                bw.write(toCSV(empleado));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(Empleado empleado) {
        return String.join(",",
                String.valueOf(empleado.getId()),
                empleado.getNombre(),
                empleado.getApellido(),
                empleado.getDocumento(),
                empleado.getTelefono(),
                empleado.getEmail(),
                String.valueOf(empleado.getSalario())
        );
    }
    
    private Empleado parseEmpleado(String line) {
        String[] parts = line.split(",", -1);
        
        return new Empleado(
                Long.valueOf(parts[0]),
                parts[1],
                parts[2],
                parts[3],
                parts[4],
                parts[5],
                Double.valueOf(parts[6])
        );
    }
}
