package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.ServicePerformed;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para ServicePerformed.
 */
@Service
public class ServicePerformedService {

    private final List<ServicePerformed> servicesPerformed;

    public ServicePerformedService() {
        this.servicesPerformed = new ArrayList<>();
    }

    /**
     * Agrega un servicio realizado si su código no existe.
     */
    public boolean addService(ServicePerformed service) {
        if (service == null || service.getCode() == null) {
            return false;
        }
        ServicePerformed existing = findByCode(service.getCode());
        if (existing != null) {
            return false;
        }
        servicesPerformed.add(service);
        return true;
    }

    /**
     * Lista todos los servicios realizados.
     */
    public List<ServicePerformed> listAll() {
        return servicesPerformed;
    }

    /**
     * Busca un servicio realizado por código.
     */
    public ServicePerformed findByCode(String code) {
        if (code == null) {
            return null;
        }
        for (ServicePerformed s : servicesPerformed) {
            if (code.equalsIgnoreCase(s.getCode())) {
                return s;
            }
        }
        return null;
    }

    /**
     * Actualiza un servicio realizado por código (no cambia el código).
     */
    public ServicePerformed updateService(String code, ServicePerformed updated) {
        if (code == null || updated == null) {
            return null;
        }
        for (ServicePerformed s : servicesPerformed) {
            if (code.equalsIgnoreCase(s.getCode())) {
                s.setName(updated.getName());
                s.setDescription(updated.getDescription());
                s.setHours(updated.getHours());
                s.setHourlyRate(updated.getHourlyRate());
                return s;
            }
        }
        return null;
    }

    /**
     * Elimina un servicio realizado por código.
     */
    public boolean deleteByCode(String code) {
        if (code == null) {
            return false;
        }
        for (int i = 0; i < servicesPerformed.size(); i++) {
            ServicePerformed s = servicesPerformed.get(i);
            if (code.equalsIgnoreCase(s.getCode())) {
                servicesPerformed.remove(i);
                return true;
            }
        }
        return false;
    }
}
