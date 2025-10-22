package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.OrdenServicio;
import co.edu.umanizales.tallerautomotriz_api.repository.OrdenServicioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrdenServicioRepositoryCSV implements OrdenServicioRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "ordenes_servicio.csv";
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    
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
    public OrdenServicio save(OrdenServicio entity) {
        ensureFileExists();
        List<OrdenServicio> ordenes = findAll();
        
        if (entity.getId() == null) {
            Long newId = ordenes.stream()
                    .mapToLong(OrdenServicio::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            ordenes.add(entity);
        } else {
            ordenes.removeIf(o -> o.getId().equals(entity.getId()));
            ordenes.add(entity);
        }
        
        saveAll(ordenes);
        return entity;
    }
    
    @Override
    public Optional<OrdenServicio> findById(Long id) {
        return findAll().stream()
                .filter(o -> o.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<OrdenServicio> findAll() {
        ensureFileExists();
        List<OrdenServicio> ordenes = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                ordenes.add(parseOrdenServicio(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return ordenes;
    }
    
    @Override
    public void deleteById(Long id) {
        List<OrdenServicio> ordenes = findAll();
        ordenes.removeIf(o -> o.getId().equals(id));
        saveAll(ordenes);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    @Override
    public List<OrdenServicio> findByVehiculoId(Long vehiculoId) {
        return findAll().stream()
                .filter(o -> o.getVehiculoId().equals(vehiculoId))
                .collect(Collectors.toList());
    }
    
    @Override
    public List<OrdenServicio> findByTecnicoId(Long tecnicoId) {
        return findAll().stream()
                .filter(o -> o.getTecnicoId().equals(tecnicoId))
                .collect(Collectors.toList());
    }
    
    private void saveAll(List<OrdenServicio> ordenes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (OrdenServicio orden : ordenes) {
                bw.write(toCSV(orden));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(OrdenServicio orden) {
        String repuestosStr = orden.getRepuestosIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        
        String serviciosStr = orden.getServiciosIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        
        return String.join(",",
                String.valueOf(orden.getId()),
                String.valueOf(orden.getVehiculoId()),
                String.valueOf(orden.getTecnicoId()),
                orden.getFechaIngreso().format(formatter),
                orden.getFechaSalida() != null ? orden.getFechaSalida().format(formatter) : "",
                orden.getEstado(),
                orden.getObservaciones() != null ? orden.getObservaciones() : "",
                repuestosStr,
                serviciosStr,
                String.valueOf(orden.getTotalRepuestos()),
                String.valueOf(orden.getTotalServicios())
        );
    }
    
    private OrdenServicio parseOrdenServicio(String line) {
        String[] parts = line.split(",", -1);
        
        List<Long> repuestosIds = new ArrayList<>();
        if (!parts[7].isEmpty()) {
            repuestosIds = Arrays.stream(parts[7].split("\\|"))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
        
        List<Long> serviciosIds = new ArrayList<>();
        if (!parts[8].isEmpty()) {
            serviciosIds = Arrays.stream(parts[8].split("\\|"))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
        
        return OrdenServicio.builder()
                .id(Long.valueOf(parts[0]))
                .vehiculoId(Long.valueOf(parts[1]))
                .tecnicoId(Long.valueOf(parts[2]))
                .fechaIngreso(LocalDateTime.parse(parts[3], formatter))
                .fechaSalida(parts[4].isEmpty() ? null : LocalDateTime.parse(parts[4], formatter))
                .estado(parts[5])
                .observaciones(parts[6])
                .repuestosIds(repuestosIds)
                .serviciosIds(serviciosIds)
                .totalRepuestos(Double.valueOf(parts[9]))
                .totalServicios(Double.valueOf(parts[10]))
                .build();
    }
}
