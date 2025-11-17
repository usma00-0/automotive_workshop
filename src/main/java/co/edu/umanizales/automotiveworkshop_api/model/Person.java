package co.edu.umanizales.automotiveworkshop_api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase base que representa a una persona en el sistema.
 * Cumple el requerimiento de herencia: {@link Client}, {@link Employee} y {@link Technician}
 * extienden de esta clase para reutilizar atributos comunes.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Person {
    private String id;
    private String name;
    private String email;
    private String phone;
    private Address address;
}
