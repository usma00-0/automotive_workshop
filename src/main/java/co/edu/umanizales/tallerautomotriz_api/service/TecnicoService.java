package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.Tecnico;
import co.edu.umanizales.tallerautomotriz_api.repository.TecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TecnicoService {
    
    private final TecnicoRepository tecnicoRepository;
    
    public Tecnico save(Tecnico tecnico) {
        return tecnicoRepository.save(tecnico);
    }
    
    public Optional<Tecnico> findById(Long id) {
        return tecnicoRepository.findById(id);
    }
    
    public List<Tecnico> findAll() {
        return tecnicoRepository.findAll();
    }
    
    public void deleteById(Long id) {
        tecnicoRepository.deleteById(id);
    }
    
    public Tecnico update(Long id, Tecnico tecnico) {
        if (!tecnicoRepository.existsById(id)) {
            throw new RuntimeException("Técnico no encontrado con id: " + id);
        }
        tecnico.setId(id);
        return tecnicoRepository.save(tecnico);
    }
}
