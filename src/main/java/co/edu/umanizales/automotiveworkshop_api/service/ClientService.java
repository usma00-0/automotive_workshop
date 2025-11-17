package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.model.Address;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de negocio para gestionar clientes (Client) usando una lista en memoria.
 *
 * Responsabilidades:
 * - Cargar/guardar clientes desde/hacia CSV.
 * - Exponer operaciones CRUD con ciclos simples (for/for-each) y validaciones básicas.
 */
@Service
public class ClientService {

    private final List<Client> clients;
    private static final String DATA_FILE = "clients.csv";
    private final CsvStorage csv;

    /**
     * Constructor por defecto que inicializa la lista y el acceso a CSV.
     */
    public ClientService() {
        this.clients = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    /**
     * Carga el contenido de clients.csv a memoria.
     * Convierte cada línea en un objeto Client, incluyendo su Address (record).
     */
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
            if (parts.length < 11) {
                continue;
            }
            Client c = new Client();
            c.setClientId(parts[0]);
            c.setId(parts[1]);
            c.setName(parts[2]);
            c.setEmail(parts[3]);
            c.setPhone(parts[4]);
            Address address = new Address(parts[5], parts[6], parts[7], parts[8], parts[9]);
            c.setAddress(address);
            c.setActive("true".equalsIgnoreCase(parts[10]));
            clients.add(c);
        }
    }

    /**
     * Serializa los clientes en memoria y persiste en clients.csv.
     */
    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("clientId,id,name,email,phone,street,city,state,postalCode,country,active");
        for (Client c : clients) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.getClientId() == null ? "" : c.getClientId()).append(",")
              .append(c.getId() == null ? "" : c.getId()).append(",")
              .append(c.getName() == null ? "" : c.getName()).append(",")
              .append(c.getEmail() == null ? "" : c.getEmail()).append(",")
              .append(c.getPhone() == null ? "" : c.getPhone()).append(",");
            Address a = c.getAddress();
            String street = a == null ? "" : a.street();
            String city = a == null ? "" : a.city();
            String state = a == null ? "" : a.state();
            String postal = a == null ? "" : a.postalCode();
            String country = a == null ? "" : a.country();
            sb.append(street == null ? "" : street).append(",")
              .append(city == null ? "" : city).append(",")
              .append(state == null ? "" : state).append(",")
              .append(postal == null ? "" : postal).append(",")
              .append(country == null ? "" : country).append(",")
              .append(c.isActive());
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
     * @return lista de clientes en memoria
     */
    public List<Client> listAll() {
        return clients;
    }

    /**
     * Busca un cliente por su clientId recorriendo la lista.
     * @param id clientId a buscar
     * @return cliente encontrado o null si no existe
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
     * @param id clientId a localizar
     * @param updated nuevos datos
     * @return cliente actualizado o null si no existe
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
                saveToCsv();
                return c;
            }
        }
        return null;
    }

    /**
     * Elimina un cliente por clientId recorriendo la lista.
     * @param id clientId a eliminar
     * @return true si se eliminó; false si no existía
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
