package co.edu.umanizales.automotiveworkshop_api.repository.impl;

import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.repository.CrudRepository;
import co.edu.umanizales.automotiveworkshop_api.repository.FilePersistence;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository("clientRepository")
public class ClientFileRepository implements CrudRepository<Client, String> {

    private final FilePersistence<Client> filePersistence;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final List<Client> clients = new ArrayList<>();

    public ClientFileRepository(@Value("${app.data.clients.file:data/clients.csv}") String dataFilePath) {
        this.filePersistence = new FilePersistence<>(dataFilePath);
    }

    @PostConstruct
    private void loadData() {
        try {
            List<String> lines = filePersistence.readAllLines();
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Client client = objectMapper.readValue(line, Client.class);
                    clients.add(client);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading client data", e);
        }
    }

    private void saveData() {
        try {
            List<String> lines = clients.stream()
                    .map(client -> {
                        try {
                            return objectMapper.writeValueAsString(client);
                        } catch (Exception e) {
                            throw new RuntimeException("Error serializing client", e);
                        }
                    })
                    .collect(Collectors.toList());
            filePersistence.saveAllLines(lines);
        } catch (Exception e) {
            throw new RuntimeException("Error saving client data", e);
        }
    }

    @Override
    public Client save(Client client) {
        clients.removeIf(c -> c.getClientId().equals(client.getClientId()));
        clients.add(client);
        saveData();
        return client;
    }

    @Override
    public Optional<Client> findById(String id) {
        return clients.stream()
                .filter(client -> client.getClientId().equals(id))
                .findFirst();
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clients);
    }

    @Override
    public void deleteById(String id) {
        clients.removeIf(client -> client.getClientId().equals(id));
        saveData();
    }

    @Override
    public boolean existsById(String id) {
        return clients.stream().anyMatch(client -> client.getClientId().equals(id));
    }
}
