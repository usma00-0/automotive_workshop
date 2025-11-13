package co.edu.umanizales.automotiveworkshop_api.service;

import co.edu.umanizales.automotiveworkshop_api.model.VehicleCategory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio simple en memoria para VehicleCategory.
 */
@Service
public class VehicleCategoryService {

    private final List<VehicleCategory> categories;

    public VehicleCategoryService() {
        this.categories = new ArrayList<>();
    }

    /**
     * Agrega una categoría si su nombre no existe.
     */
    public boolean addCategory(VehicleCategory category) {
        if (category == null || category.name() == null) {
            return false;
        }
        VehicleCategory existing = findByName(category.name());
        if (existing != null) {
            return false;
        }
        categories.add(category);
        return true;
    }

    /**
     * Lista todas las categorías de vehículo.
     */
    public List<VehicleCategory> listAll() {
        return categories;
    }

    /**
     * Busca una categoría por nombre.
     */
    public VehicleCategory findByName(String name) {
        if (name == null) {
            return null;
        }
        for (VehicleCategory c : categories) {
            if (name.equalsIgnoreCase(c.name())) {
                return c;
            }
        }
        return null;
    }

    /**
     * Actualiza una categoría por nombre (reemplaza el objeto).
     */
    public VehicleCategory updateCategory(String name, VehicleCategory updated) {
        if (name == null || updated == null) {
            return null;
        }
        for (int i = 0; i < categories.size(); i++) {
            VehicleCategory c = categories.get(i);
            if (name.equalsIgnoreCase(c.name())) {
                categories.set(i, updated);
                return updated;
            }
        }
        return null;
    }

    /**
     * Elimina una categoría por nombre.
     */
    public boolean deleteByName(String name) {
        if (name == null) {
            return false;
        }
        VehicleCategory toRemove = null;
        for (VehicleCategory c : categories) {
            if (name.equalsIgnoreCase(c.name())) {
                toRemove = c;
                break;
            }
        }
        if (toRemove != null) {
            categories.remove(toRemove);
            return true;
        }
        return false;
    }
}
