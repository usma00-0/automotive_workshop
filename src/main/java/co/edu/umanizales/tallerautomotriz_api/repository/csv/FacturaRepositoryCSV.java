package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.Factura;
import co.edu.umanizales.tallerautomotriz_api.model.TipoPago;
import co.edu.umanizales.tallerautomotriz_api.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FacturaRepositoryCSV implements FacturaRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "facturas.csv";
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
    public Factura save(Factura entity) {
        ensureFileExists();
        List<Factura> facturas = findAll();
        
        if (entity.getId() == null) {
            Long newId = facturas.stream()
                    .mapToLong(Factura::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            facturas.add(entity);
        } else {
            facturas.removeIf(f -> f.getId().equals(entity.getId()));
            facturas.add(entity);
        }
        
        saveAll(facturas);
        return entity;
    }
    
    @Override
    public Optional<Factura> findById(Long id) {
        return findAll().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Factura> findAll() {
        ensureFileExists();
        List<Factura> facturas = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                facturas.add(parseFactura(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return facturas;
    }
    
    @Override
    public void deleteById(Long id) {
        List<Factura> facturas = findAll();
        facturas.removeIf(f -> f.getId().equals(id));
        saveAll(facturas);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    @Override
    public List<Factura> findByClienteId(Long clienteId) {
        return findAll().stream()
                .filter(f -> f.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }
    
    private void saveAll(List<Factura> facturas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Factura factura : facturas) {
                bw.write(toCSV(factura));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(Factura factura) {
        return String.join(",",
                String.valueOf(factura.getId()),
                String.valueOf(factura.getOrdenServicioId()),
                String.valueOf(factura.getClienteId()),
                factura.getFechaEmision().format(formatter),
                String.valueOf(factura.getSubtotal()),
                String.valueOf(factura.getIva()),
                String.valueOf(factura.getTotal()),
                factura.getTipoPago().name(),
                factura.getNumeroFactura()
        );
    }
    
    private Factura parseFactura(String line) {
        String[] parts = line.split(",", -1);
        
        return Factura.builder()
                .id(Long.valueOf(parts[0]))
                .ordenServicioId(Long.valueOf(parts[1]))
                .clienteId(Long.valueOf(parts[2]))
                .fechaEmision(LocalDateTime.parse(parts[3], formatter))
                .subtotal(Double.valueOf(parts[4]))
                .iva(Double.valueOf(parts[5]))
                .total(Double.valueOf(parts[6]))
                .tipoPago(TipoPago.valueOf(parts[7]))
                .numeroFactura(parts[8])
                .build();
    }
}
