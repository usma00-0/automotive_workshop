package co.edu.umanizales.automotiveworkshop_api.controller;

import co.edu.umanizales.automotiveworkshop_api.model.PaymentType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Controlador simple para exponer los valores del enum PaymentType.
 */
@RestController
@RequestMapping("/api/v1/payment-types")
public class PaymentTypeController {

    /**
     * Lista todos los tipos de pago.
     */
    @GetMapping
    public List<PaymentType> listAll() {
        List<PaymentType> list = new ArrayList<>();
        for (PaymentType p : PaymentType.values()) {
            list.add(p);
        }
        return list;
    }

    /**
     * Busca un tipo de pago por su nombre.
     */
    @GetMapping("/{name}")
    public PaymentType findByName(@PathVariable String name) {
        if (name == null) {
            return null;
        }
        for (PaymentType p : PaymentType.values()) {
            if (name.equalsIgnoreCase(p.name())) {
                return p;
            }
        }
        return null;
    }
}
