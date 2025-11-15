package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio en memoria para gestionar Client con una lista interna.
 * Lógica simple y didáctica usando for-each e if.
 */
@Service
public class ClientService {

    private final List<Client> clients;
    private static final String DATA_FILE = "clients.csv";
    private final CsvStorage csv;

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public ClientService() {
        this.clients = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        clients.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) {
                continue;
            }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (trimmed.startsWith("clientId,")) {
                continue;
            }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 8) {
                continue;
            }
            Client c = new Client();
            c.setClientId(parts[0]);
            c.setId(parts[1]);
            c.setName(parts[2]);
            c.setEmail(parts[3]);
            c.setPhone(parts[4]);
            c.setAddress(parts[5]);
            c.setActive("true".equalsIgnoreCase(parts[6]));
            c.setVehicleInfo(parts[7]);
            clients.add(c);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("clientId,id,name,email,phone,address,active,vehicleInfo");
        for (Client c : clients) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.getClientId() == null ? "" : c.getClientId()).append(",")
              .append(c.getId() == null ? "" : c.getId()).append(",")
              .append(c.getName() == null ? "" : c.getName()).append(",")
              .append(c.getEmail() == null ? "" : c.getEmail()).append(",")
              .append(c.getPhone() == null ? "" : c.getPhone()).append(",")
              .append(c.getAddress() == null ? "" : c.getAddress()).append(",")
              .append(c.isActive()).append(",")
              .append(c.getVehicleInfo() == null ? "" : c.getVehicleInfo());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
    }

    /**
     * Agrega un cliente si su clientId no existe aún.
     * @param client cliente a agregar
     * @return true si se agregó, false si no fue posible
     */
    public boolean addClient(Client client) {
        if (client == null || client.getClientId() == null) {
            return false;
        }
        Client existing = findById(client.getClientId());
        if (existing != null) {
            return false;
        }
        clients.add(client);
        saveToCsv();
        return true;
    }

    /**
     * Lista todos los clientes.
     */
    public List<Client> listAll() {
        return clients;
    }

    /**
     * Busca un cliente por su clientId recorriendo la lista.
     */
    public Client findById(String id) {
        if (id == null) {
            return null;
        }
        for (Client c : clients) {
            if (id.equalsIgnoreCase(c.getClientId())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Actualiza los datos de un cliente identificado por clientId.
     */
    public Client updateClient(String id, Client updated) {
        if (id == null || updated == null) {
            return null;
        }
        for (Client c : clients) {
            if (id.equalsIgnoreCase(c.getClientId())) {
                // Datos heredados de Person
                c.setId(updated.getId());
                c.setName(updated.getName());
                c.setEmail(updated.getEmail());
                c.setPhone(updated.getPhone());
                c.setAddress(updated.getAddress());
                // Específicos de Client
                c.setActive(updated.isActive());
                c.setVehicleInfo(updated.getVehicleInfo());
                saveToCsv();
                return c;
            }
        }
        return null;
    }

    /**
     * Elimina un cliente por clientId recorriendo la lista.
     */
    public boolean deleteById(String id) {
        if (id == null) {
            return false;
        }
        for (int i = 0; i < clients.size(); i++) {
            Client c = clients.get(i);
            if (id.equalsIgnoreCase(c.getClientId())) {
                clients.remove(i);
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
