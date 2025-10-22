package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.Vehiculo;
import co.edu.umanizales.tallerautomotriz_api.repository.VehiculoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class VehiculoRepositoryCSV implements VehiculoRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "vehiculos.csv";
    
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
    public Vehiculo save(Vehiculo entity) {
        ensureFileExists();
        List<Vehiculo> vehiculos = findAll();
        
        if (entity.getId() == null) {
            Long newId = vehiculos.stream()
                    .mapToLong(Vehiculo::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            vehiculos.add(entity);
        } else {
            vehiculos.removeIf(v -> v.getId().equals(entity.getId()));
            vehiculos.add(entity);
        }
        
        saveAll(vehiculos);
        return entity;
    }
    
    @Override
    public Optional<Vehiculo> findById(Long id) {
        return findAll().stream()
                .filter(v -> v.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Vehiculo> findAll() {
        ensureFileExists();
        List<Vehiculo> vehiculos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                vehiculos.add(parseVehiculo(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return vehiculos;
    }
    
    @Override
    public void deleteById(Long id) {
        List<Vehiculo> vehiculos = findAll();
        vehiculos.removeIf(v -> v.getId().equals(id));
        saveAll(vehiculos);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    @Override
    public List<Vehiculo> findByClienteId(Long clienteId) {
        return findAll().stream()
                .filter(v -> v.getClienteId() != null && v.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }
    
    private void saveAll(List<Vehiculo> vehiculos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Vehiculo vehiculo : vehiculos) {
                bw.write(toCSV(vehiculo));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(Vehiculo vehiculo) {
        return String.join(",",
                String.valueOf(vehiculo.getId()),
                vehiculo.getPlaca(),
                vehiculo.getMarca(),
                vehiculo.getModelo(),
                String.valueOf(vehiculo.getAnio()),
                vehiculo.getColor(),
                vehiculo.getTipoVehiculo(),
                vehiculo.getClienteId() != null ? String.valueOf(vehiculo.getClienteId()) : "",
                vehiculo.getCategoriaVehiculoNombre() != null ? vehiculo.getCategoriaVehiculoNombre() : ""
        );
    }
    
    private Vehiculo parseVehiculo(String line) {
        String[] parts = line.split(",", -1);
        
        return Vehiculo.builder()
                .id(Long.valueOf(parts[0]))
                .placa(parts[1])
                .marca(parts[2])
                .modelo(parts[3])
                .anio(Integer.valueOf(parts[4]))
                .color(parts[5])
                .tipoVehiculo(parts[6])
                .clienteId(parts[7].isEmpty() ? null : Long.valueOf(parts[7]))
                .categoriaVehiculoNombre(parts.length > 8 ? parts[8] : "")
                .build();
    }
}
