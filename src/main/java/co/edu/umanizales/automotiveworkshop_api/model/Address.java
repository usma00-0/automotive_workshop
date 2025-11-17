package co.edu.umanizales.automotiveworkshop_api.model;

/**
 * Record inmutable que representa una dirección física básica
 * (calle, ciudad, estado, código postal y país).
 * Provee el método de ayuda {@link #getFullAddress()} para formatear la dirección completa.
 */
public record Address(
    String street,
    String city,
    String state,
    String postalCode,
    String country
) {
    public String getFullAddress() {
        return String.format("%s, %s, %s %s, %s", 
            street, city, state, postalCode, country);
    }
}
