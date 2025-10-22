package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.Cliente;
import co.edu.umanizales.tallerautomotriz_api.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ClienteRepositoryCSV implements ClienteRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "clientes.csv";
    
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
    public Cliente save(Cliente entity) {
        ensureFileExists();
        List<Cliente> clientes = findAll();
        
        if (entity.getId() == null) {
            Long newId = clientes.stream()
                    .mapToLong(Cliente::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            clientes.add(entity);
        } else {
            clientes.removeIf(c -> c.getId().equals(entity.getId()));
            clientes.add(entity);
        }
        
        saveAll(clientes);
        return entity;
    }
    
    @Override
    public Optional<Cliente> findById(Long id) {
        return findAll().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Cliente> findAll() {
        ensureFileExists();
        List<Cliente> clientes = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                clientes.add(parseCliente(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return clientes;
    }
    
    @Override
    public void deleteById(Long id) {
        List<Cliente> clientes = findAll();
        clientes.removeIf(c -> c.getId().equals(id));
        saveAll(clientes);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    private void saveAll(List<Cliente> clientes) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Cliente cliente : clientes) {
                bw.write(toCSV(cliente));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(Cliente cliente) {
        String vehiculosStr = cliente.getVehiculosIds().stream()
                .map(String::valueOf)
                .collect(Collectors.joining("|"));
        
        return String.join(",",
                String.valueOf(cliente.getId()),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getDocumento(),
                cliente.getTelefono(),
                cliente.getEmail(),
                cliente.getDireccion(),
                vehiculosStr
        );
    }
    
    private Cliente parseCliente(String line) {
        String[] parts = line.split(",", -1);
        
        List<Long> vehiculosIds = new ArrayList<>();
        if (parts.length > 7 && !parts[7].isEmpty()) {
            vehiculosIds = Arrays.stream(parts[7].split("\\|"))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
        }
        
        return Cliente.builder()
                .id(Long.valueOf(parts[0]))
                .nombre(parts[1])
                .apellido(parts[2])
                .documento(parts[3])
                .telefono(parts[4])
                .email(parts[5])
                .direccion(parts[6])
                .vehiculosIds(vehiculosIds)
                .build();
    }
}
