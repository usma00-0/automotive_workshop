Enunciado del Proyecto: Sistema de Gestión de Taller Automotriz

Desarrolla una API REST utilizando Java con Spring Boot y Lombok, que permita administrar un taller 
automotriz. Toda la información se debe almacenar en archivos CSV. El sistema debe cubrir tanto la 
gestión de vehículos y clientes como las órdenes de servicio, el personal técnico y los repuestos.

🧱 Requisitos Técnicos

El sistema debe evidenciar:

✅ Encapsulamiento (atributos privados, acceso controlado)
✅ Herencia (por ejemplo: tipos de vehículos o empleados)
✅ Polimorfismo (diferentes comportamientos según el tipo de orden o servicio)
✅ Interfaces (para servicios, almacenamiento, pagos, etc.)
✅ Composición (una orden tiene varios repuestos y servicios)
✅ Agregación (un cliente puede tener varios vehículos, pero pueden existir por separado)
✅ CRUD completo de al menos 10 clases
✅ Uso de al menos un record, un enum y una clase abstracta
🧰 Entidades mínimas (10 clases CRUD)

Vehículo (autos, motos, camionetas)
Cliente
OrdenServicio
Tecnico (clase abstracta → Mecanico, Electrico)
Repuesto
ServicioRealizado
Factura
TipoPago (enum: EFECTIVO, TARJETA, TRANSFERENCIA)
CategoriaVehiculo (record: nombre, descripción)
Empleado (superclase de técnicos, administradores, etc.)
🔧 Requisitos funcionales

El sistema debe permitir:

Registrar y consultar clientes y vehículos.
Crear órdenes de servicio asociadas a un vehículo y técnico.
Asignar repuestos y servicios a cada orden.
Generar facturas con total a pagar y tipo de pago.
Consultar historial de servicios por vehículo o cliente.
Actualizar o eliminar cualquier entidad.
💡 Consideraciones adicionales

Usa interfaces para almacenamiento de datos (RepositorioCSV) y lógica de negocio (ServicioOrden).
Aplica polimorfismo en los servicios (mecánicos vs eléctricos).
Aplica composición: una OrdenServicio tiene lista de Repuesto, ServicioRealizado, Tecnico.
Aplica agregación: un Cliente tiene una lista de Vehiculo.
Usa Lombok para facilitar getters, setters, @Builder, etc.
La persistencia será por medio de archivos separados por comas (.csv).