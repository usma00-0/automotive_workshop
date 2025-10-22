package co.edu.umanizales.tallerautomotriz_api.repository;

import co.edu.umanizales.tallerautomotriz_api.model.Factura;

import java.util.List;

public interface FacturaRepository extends CrudRepository<Factura, Long> {
    List<Factura> findByClienteId(Long clienteId);
}
