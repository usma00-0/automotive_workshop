# Automotive Workshop API — Guía de Sustentación

## 1) Descripción general del proyecto
- **¿Qué hace el sistema?** Gestiona clientes, vehículos, mecánicos, órdenes de servicio y facturas de un taller automotriz.
- **¿Qué problema resuelve?** Permite registrar, consultar y actualizar información del taller sin BD tradicional; usa archivos CSV como persistencia simple y didáctica.
- **Stack**: Java 17, Spring Boot 3.x, REST, Lombok, Persistencia en CSV mediante `CsvStorage`.

## 2) Estructura del backend
- **Paquetes**
  - `controller`: Endpoints REST (GET/POST/PUT/DELETE).
  - `service`: Lógica de negocio. Carga/guarda CSV. Hidratación de relaciones.
  - `model`: Entidades (POJOs), enums y records.
  - `resources/data`: Archivos `.csv` con datos y almacenamiento.
- **Comunicación**: Postman → Controller → Service → CSV (lectura/escritura) → Service → Controller → JSON.

## 3) Flujo completo de una petición (ejemplo crear orden)
1. Postman envía JSON a `POST /api/v1/orders`.
2. Spring deserializa el cuerpo hacia el modelo `OrderService`.
3. `OrderServiceService.addOrder` valida, agrega a la lista en memoria y llama `saveToCsv()`.
4. En lecturas (GET), el servicio “hidrata” referencias: a partir de `clientId`, `licensePlate`, `technicianId` obtiene los objetos completos, para devolver respuestas ricas.
5. El Controller retorna JSON a Postman.

## 4) Modelo: clases, atributos, métodos y relaciones
- `Person` (abstracta): `id`, `name`, `email`, `phone`, `address: Address`.
- `Client` (extiende Person): `clientId`, `active`, `vehicles: List<Vehicle>`. Relación 1..n Cliente→Vehículos.
- `Technician` (abstracta, extiende Person): `technicianId`, `specializations: List<TechnicianSkill>`, `experienceYears`, `available`, `hourlyRate`.
- `Mechanic` (extiende Technician): `specialty: MechanicSpecialty`.
- `Vehicle`: `licensePlate`, `brand`, `modelYear`, `color`, `owner: Client`, `category: VehicleCategory (record)`, `type: VehicleType (enum)`.
- `Car` (extiende Vehicle): `numberOfDoors`, `fuelType: FuelType`.
- `OrderService`:
  - `id`, `client: Client`, `vehicle: Vehicle`, `technician: Mechanic`, `createdAt: LocalDateTime`, `status: ServiceStatus`, `parts: List<Replacement>`, `services: List<ServicePerformed>`, `notes`.
  - `getTotalParts()`, `getTotalServices()`, `getTotal()`.
  - Relaciones 1..1 con Client/Vehicle/Mechanic y 1..n con Replacement/ServicePerformed.
- `Replacement`: `code`, `name`, `description`, `quantity`, `unitPrice`. `getTotal()`.
- `ServicePerformed`: `code`, `name`, `description`, `hours`, `hourlyRate`. `getTotal()`.
- `Bill`: `id`, `order: OrderService`, `issuedAt`, `subtotalParts`, `subtotalServices`, `taxes`, `total`, `paymentType: PaymentType`.

### Records y Enums
- **Records**: `Address(street, city, state, postalCode, country)` con `getFullAddress()`. `VehicleCategory(name, description)`.
- **Enums**:
  - `ServiceStatus { PENDING, IN_PROGRESS, COMPLETED, CANCELLED, ON_HOLD }`
  - `VehicleType { SEDAN, HATCHBACK, SUV, PICKUP, VAN, MOTORCYCLE, TRUCK, OTHER }`
  - `FuelType { GASOLINE, DIESEL, ELECTRIC, HYBRID, LPG, CNG }`
  - `MechanicSpecialty { ENGINE, TRANSMISSION, ELECTRICAL, BRAKES, SUSPENSION, AIR_CONDITIONING, DIAGNOSTICS, GENERAL }`
  - `TechnicianSkill { OIL_CHANGE, TIRE_ROTATION, ENGINE_DIAGNOSTIC, ELECTRICAL_SYSTEM, TRANSMISSION_SERVICE, BRAKE_SERVICE, ALIGNMENT, AC_SERVICE, DETAILING }`
  - `PaymentType { EFECTIVO, TARJETA, TRANSFERENCIA }`

### Conceptos Java clave
- **Clase**: define estado y comportamiento (ej.: `OrderService`).
- **Constructor**: inicializa objetos; Lombok genera constructores y `@Builder`.
- **POJO**: objeto simple de Java, sin herencias o frameworks especiales.
- **Enum**: conjunto fijo de constantes (p. ej., estados). Mejora seguridad y legibilidad.
- **Record**: clase inmutable y concisa para datos; menos boilerplate (útil para `Address`/`VehicleCategory`).

## 5) Services: responsabilidades, métodos y persistencia CSV
- **CsvStorage**: lectura/escritura de archivos en `resources/data`. Usado por todos los services.
- **ClientService**: `addClient`, `listAll`, `findById`, `updateClient`, `deleteById`. CSV: `clients.csv`.
- **VehicleService**: `addVehicle`, `listAll`, `findByPlate`, `updateVehicle`, `deleteByPlate`. CSV: `vehicles.csv`. Hidratación de `owner` usando `ClientService`.
- **CarService**: similar a `VehicleService`. CSV: `cars.csv`. Hidratación de `owner`.
- **MechanicService**: `addMechanic`, `listAll`, `findById`, `updateMechanic`, `deleteById`. CSV: `mechanics.csv`.
- **OrderServiceService**: `addOrder`, `listAll`, `findById`, `updateOrder`, `deleteById`. CSV: `orders.csv`. Hidratación de `client`, `vehicle`, `technician`.
- **BillService**: `addBill`, `listAll`, `findById`, `updateBill`, `deleteById`. CSV: `bills.csv`. Hidratación de `order`.

