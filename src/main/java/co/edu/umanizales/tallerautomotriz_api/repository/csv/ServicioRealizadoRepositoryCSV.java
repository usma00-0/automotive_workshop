package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.ServicioRealizado;
import co.edu.umanizales.tallerautomotriz_api.repository.ServicioRealizadoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ServicioRealizadoRepositoryCSV implements ServicioRealizadoRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "servicios_realizados.csv";
    
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
    public ServicioRealizado save(ServicioRealizado entity) {
        ensureFileExists();
        List<ServicioRealizado> servicios = findAll();
        
        if (entity.getId() == null) {
            Long newId = servicios.stream()
                    .mapToLong(ServicioRealizado::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            servicios.add(entity);
        } else {
            servicios.removeIf(s -> s.getId().equals(entity.getId()));
            servicios.add(entity);
        }
        
        saveAll(servicios);
        return entity;
    }
    
    @Override
    public Optional<ServicioRealizado> findById(Long id) {
        return findAll().stream()
                .filter(s -> s.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<ServicioRealizado> findAll() {
        ensureFileExists();
        List<ServicioRealizado> servicios = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                servicios.add(parseServicioRealizado(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return servicios;
    }
    
    @Override
    public void deleteById(Long id) {
        List<ServicioRealizado> servicios = findAll();
        servicios.removeIf(s -> s.getId().equals(id));
        saveAll(servicios);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    private void saveAll(List<ServicioRealizado> servicios) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (ServicioRealizado servicio : servicios) {
                bw.write(toCSV(servicio));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(ServicioRealizado servicio) {
        return String.join(",",
                String.valueOf(servicio.getId()),
                servicio.getNombre(),
                servicio.getDescripcion(),
                String.valueOf(servicio.getPrecio()),
                String.valueOf(servicio.getDuracionHoras()),
                servicio.getTipoServicio()
        );
    }
    
    private ServicioRealizado parseServicioRealizado(String line) {
        String[] parts = line.split(",", -1);
        
        return ServicioRealizado.builder()
                .id(Long.valueOf(parts[0]))
                .nombre(parts[1])
                .descripcion(parts[2])
                .precio(Double.valueOf(parts[3]))
                .duracionHoras(Integer.valueOf(parts[4]))
                .tipoServicio(parts[5])
                .build();
    }
}
