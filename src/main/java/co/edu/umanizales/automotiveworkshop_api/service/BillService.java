package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Bill;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para Bill.
 */
@Service
public class BillService {

    private final List<Bill> bills;

    public BillService() {
        this.bills = new ArrayList<>();
    }

    /**
     * Agrega una factura si su id no existe.
     */
    public boolean addBill(Bill bill) {
        if (bill == null || bill.getId() == null) {
            return false;
        }
        Bill existing = findById(bill.getId());
        if (existing != null) {
            return false;
        }
        bills.add(bill);
        return true;
    }

    /**
     * Lista todas las facturas.
     */
    public List<Bill> listAll() {
        return bills;
    }

    /**
     * Busca una factura por id.
     */
    public Bill findById(String id) {
        if (id == null) {
            return null;
        }
        for (Bill b : bills) {
            if (id.equalsIgnoreCase(b.getId())) {
                return b;
            }
        }
        return null;
    }

    /**
     * Actualiza una factura por id.
     */
    public Bill updateBill(String id, Bill updated) {
        if (id == null || updated == null) {
            return null;
        }
        for (Bill b : bills) {
            if (id.equalsIgnoreCase(b.getId())) {
                b.setOrderId(updated.getOrderId());
                b.setIssuedAt(updated.getIssuedAt());
                b.setSubtotalParts(updated.getSubtotalParts());
                b.setSubtotalServices(updated.getSubtotalServices());
                b.setTaxes(updated.getTaxes());
                b.setTotal(updated.getTotal());
                b.setPaymentType(updated.getPaymentType());
                return b;
            }
        }
        return null;
    }

    /**
     * Elimina una factura por id.
     */
    public boolean deleteById(String id) {
        if (id == null) {
            return false;
        }
        for (int i = 0; i < bills.size(); i++) {
            Bill b = bills.get(i);
            if (id.equalsIgnoreCase(b.getId())) {
                bills.remove(i);
                return true;
            }
        }
        return false;
    }
}