### Ciclos sin streams
- Búsquedas/actualizaciones con `for`/`for-each` sobre listas.
- Generación de CSV: `for` construyendo `StringBuilder` por registro.

### Persistencia CSV
- `loadFromCsv()`: lee líneas, ignora encabezado, parsea columnas y crea objetos; referencias quedan mínimas (ID/placa).
- `saveToCsv()`: serializa listas a líneas planas; 
  - En `orders.csv`: `parts` como `code:quantity:unitPrice` y `services` como `code:hours:hourlyRate` separados por `|`.
- **Hidratación en lectura**: al responder GET se transforman las referencias (IDs) en objetos completos usando otros services.

## 6) Controllers: endpoints y prueba en Postman
- Patrón común por recurso:
  - GET `/api/v1/<recurso>`
  - GET `/api/v1/<recurso>/{id}` (o `{plate}`)
  - POST `/api/v1/<recurso>`
  - PUT `/api/v1/<recurso>/{id}`
  - DELETE `/api/v1/<recurso>/{id}`
- Headers: `Content-Type: application/json`, `Accept: application/json`.
- Fechas `LocalDateTime`: formato ISO-8601, ej. `"2025-11-21T18:00:00"`.

## 7) Ejemplos reales (JSON) para Postman
### Crear Cliente
```json
{
  "clientId": "C001",
  "id": "P100",
  "name": "Ana Perez",
  "email": "ana@example.com",
  "phone": "3001234567",
  "address": {
    "street": "Calle 10 #5-20",
    "city": "City",
    "state": "ST",
    "postalCode": "00000",
    "country": "CO"
  },
  "active": true
}
```

### Crear Vehículo
```json
{
  "licensePlate": "IGQ342",
  "brand": "Fiat",
  "modelYear": 2026,
  "color": "Blanco",
  "owner": { "clientId": "C001" },
  "category": { "name": "Particular", "description": "Uso personal" },
  "type": "SEDAN"
}
```

### Crear Mecánico
```json
{
  "technicianId": "T001",
  "id": "P300",
  "name": "Carlos Ruiz",
  "email": "carlos@example.com",
  "phone": "3015555555",
  "address": {
    "street": "Calle 50 #30-10",
    "city": "City",
    "state": "ST",
    "postalCode": "00000",
    "country": "CO"
  },
  "specialty": "ENGINE",
  "specializations": ["ENGINE_DIAGNOSTIC", "BRAKE_SERVICE"],
  "experienceYears": 5,
  "available": true,
  "hourlyRate": 80000.0
}
```

### Crear Orden
```json
{
  "id": "ORD-001",
  "client": { "clientId": "C001" },
  "vehicle": { "licensePlate": "IGQ342" },
  "technician": { "technicianId": "T001" },
  "createdAt": "2025-11-21T18:00:00",
  "status": "PENDING",
  "parts": [
    { "code": "R001", "name": "Filtro de aceite", "description": "Filtro OEM", "quantity": 2, "unitPrice": 35000.0 }
  ],
  "services": [
    { "code": "S001", "name": "Cambio de aceite", "description": "Incluye mano de obra", "hours": 1.5, "hourlyRate": 70000.0 }
  ],
  "notes": "Cliente espera en sala"
}
```

### Crear Factura
```json
{
  "id": "BILL-001",
  "order": { "id": "ORD-001" },
  "issuedAt": "2025-11-21T18:30:00",
  "subtotalParts": 70000.0,
  "subtotalServices": 105000.0,
  "taxes": 33150.0,
  "total": 208150.0,
  "paymentType": "EFECTIVO"
}
```

## 8) Diagrama de clases (ASCII simplificado)
```
Person (abstract)
 |-- Client
 |     - clientId
 |     - active
 |
 |-- Technician (abstract)
       - technicianId
       - specializations: List<TechnicianSkill>
       - experienceYears, available, hourlyRate
       |
       |-- Mechanic
            - specialty: MechanicSpecialty

Vehicle
 - licensePlate
 - owner: Client
 - category: VehicleCategory (record)
 - type: VehicleType (enum)

Car extends Vehicle
 - numberOfDoors
 - fuelType: FuelType (enum)

OrderService
 - id
 - client: Client
 - vehicle: Vehicle
 - technician: Mechanic
 - status: ServiceStatus (enum)
 - parts: List<Replacement>
 - services: List<ServicePerformed>

Bill
 - id
 - order: OrderService
 - paymentType: PaymentType (enum)
```

## 9) Decisiones de diseño
- **CSV con referencias**: se guardan IDs/placa; en lectura, se hidratan las relaciones para respuestas con datos completos.
- **Clases abstractas**: `Technician` es abstracta; para deserializar en JSON se usa el concreto `Mechanic` en `OrderService.technician`.
- **Código didáctico**: sin streams; ciclos `for` y `for-each` para facilitar explicación.

## 10) Cómo sustentar ante el profesor
- Explica el ciclo de vida de una entidad (crear cliente → vehículo → orden → factura).
- Muestra en Postman cómo los GET devuelven objetos “hidratados”.
- Justifica enums/records por claridad y robustez.
- Señala dónde están los CSV y cómo los `Service` los leen/escriben.

---

Estado del proyecto:
- Controllers y Services expuestos con endpoints REST.
- Persistencia en CSV operativa.
- Hidratación aplicada en Orders, Bills, Vehicles y Cars para devolver datos completos.
