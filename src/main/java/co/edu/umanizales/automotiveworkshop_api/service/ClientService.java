package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Client;
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

    /**
     * Constructor por defecto que inicializa la lista.
     */
    public ClientService() {
        this.clients = new ArrayList<>();
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
                return true;
            }
        }
        return false;
    }
}
