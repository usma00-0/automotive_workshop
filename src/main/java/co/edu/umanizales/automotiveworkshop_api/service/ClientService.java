package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.repository.CrudRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service class for handling business logic related to Clients.
 */
@Service
public class ClientService {

    private final CrudRepository<Client, String> clientRepository;

    @Autowired
    public ClientService(@Qualifier("clientRepository") CrudRepository<Client, String> clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client saveClient(Client client) {
        // Validate client data before saving
        if (client == null || client.getClientId() == null || client.getClientId().trim().isEmpty()) {
            throw new IllegalArgumentException("Client ID is required");
        }
        
        // Check if client already exists
        if (clientRepository.existsById(client.getClientId())) {
            throw new IllegalStateException("Client with ID " + client.getClientId() + " already exists");
        }
        
        return clientRepository.save(client);
    }

    public Optional<Client> getClientById(String id) {
        return clientRepository.findById(id);
    }

    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public void deleteClient(String id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalStateException("Client with ID " + id + " not found");
        }
        clientRepository.deleteById(id);
    }

    public Client updateClient(String id, Client client) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalStateException("Client with ID " + id + " not found");
        }
        if (!id.equals(client.getClientId())) {
            throw new IllegalArgumentException("Client ID in path does not match client ID in body");
        }
        return clientRepository.save(client);
    }
}
