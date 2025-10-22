package co.edu.umanizales.tallerautomotriz_api.service;

import co.edu.umanizales.tallerautomotriz_api.model.Cliente;
import co.edu.umanizales.tallerautomotriz_api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {
    
    private final ClienteRepository clienteRepository;
    
    public Cliente save(Cliente cliente) {
        return clienteRepository.save(cliente);
    }
    
    public Optional<Cliente> findById(Long id) {
        return clienteRepository.findById(id);
    }
    
    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }
    
    public void deleteById(Long id) {
        clienteRepository.deleteById(id);
    }
    
    public Cliente update(Long id, Cliente cliente) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente no encontrado con id: " + id);
        }
        cliente.setId(id);
        return clienteRepository.save(cliente);
    }
}
