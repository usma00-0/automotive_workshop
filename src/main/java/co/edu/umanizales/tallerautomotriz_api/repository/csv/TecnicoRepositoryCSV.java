package co.edu.umanizales.tallerautomotriz_api.repository.csv;

import co.edu.umanizales.tallerautomotriz_api.model.Electrico;
import co.edu.umanizales.tallerautomotriz_api.model.Mecanico;
import co.edu.umanizales.tallerautomotriz_api.model.Tecnico;
import co.edu.umanizales.tallerautomotriz_api.repository.TecnicoRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class TecnicoRepositoryCSV implements TecnicoRepository {
    
    @Value("${csv.storage.path:data}")
    private String storagePath;
    
    private final String fileName = "tecnicos.csv";
    
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
    public Tecnico save(Tecnico entity) {
        ensureFileExists();
        List<Tecnico> tecnicos = findAll();
        
        if (entity.getId() == null) {
            Long newId = tecnicos.stream()
                    .mapToLong(Tecnico::getId)
                    .max()
                    .orElse(0L) + 1;
            entity.setId(newId);
            tecnicos.add(entity);
        } else {
            tecnicos.removeIf(t -> t.getId().equals(entity.getId()));
            tecnicos.add(entity);
        }
        
        saveAll(tecnicos);
        return entity;
    }
    
    @Override
    public Optional<Tecnico> findById(Long id) {
        return findAll().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst();
    }
    
    @Override
    public List<Tecnico> findAll() {
        ensureFileExists();
        List<Tecnico> tecnicos = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(getFilePath()))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                tecnicos.add(parseTecnico(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file", e);
        }
        
        return tecnicos;
    }
    
    @Override
    public void deleteById(Long id) {
        List<Tecnico> tecnicos = findAll();
        tecnicos.removeIf(t -> t.getId().equals(id));
        saveAll(tecnicos);
    }
    
    @Override
    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }
    
    private void saveAll(List<Tecnico> tecnicos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getFilePath()))) {
            for (Tecnico tecnico : tecnicos) {
                bw.write(toCSV(tecnico));
                bw.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Error writing CSV file", e);
        }
    }
    
    private String toCSV(Tecnico tecnico) {
        return String.join(",",
                String.valueOf(tecnico.getId()),
                tecnico.getNombre(),
                tecnico.getApellido(),
                tecnico.getDocumento(),
                tecnico.getTelefono(),
                tecnico.getEmail(),
                String.valueOf(tecnico.getSalario()),
                tecnico.getEspecialidad(),
                String.valueOf(tecnico.getAniosExperiencia()),
                tecnico.getTipoTecnico()
        );
    }
    
    private Tecnico parseTecnico(String line) {
        String[] parts = line.split(",", -1);
        
        Long id = Long.valueOf(parts[0]);
        String nombre = parts[1];
        String apellido = parts[2];
        String documento = parts[3];
        String telefono = parts[4];
        String email = parts[5];
        Double salario = Double.valueOf(parts[6]);
        String especialidad = parts[7];
        Integer aniosExperiencia = Integer.valueOf(parts[8]);
        String tipoTecnico = parts[9];
        
        if ("MECANICO".equals(tipoTecnico)) {
            return new Mecanico(id, nombre, apellido, documento, telefono, email, salario, especialidad, aniosExperiencia);
        } else {
            return new Electrico(id, nombre, apellido, documento, telefono, email, salario, especialidad, aniosExperiencia);
        }
    }
}
