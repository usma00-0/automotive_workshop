package co.edu.umanizales.automotiveworkshop_api.model;

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
