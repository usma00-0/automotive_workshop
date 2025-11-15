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
            for (int i = 0; i < lines.size(); i++) {
                String raw = lines.get(i);
                if (raw == null) { continue; }
                String line = raw.trim();
                if (line.isEmpty()) { continue; }
                Client client = objectMapper.readValue(line, Client.class);
                clients.add(client);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error loading client data", e);
        }
    }

    private void saveData() {
        try {
            List<String> lines = new ArrayList<>();
            for (int i = 0; i < clients.size(); i++) {
                Client c = clients.get(i);
                String json = objectMapper.writeValueAsString(c);
                lines.add(json);
            }
            filePersistence.saveAllLines(lines);
        } catch (Exception e) {
            throw new RuntimeException("Error saving client data", e);
        }
    }

    @Override
    public Client save(Client client) {
        if (client == null || client.getClientId() == null) {
            return client;
        }
        int indexFound = -1;
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            if (client.getClientId().equals(c.getClientId())) {
                indexFound = i;
                break;
            }
        }
        if (indexFound >= 0) {
            clients.remove(indexFound);
        }
        clients.add(client);
        saveData();
        return client;
    }

    @Override
    public Optional<Client> findById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            if (id.equals(c.getClientId())) {
                return Optional.of(c);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll() {
        return new ArrayList<>(clients);
    }

    @Override
    public void deleteById(String id) {
        if (id == null) {
            return;
        }
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            if (id.equals(c.getClientId())) {
                clients.remove(i);
                break;
            }
        }
        saveData();
    }

    @Override
    public boolean existsById(String id) {
        if (id == null) {
            return false;
        }
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            if (id.equals(c.getClientId())) {
                return true;
            }
        }
        return false;
    }
}
