# Sistema de Gestión de Taller Automotriz - API REST

API REST desarrollada con Java 17 y Spring Boot 3.5.6 para la gestión integral de un taller automotriz. Utiliza persistencia en archivos CSV y aplica principios de POO (Encapsulamiento, Herencia, Polimorfismo, Interfaces, Composición y Agregación).

## 🚀 Características

- **CRUD completo** para 10 entidades principales
- **Persistencia en CSV** sin base de datos
- **Arquitectura en capas**: Controller → Service → Repository
- **Principios OOP**: Herencia, Polimorfismo, Interfaces, Composición, Agregación
- **Manejo de excepciones** centralizado
- **Lombok** para reducir código boilerplate

## 📋 Entidades del Sistema

1. **Cliente** - Gestión de clientes del taller
2. **Vehículo** - Registro de vehículos (autos, motos, camionetas)
3. **Empleado** - Empleados del taller
4. **Técnico** (abstracta) - Clase base para técnicos
   - **Mecánico** - Técnico especializado en mecánica
   - **Eléctrico** - Técnico especializado en sistemas eléctricos
5. **Repuesto** - Catálogo de repuestos
6. **ServicioRealizado** - Servicios ofrecidos por el taller
7. **OrdenServicio** - Órdenes de trabajo
8. **Factura** - Facturación con cálculo automático de IVA
9. **TipoPago** (enum) - EFECTIVO, TARJETA, TRANSFERENCIA
10. **CategoriaVehiculo** (record) - Categorías de vehículos

## 🏗️ Arquitectura

### Principios OOP Implementados

- ✅ **Encapsulamiento**: Atributos privados con getters/setters (Lombok)
- ✅ **Herencia**: `Tecnico` → `Mecanico`, `Electrico`; `Empleado` → `Tecnico`
- ✅ **Polimorfismo**: Método abstracto `getTipoTecnico()` con diferentes implementaciones
- ✅ **Interfaces**: `CrudRepository<T, ID>` implementado por todos los repositorios CSV
- ✅ **Composición**: `OrdenServicio` contiene listas de `Repuesto` y `ServicioRealizado`
- ✅ **Agregación**: `Cliente` tiene lista de `Vehiculo` (pueden existir independientemente)

### Estructura del Proyecto

```
src/main/java/co/edu/umanizales/tallerautomotriz_api/
├── controller/          # Controladores REST
├── service/            # Lógica de negocio
├── repository/         # Interfaces de repositorio
│   └── csv/           # Implementaciones CSV
├── model/             # Entidades del dominio
└── exception/         # Manejo de excepciones
```

## 🔧 Tecnologías

- Java 17
- Spring Boot 3.5.6
- Lombok
- Maven
- CSV para persistencia

## 📦 Instalación y Ejecución

### Prerrequisitos

- JDK 17 o superior
- Maven 3.6+

### Pasos

1. **Clonar el repositorio**
```bash
git clone <repository-url>
cd tallerautomotriz_api
```

2. **Compilar el proyecto**
```bash
mvnw clean install
```

3. **Ejecutar la aplicación**
```bash
mvnw spring-boot:run
```

La API estará disponible en: `http://localhost:8080`

## 📚 Endpoints de la API

### Información General
- `GET /api` - Información de la API
- `GET /api/health` - Estado del servicio

### Clientes
- `POST /api/clientes` - Crear cliente
- `GET /api/clientes` - Listar todos
- `GET /api/clientes/{id}` - Obtener por ID
- `PUT /api/clientes/{id}` - Actualizar
- `DELETE /api/clientes/{id}` - Eliminar

### Vehículos
- `POST /api/vehiculos` - Crear vehículo
- `GET /api/vehiculos` - Listar todos
- `GET /api/vehiculos/{id}` - Obtener por ID
- `GET /api/vehiculos/cliente/{clienteId}` - Vehículos por cliente
- `PUT /api/vehiculos/{id}` - Actualizar
- `DELETE /api/vehiculos/{id}` - Eliminar

### Técnicos
- `POST /api/tecnicos` - Crear técnico
- `GET /api/tecnicos` - Listar todos
- `GET /api/tecnicos/{id}` - Obtener por ID
- `PUT /api/tecnicos/{id}` - Actualizar
- `DELETE /api/tecnicos/{id}` - Eliminar

