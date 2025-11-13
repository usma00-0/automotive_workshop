package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.OrderService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para OrderService.
 */
@Service
public class OrderServiceService {

    private final List<OrderService> orders;

    public OrderServiceService() {
        this.orders = new ArrayList<>();
    }

    /**
     * Agrega una orden si su id no existe.
     */
    public boolean addOrder(OrderService order) {
        if (order == null || order.getId() == null) {
            return false;
        }
        OrderService existing = findById(order.getId());
        if (existing != null) {
            return false;
        }
        orders.add(order);
        return true;
    }

    /**
     * Lista todas las órdenes de servicio.
     */
    public List<OrderService> listAll() {
        return orders;
    }

    /**
     * Busca una orden por id.
     */
    public OrderService findById(String id) {
        if (id == null) {
            return null;
        }
        for (OrderService o : orders) {
            if (id.equalsIgnoreCase(o.getId())) {
                return o;
            }
        }
        return null;
    }

    /**
     * Actualiza una orden por id.
     */
    public OrderService updateOrder(String id, OrderService updated) {
        if (id == null || updated == null) {
            return null;
        }
        for (OrderService o : orders) {
            if (id.equalsIgnoreCase(o.getId())) {
                o.setClientId(updated.getClientId());
                o.setVehiclePlate(updated.getVehiclePlate());
                o.setTechnicianId(updated.getTechnicianId());
                o.setCreatedAt(updated.getCreatedAt());
                o.setStatus(updated.getStatus());
                o.setParts(updated.getParts());
                o.setServices(updated.getServices());
                o.setNotes(updated.getNotes());
                return o;
            }
        }
        return null;
    }

    /**
     * Elimina una orden por id.
     */
    public boolean deleteById(String id) {
        if (id == null) {
            return false;
        }
        for (int i = 0; i < orders.size(); i++) {
            OrderService o = orders.get(i);
            if (id.equalsIgnoreCase(o.getId())) {
                orders.remove(i);
                return true;
            }
        }
        return false;
    }
}
