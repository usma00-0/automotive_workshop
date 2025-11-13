package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para gestionar Client usando una lista en memoria.
 * Endpoints CRUD básicos y respuestas sencillas (sin ResponseEntity).
 */
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientService clientService;

    /**
     * Inyección del servicio mediante constructor.
     */
    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    /**
     * Lista todos los clientes.
     * GET /api/v1/clients
     */
    @GetMapping
    public List<Client> listAll() {
        return clientService.listAll();
    }

    /**
     * Busca un cliente por su ID lógico (clientId).
     * GET /api/v1/clients/{id}
     */
    @GetMapping("/{id}")
    public Client findById(@PathVariable String id) {
        return clientService.findById(id);
    }

    /**
     * Agrega un nuevo cliente si su clientId no existe.
     * POST /api/v1/clients
     */
    @PostMapping
    public String addClient(@RequestBody Client client) {
        boolean added = clientService.addClient(client);
        if (added) {
            return "Client added successfully";
        } else {
            return "Client could not be added (null or id already exists)";
        }
    }

    /**
     * Actualiza los datos de un cliente identificado por clientId.
     * PUT /api/v1/clients/{id}
     */
    @PutMapping("/{id}")
    public Client updateClient(@PathVariable String id, @RequestBody Client client) {
        return clientService.updateClient(id, client);
    }

    /**
     * Elimina un cliente por clientId.
     * DELETE /api/v1/clients/{id}
     */
    @DeleteMapping("/{id}")
    public String deleteClient(@PathVariable String id) {
        boolean removed = clientService.deleteById(id);
        if (removed) {
            return "Client deleted successfully";
        } else {
            return "Client not found";
        }
    }
}
