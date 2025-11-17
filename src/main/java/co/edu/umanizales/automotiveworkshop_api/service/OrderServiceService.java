package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.OrderService;
import co.edu.umanizales.automotiveworkshop_api.model.Replacement;
import co.edu.umanizales.automotiveworkshop_api.model.ServicePerformed;
import co.edu.umanizales.automotiveworkshop_api.model.Client;
import co.edu.umanizales.automotiveworkshop_api.model.Vehicle;
import co.edu.umanizales.automotiveworkshop_api.model.Mechanic;
import co.edu.umanizales.automotiveworkshop_api.model.ServiceStatus;
import co.edu.umanizales.automotiveworkshop_api.repository.CsvStorage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para OrderService.
 */
@Service
public class OrderServiceService {

    private final List<OrderService> orders;
    private static final String DATA_FILE = "orders.csv";
    private final CsvStorage csv;
    private final ClientService clientService;
    private final VehicleService vehicleService;
    private final MechanicService mechanicService;

    public OrderServiceService(ClientService clientService,
                               VehicleService vehicleService,
                               MechanicService mechanicService) {
        this.orders = new ArrayList<>();
        this.csv = new CsvStorage(DATA_FILE);
        this.clientService = clientService;
        this.vehicleService = vehicleService;
        this.mechanicService = mechanicService;
    }

    @PostConstruct
    private void init() {
        loadFromCsv();
    }

