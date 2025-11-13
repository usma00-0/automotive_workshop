package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.ServiceStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador simple para exponer los valores del enum ServiceStatus.
 */
@RestController
@RequestMapping("/api/v1/service-status")
public class ServiceStatusController {

    /**
     * Lista todos los estados de servicio.
     */
    @GetMapping
    public List<ServiceStatus> listAll() {
        List<ServiceStatus> list = new ArrayList<>();
        for (ServiceStatus s : ServiceStatus.values()) {
            list.add(s);
        }
        return list;
    }

    /**
     * Busca un estado de servicio por su nombre.
     */
    @GetMapping("/{name}")
    public ServiceStatus findByName(@PathVariable String name) {
        if (name == null) {
            return null;
        }
        for (ServiceStatus s : ServiceStatus.values()) {
            if (name.equalsIgnoreCase(s.name())) {
                return s;
            }
        }
        return null;
    }
}
