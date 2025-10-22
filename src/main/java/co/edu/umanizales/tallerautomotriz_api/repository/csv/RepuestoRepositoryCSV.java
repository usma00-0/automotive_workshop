package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.Repuesto;
import co.edu.umanizales.tallerautomotriz_api.repository.RepuestoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class RepuestoRepositoryCSV implements RepuestoRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "repuestos.csv";
    
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
    public Repuesto save(Repuesto entity) {
        ensureFileExists();
        List<Repuesto> repuestos = findAll();
        
        if (entity.getId() == null) {
            Long newId = repuestos.stream()
                    .mapToLong(Repuesto::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            repuestos.add(entity);
        } else {
            repuestos.removeIf(r -> r.getId().equals(entity.getId()));
            repuestos.add(entity);
        }
        
        saveAll(repuestos);
        return entity;
    }
    
    @Override
    public Optional<Repuesto> findById(Long id) {
        return findAll().stream()
                .filter(r -> r.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Repuesto> findAll() {
        ensureFileExists();
        List<Repuesto> repuestos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                repuestos.add(parseRepuesto(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return repuestos;
    }
    
    @Override
    public void deleteById(Long id) {
        List<Repuesto> repuestos = findAll();
        repuestos.removeIf(r -> r.getId().equals(id));
        saveAll(repuestos);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    private void saveAll(List<Repuesto> repuestos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Repuesto repuesto : repuestos) {
                bw.write(toCSV(repuesto));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(Repuesto repuesto) {
        return String.join(",",
                String.valueOf(repuesto.getId()),
                repuesto.getCodigo(),
                repuesto.getNombre(),
                repuesto.getDescripcion(),
                String.valueOf(repuesto.getPrecio()),
                String.valueOf(repuesto.getStock()),
                repuesto.getMarca()
        );
    }
    
    private Repuesto parseRepuesto(String line) {
        String[] parts = line.split(",", -1);
        
        return Repuesto.builder()
                .id(Long.valueOf(parts[0]))
                .codigo(parts[1])
                .nombre(parts[2])
                .descripcion(parts[3])
                .precio(Double.valueOf(parts[4]))
                .stock(Integer.valueOf(parts[5]))
                .marca(parts[6])
                .build();
    }
}