### Repuestos
- `POST /api/repuestos` - Crear repuesto
- `GET /api/repuestos` - Listar todos
- `GET /api/repuestos/{id}` - Obtener por ID
- `PUT /api/repuestos/{id}` - Actualizar
- `DELETE /api/repuestos/{id}` - Eliminar

### Servicios Realizados
- `POST /api/servicios` - Crear servicio
- `GET /api/servicios` - Listar todos
- `GET /api/servicios/{id}` - Obtener por ID
- `PUT /api/servicios/{id}` - Actualizar
- `DELETE /api/servicios/{id}` - Eliminar

### Órdenes de Servicio
- `POST /api/ordenes` - Crear orden
- `GET /api/ordenes` - Listar todas
- `GET /api/ordenes/{id}` - Obtener por ID
- `GET /api/ordenes/vehiculo/{vehiculoId}` - Órdenes por vehículo
- `GET /api/ordenes/tecnico/{tecnicoId}` - Órdenes por técnico
- `PUT /api/ordenes/{id}` - Actualizar
- `DELETE /api/ordenes/{id}` - Eliminar

### Facturas
- `POST /api/facturas` - Crear factura
- `GET /api/facturas` - Listar todas
- `GET /api/facturas/{id}` - Obtener por ID
- `GET /api/facturas/cliente/{clienteId}` - Facturas por cliente
- `PUT /api/facturas/{id}` - Actualizar
- `DELETE /api/facturas/{id}` - Eliminar

### Empleados
- `POST /api/empleados` - Crear empleado
- `GET /api/empleados` - Listar todos
- `GET /api/empleados/{id}` - Obtener por ID
- `PUT /api/empleados/{id}` - Actualizar
- `DELETE /api/empleados/{id}` - Eliminar

## 📝 Ejemplos de Uso

### Crear un Cliente
```json
POST /api/clientes
{
  "nombre": "Juan",
  "apellido": "Pérez",
  "documento": "1234567890",
  "telefono": "3001234567",
  "email": "juan@example.com",
  "direccion": "Calle 123",
  "vehiculosIds": []
}
```

### Crear un Técnico Mecánico
```json
POST /api/tecnicos
{
  "@class": "co.edu.umanizales.tallerautomotriz_api.model.Mecanico",
  "nombre": "Carlos",
  "apellido": "Gómez",
  "documento": "9876543210",
  "telefono": "3009876543",
  "email": "carlos@taller.com",
  "salario": 2500000,
  "especialidad": "Motor",
  "aniosExperiencia": 5
}
```

### Crear una Orden de Servicio
```json
POST /api/ordenes
{
  "vehiculoId": 1,
  "tecnicoId": 1,
  "fechaIngreso": "2025-10-21T10:00:00",
  "estado": "PENDIENTE",
  "observaciones": "Cambio de aceite y revisión general",
  "repuestosIds": [1, 2],
  "serviciosIds": [1]
}
```

### Crear una Factura
```json
POST /api/facturas
{
  "ordenServicioId": 1,
  "clienteId": 1,
  "fechaEmision": "2025-10-21T15:00:00",
  "tipoPago": "TARJETA"
}
```

## 💾 Almacenamiento CSV

Los datos se almacenan en archivos CSV en el directorio `data/`:

- `clientes.csv`
- `vehiculos.csv`
- `tecnicos.csv`
- `repuestos.csv`
- `servicios_realizados.csv`
- `ordenes_servicio.csv`
- `facturas.csv`
- `empleados.csv`

## 🔍 Características Especiales

### Cálculo Automático de Totales
- Las **Órdenes de Servicio** calculan automáticamente el total de repuestos y servicios
- Las **Facturas** calculan automáticamente subtotal, IVA (19%) y total

### Polimorfismo en Técnicos
Los técnicos pueden ser de tipo `MECANICO` o `ELECTRICO`, cada uno con comportamiento específico mediante el método `getTipoTecnico()`.

### Relaciones entre Entidades
- **Agregación**: Cliente → Vehículos (un cliente puede tener múltiples vehículos)
- **Composición**: OrdenServicio → Repuestos + Servicios (la orden contiene estos elementos)

## 🛠️ Configuración

Editar `application.properties`:

```properties
# Ruta de almacenamiento CSV
csv.storage.path=data

# Puerto del servidor
server.port=8080
```

## 📄 Licencia

Este proyecto es de uso académico para la Universidad de Manizales.

## 👥 Autor

Desarrollado como proyecto académico para el curso de Programación Orientada a Objetos.
