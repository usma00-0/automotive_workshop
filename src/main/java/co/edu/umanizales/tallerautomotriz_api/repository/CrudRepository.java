package co.edu.umanizales.tallerautomotriz_api.repository;

import java.util.List;
import java.util.Optional;

/**
 * Generic CRUD repository interface.
 * @param <T> The type of entity this repository manages
 * @param <ID> The type of the entity's identifier
 */
public interface CrudRepository<T, ID> {
    T save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void deleteById(ID id);
    boolean existsById(ID id);
}
