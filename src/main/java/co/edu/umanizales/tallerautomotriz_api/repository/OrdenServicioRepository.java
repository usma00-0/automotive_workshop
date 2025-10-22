package co.edu.umanizales.tallerautomotriz_api.repository;

import co.edu.umanizales.tallerautomotriz_api.model.OrdenServicio;

import java.util.List;

public interface OrdenServicioRepository extends CrudRepository<OrdenServicio, Long> {
    List<OrdenServicio> findByVehiculoId(Long vehiculoId);
    List<OrdenServicio> findByTecnicoId(Long tecnicoId);
}
