package co.edu.umanizales.tallerautomotriz_api.repository;

import co.edu.umanizales.tallerautomotriz_api.model.Vehiculo;

import java.util.List;

public interface VehiculoRepository extends CrudRepository<Vehiculo, Long> {
    List<Vehiculo> findByClienteId(Long clienteId);
}
