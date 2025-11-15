package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Bill;
import co.edu.umanizales.automotiveworkshop_api.model.PaymentType;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para Bill.
 */
@Service
public class BillService {

    private final List<Bill> bills;
    private static final String DATA_FILE = "bills.csv";
    private final CsvStorage csv;

    public BillService() {
        this.bills = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        bills.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) { continue; }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) { continue; }
            if (trimmed.startsWith("id,")) { continue; }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 8) { continue; }
            Bill b = new Bill();
            b.setId(parts[0]);
            b.setOrderId(parts[1]);
            try { if (parts[2] != null && !parts[2].isEmpty()) { b.setIssuedAt(LocalDateTime.parse(parts[2])); } } catch (Exception e) { b.setIssuedAt(null); }
            try { b.setSubtotalParts(Double.parseDouble(parts[3])); } catch (Exception e) { b.setSubtotalParts(0); }
            try { b.setSubtotalServices(Double.parseDouble(parts[4])); } catch (Exception e) { b.setSubtotalServices(0); }
            try { b.setTaxes(Double.parseDouble(parts[5])); } catch (Exception e) { b.setTaxes(0); }
            try { b.setTotal(Double.parseDouble(parts[6])); } catch (Exception e) { b.setTotal(0); }
            PaymentType pt = null;
            String ptStr = parts[7];
            if (ptStr != null && !ptStr.isEmpty()) {
                try { pt = PaymentType.valueOf(ptStr.toUpperCase()); } catch (Exception e) { pt = null; }
            }
            b.setPaymentType(pt);
            bills.add(b);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("id,orderId,issuedAt,subtotalParts,subtotalServices,taxes,total,paymentType");
        for (Bill b : bills) {
            String pt = b.getPaymentType() == null ? "" : b.getPaymentType().name();
            StringBuilder sb = new StringBuilder();
            sb.append(b.getId() == null ? "" : b.getId()).append(",")
              .append(b.getOrderId() == null ? "" : b.getOrderId()).append(",")
              .append(b.getIssuedAt() == null ? "" : b.getIssuedAt().toString()).append(",")
              .append(b.getSubtotalParts()).append(",")
              .append(b.getSubtotalServices()).append(",")
              .append(b.getTaxes()).append(",")
              .append(b.getTotal()).append(",")
              .append(pt);
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
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
        saveToCsv();
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
                saveToCsv();
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
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
