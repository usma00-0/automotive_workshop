package co.edu.umanizales.automotiveworkshop_api.model;

/**
 * Estados posibles de una orden de servicio.
 * Útil para validar y documentar el ciclo de vida: PENDING, IN_PROGRESS, COMPLETED, etc.
 */
public enum ServiceStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED,
    ON_HOLD
}
