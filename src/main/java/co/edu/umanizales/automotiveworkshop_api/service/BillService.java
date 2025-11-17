package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.Bill;
import co.edu.umanizales.automotiveworkshop_api.model.OrderService;
import co.edu.umanizales.automotiveworkshop_api.model.PaymentType;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio de negocio para gestionar facturas (Bill).
 *
 * Responsabilidades:
 * - Cargar/guardar facturas en CSV usando una lista en memoria como almacén temporal.
 * - Exponer operaciones CRUD.
 * - Hidratación: al consultar, completa el objeto OrderService asociado a partir del id.
 */
@Service
public class BillService {

    private final List<Bill> bills;
    private static final String DATA_FILE = "bills.csv";
    private final CsvStorage csv;
    private final OrderServiceService orderServiceService;

    public BillService(OrderServiceService orderServiceService) {
        this.bills = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
        this.orderServiceService = orderServiceService;
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    /**
     * Carga el contenido de bills.csv y crea instancias mínimas de Bill.
     * La relación con OrderService se almacena como un objeto con solo el id
     * para luego ser hidratado en tiempo de lectura.
     */
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
            // Map orderId to nested OrderService with only id
            OrderService os = new OrderService();
            os.setId(parts[1]);
            b.setOrder(os);
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

    /**
     * Serializa las facturas y persiste en CSV.
     * Se guarda únicamente el id de la orden asociada para evitar duplicar datos.
     */
    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("id,orderId,issuedAt,subtotalParts,subtotalServices,taxes,total,paymentType");
        for (Bill b : bills) {
            String pt = b.getPaymentType() == null ? "" : b.getPaymentType().name();
            String orderId = "";
            if (b.getOrder() != null) {
                orderId = b.getOrder().getId() == null ? "" : b.getOrder().getId();
            }
            StringBuilder sb = new StringBuilder();
            sb.append(b.getId() == null ? "" : b.getId()).append(",")
              .append(orderId).append(",")
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
     * Hidrata la factura completando la orden asociada usando el servicio de órdenes.
     * @param b factura a hidratar
     * @return factura con su orden completa si existe
     */
    private Bill hydrate(Bill b) {
        if (b == null) { return null; }
        if (b.getOrder() != null && b.getOrder().getId() != null) {
            OrderService full = orderServiceService.findById(b.getOrder().getId());
            if (full != null) { b.setOrder(full); }
        }
        return b;
    }

    /**
     * Agrega una factura si su id no existe.
     * @param bill factura a agregar (id único y no nulo)
     * @return true si se agregó; false si es nula o ya existe el id
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
     * Lista todas las facturas hidratadas.
     * @return lista de facturas con su orden completa cuando aplica
     */
    public List<Bill> listAll() {
        List<Bill> result = new ArrayList<>();
        for (Bill b : bills) {
            result.add(hydrate(b));
        }
        return result;
    }

    /**
     * Busca una factura por id y la retorna hidratada.
     * @param id identificador de la factura
     * @return factura encontrada o null
     */
    public Bill findById(String id) {
        if (id == null) {
            return null;
        }
        for (Bill b : bills) {
            if (id.equalsIgnoreCase(b.getId())) {
                return hydrate(b);
            }
        }
        return null;
    }

    /**
     * Actualiza una factura por id (no cambia el id).
     * @param id identificador a localizar
     * @param updated datos nuevos
     * @return factura actualizada o null si no existe
     */
    public Bill updateBill(String id, Bill updated) {
        if (id == null || updated == null) {
            return null;
        }
        for (Bill b : bills) {
            if (id.equalsIgnoreCase(b.getId())) {
                b.setOrder(updated.getOrder());
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
     * Elimina una factura por id si existe.
     * @param id identificador a eliminar
     * @return true si se eliminó, false en caso contrario
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
