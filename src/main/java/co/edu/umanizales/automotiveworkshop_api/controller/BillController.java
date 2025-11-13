package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.Bill;
import co.edu.umanizales.automotiveworkshop_api.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST simple para Bill.
 */
@RestController
@RequestMapping("/api/v1/bills")
public class BillController {

    private final BillService billService;

    @Autowired
    public BillController(BillService billService) {
        this.billService = billService;
    }

    /**
     * Lista todas las facturas.
     */
    @GetMapping
    public List<Bill> listAll() {
        return billService.listAll();
    }

    /**
     * Busca una factura por id.
     */
    @GetMapping("/{id}")
    public Bill findById(@PathVariable String id) {
        return billService.findById(id);
    }

    /**
     * Agrega una nueva factura.
     */
    @PostMapping
    public String addBill(@RequestBody Bill bill) {
        boolean added = billService.addBill(bill);
        if (added) {
            return "Bill added successfully";
        } else {
            return "Bill could not be added (null or id already exists)";
        }
    }

    /**
     * Actualiza una factura por id.
     */
    @PutMapping("/{id}")
    public Bill updateBill(@PathVariable String id, @RequestBody Bill bill) {
        return billService.updateBill(id, bill);
    }

    /**
     * Elimina una factura por id.
     */
    @DeleteMapping("/{id}")
    public String deleteBill(@PathVariable String id) {
        boolean removed = billService.deleteById(id);
        if (removed) {
            return "Bill deleted successfully";
        } else {
            return "Bill not found";
        }
    }
}
