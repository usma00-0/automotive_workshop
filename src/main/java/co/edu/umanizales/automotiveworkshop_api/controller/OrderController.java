package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.OrderService;
import co.edu.umanizales.automotiveworkshop_api.service.OrderServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para gestionar órdenes de servicio (OrderService).
 *
 * Endpoints expuestos (todos bajo /api/v1/orders):
 * - GET    /            -> lista todas las órdenes (ya hidratadas en el Service)
 * - GET    /{id}        -> obtiene una orden por id
 * - POST   /            -> crea una nueva orden
 * - PUT    /{id}        -> actualiza una orden existente (sin cambiar su id)
 * - DELETE /{id}        -> elimina una orden por id
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderServiceService orderServiceService;

    @Autowired
    public OrderController(OrderServiceService orderServiceService) {
        this.orderServiceService = orderServiceService;
    }

    /**
     * Lista todas las órdenes.
     * GET /api/v1/orders
     */
    @GetMapping
    public List<OrderService> listAll() {
        return orderServiceService.listAll();
    }

    /**
     * Busca una orden por id.
     * GET /api/v1/orders/{id}
     */
    @GetMapping("/{id}")
    public OrderService findById(@PathVariable String id) {
        return orderServiceService.findById(id);
    }

    /**
     * Crea una nueva orden.
     * POST /api/v1/orders
     */
    @PostMapping
    public String addOrder(@RequestBody OrderService order) {
        boolean added = orderServiceService.addOrder(order);
        if (added) {
            return "Order added successfully";
        } else {
            return "Order could not be added (null or id already exists)";
        }
    }

    /**
     * Actualiza una orden por id.
     * PUT /api/v1/orders/{id}
     */
    @PutMapping("/{id}")
    public OrderService updateOrder(@PathVariable String id, @RequestBody OrderService order) {
        return orderServiceService.updateOrder(id, order);
    }

    /**
     * Elimina una orden por id.
     * DELETE /api/v1/orders/{id}
     */
    @DeleteMapping("/{id}")
    public String deleteOrder(@PathVariable String id) {
        boolean removed = orderServiceService.deleteById(id);
        if (removed) {
            return "Order deleted successfully";
        } else {
            return "Order not found";
        }
    }
}