    private void loadFromCsv() {
        List<String> lines = csv.readAllLines();
        orders.clear();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line == null) { continue; }
            String trimmed = line.trim();
            if (trimmed.isEmpty()) { continue; }
            if (trimmed.startsWith("id,")) { continue; }
            String[] parts = trimmed.split(",", -1);
            if (parts.length < 9) { continue; }
            OrderService o = new OrderService();
            o.setId(parts[0]);
            // Create minimal nested references (by id/plate)
            Client client = new Client();
            client.setClientId(parts[1]);
            o.setClient(client);
            Vehicle vehicle = new Vehicle();
            vehicle.setLicensePlate(parts[2]);
            o.setVehicle(vehicle);
            Mechanic tech = new Mechanic();
            tech.setTechnicianId(parts[3]);
            o.setTechnician(tech);
            try { if (parts[4] != null && !parts[4].isEmpty()) { o.setCreatedAt(LocalDateTime.parse(parts[4])); } } catch (Exception e) { o.setCreatedAt(null); }
            // status enum
            ServiceStatus st = null;
            String stStr = parts[5];
            if (stStr != null && !stStr.isEmpty()) {
                try { st = ServiceStatus.valueOf(stStr.toUpperCase()); } catch (Exception e) { st = null; }
            }
            o.setStatus(st);
            // parts field: code:quantity:unitPrice joined by '|'
            List<Replacement> repList = new ArrayList<>();
            String partsSpec = parts[6];
            if (partsSpec != null && !partsSpec.isEmpty()) {
                String[] items = partsSpec.split("\\|", -1);
                for (int j = 0; j < items.length; j++) {
                    String item = items[j];
                    if (item == null || item.isEmpty()) { continue; }
                    String[] f = item.split(":", -1);
                    Replacement r = new Replacement();
                    if (f.length > 0) { r.setCode(f[0]); }
                    if (f.length > 1) { try { r.setQuantity(Integer.parseInt(f[1])); } catch (Exception e) { r.setQuantity(0); } }
                    if (f.length > 2) { try { r.setUnitPrice(Double.parseDouble(f[2])); } catch (Exception e) { r.setUnitPrice(0); } }
                    repList.add(r);
                }
            }
            o.setParts(repList);
            // services field: code:hours:hourlyRate joined by '|'
            List<ServicePerformed> servList = new ArrayList<>();
            String servSpec = parts[7];
            if (servSpec != null && !servSpec.isEmpty()) {
                String[] items = servSpec.split("\\|", -1);
                for (int j = 0; j < items.length; j++) {
                    String item = items[j];
                    if (item == null || item.isEmpty()) { continue; }
                    String[] f = item.split(":", -1);
                    ServicePerformed s = new ServicePerformed();
                    if (f.length > 0) { s.setCode(f[0]); }
                    if (f.length > 1) { try { s.setHours(Double.parseDouble(f[1])); } catch (Exception e) { s.setHours(0); } }
                    if (f.length > 2) { try { s.setHourlyRate(Double.parseDouble(f[2])); } catch (Exception e) { s.setHourlyRate(0); } }
                    servList.add(s);
                }
            }
            o.setServices(servList);
            o.setNotes(parts[8]);
            orders.add(o);
        }
    }

    private void saveToCsv() {
        List<String> lines = new ArrayList<>();
        lines.add("id,clientId,vehiclePlate,technicianId,createdAt,status,parts,services,notes");
        for (OrderService o : orders) {
            StringBuilder partsJoined = new StringBuilder();
            List<Replacement> repList = o.getParts();
            if (repList != null) {
                for (int i = 0; i < repList.size(); i++) {
                    Replacement r = repList.get(i);
                    if (i > 0) { partsJoined.append("|"); }
                    partsJoined.append(r.getCode() == null ? "" : r.getCode())
                               .append(":")
                               .append(r.getQuantity())
                               .append(":")
                               .append(r.getUnitPrice());
                }
            }
            StringBuilder servJoined = new StringBuilder();
            List<ServicePerformed> servList = o.getServices();
            if (servList != null) {
                for (int i = 0; i < servList.size(); i++) {
                    ServicePerformed s = servList.get(i);
                    if (i > 0) { servJoined.append("|"); }
                    servJoined.append(s.getCode() == null ? "" : s.getCode())
                              .append(":")
                              .append(s.getHours())
                              .append(":")
                              .append(s.getHourlyRate());
                }
            }
            StringBuilder sb = new StringBuilder();
            String clientId = "";
            if (o.getClient() != null) { clientId = o.getClient().getClientId() == null ? "" : o.getClient().getClientId(); }
            String vehiclePlate = "";
            if (o.getVehicle() != null) { vehiclePlate = o.getVehicle().getLicensePlate() == null ? "" : o.getVehicle().getLicensePlate(); }
            String technicianId = "";
            if (o.getTechnician() != null) { technicianId = o.getTechnician().getTechnicianId() == null ? "" : o.getTechnician().getTechnicianId(); }
            String statusName = o.getStatus() == null ? "" : o.getStatus().name();
            sb.append(o.getId() == null ? "" : o.getId()).append(",")
              .append(clientId).append(",")
              .append(vehiclePlate).append(",")
              .append(technicianId).append(",")
              .append(o.getCreatedAt() == null ? "" : o.getCreatedAt().toString()).append(",")
              .append(statusName).append(",")
              .append(partsJoined.toString()).append(",")
              .append(servJoined.toString()).append(",")
              .append(o.getNotes() == null ? "" : o.getNotes());
            lines.add(sb.toString());
        }
        csv.writeAllLines(lines);
    }

    private OrderService hydrate(OrderService o) {
        if (o == null) { return null; }
        // Cliente completo
        if (o.getClient() != null && o.getClient().getClientId() != null) {
            Client full = clientService.findById(o.getClient().getClientId());
            if (full != null) { o.setClient(full); }
        }
        // Vehículo completo
        if (o.getVehicle() != null && o.getVehicle().getLicensePlate() != null) {
            Vehicle fullV = vehicleService.findByPlate(o.getVehicle().getLicensePlate());
            if (fullV != null) { o.setVehicle(fullV); }
        }
        // Mecánico completo
        if (o.getTechnician() != null && o.getTechnician().getTechnicianId() != null) {
            Mechanic fullM = mechanicService.findById(o.getTechnician().getTechnicianId());
            if (fullM != null) { o.setTechnician(fullM); }
        }
        return o;
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
        saveToCsv();
        return true;
    }

    /**
     * Lista todas las órdenes de servicio.
     */
    public List<OrderService> listAll() {
        List<OrderService> result = new ArrayList<>();
        for (OrderService o : orders) {
            result.add(hydrate(o));
        }
        return result;
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
                return hydrate(o);
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
                o.setClient(updated.getClient());
                o.setVehicle(updated.getVehicle());
                o.setTechnician(updated.getTechnician());
                o.setCreatedAt(updated.getCreatedAt());
                o.setStatus(updated.getStatus());
                o.setParts(updated.getParts());
                o.setServices(updated.getServices());
                o.setNotes(updated.getNotes());
                saveToCsv();
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
                saveToCsv();
                return true;
            }
        }
        return false;
    }
}
