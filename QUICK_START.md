# 🚀 Guía de Inicio Rápido

## Ejecutar la Aplicación

### Opción 1: Usando Maven Wrapper (Recomendado)
```bash
./mvnw spring-boot:run
```

### Opción 2: Usando Maven instalado
```bash
mvn spring-boot:run
```

### Opción 3: Compilar y ejecutar JAR
```bash
./mvnw clean package
java -jar target/tallerautomotriz_api-0.0.1-SNAPSHOT.jar
```

## Verificar que la API está funcionando

Abrir en el navegador o usar curl:
```bash
curl http://localhost:8080/api/health
```

Respuesta esperada:
```json
{
  "status": "UP",
  "message": "Taller Automotriz API is running"
}
```

## Flujo de Prueba Completo

### 1. Crear un Cliente
```bash
curl -X POST http://localhost:8080/api/clientes \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan",
    "apellido": "Pérez",
    "documento": "1234567890",
    "telefono": "3001234567",
    "email": "juan@example.com",
    "direccion": "Calle 123",
    "vehiculosIds": []
  }'
```

### 2. Crear un Vehículo
```bash
curl -X POST http://localhost:8080/api/vehiculos \
  -H "Content-Type: application/json" \
  -d '{
    "placa": "ABC123",
    "marca": "Toyota",
    "modelo": "Corolla",
    "anio": 2020,
    "color": "Blanco",
    "tipoVehiculo": "AUTO",
    "clienteId": 1,
    "categoriaVehiculoNombre": "Sedan"
  }'
```

### 3. Crear un Técnico Mecánico
```bash
curl -X POST http://localhost:8080/api/tecnicos \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Carlos",
    "apellido": "Gómez",
    "documento": "9876543210",
    "telefono": "3009876543",
    "email": "carlos@taller.com",
    "salario": 2500000,
    "especialidad": "Motor",
    "aniosExperiencia": 5
  }'
```

### 4. Crear un Repuesto
```bash
curl -X POST http://localhost:8080/api/repuestos \
  -H "Content-Type: application/json" \
  -d '{
    "codigo": "REP001",
    "nombre": "Filtro de Aceite",
    "descripcion": "Filtro de aceite para motor",
    "precio": 25000,
    "stock": 50,
    "marca": "Bosch"
  }'
```

### 5. Crear un Servicio
```bash
curl -X POST http://localhost:8080/api/servicios \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Cambio de Aceite",
    "descripcion": "Cambio de aceite y filtro",
    "precio": 80000,
    "duracionHoras": 1,
    "tipoServicio": "MECANICO"
  }'
```

### 6. Crear una Orden de Servicio
```bash
curl -X POST http://localhost:8080/api/ordenes \
  -H "Content-Type: application/json" \
  -d '{
    "vehiculoId": 1,
    "tecnicoId": 1,
    "fechaIngreso": "2025-10-21T10:00:00",
    "estado": "PENDIENTE",
    "observaciones": "Cambio de aceite y revisión general",
    "repuestosIds": [1],
    "serviciosIds": [1]
  }'
```

### 7. Crear una Factura
```bash
curl -X POST http://localhost:8080/api/facturas \
  -H "Content-Type: application/json" \
  -d '{
    "ordenServicioId": 1,
    "clienteId": 1,
    "fechaEmision": "2025-10-21T15:00:00",
    "tipoPago": "TARJETA"
  }'
```

### 8. Consultar todos los clientes
```bash
curl http://localhost:8080/api/clientes
```

### 9. Consultar vehículos de un cliente
```bash
curl http://localhost:8080/api/vehiculos/cliente/1
```

### 10. Consultar órdenes de servicio de un vehículo
```bash
curl http://localhost:8080/api/ordenes/vehiculo/1
```

## Archivos CSV Generados

Los datos se almacenan en la carpeta `data/`:
- `clientes.csv`
- `vehiculos.csv`
- `tecnicos.csv`
- `repuestos.csv`
- `servicios_realizados.csv`
- `ordenes_servicio.csv`
- `facturas.csv`
- `empleados.csv`

## Importar Colección de Postman

1. Abrir Postman
2. Click en "Import"
3. Seleccionar el archivo `postman_collection_example.json`
4. Ejecutar las peticiones en orden

## Endpoints Disponibles

### General
- `GET /api` - Información de la API
- `GET /api/health` - Estado del servicio

### Clientes
- `POST /api/clientes` - Crear
- `GET /api/clientes` - Listar todos
- `GET /api/clientes/{id}` - Obtener por ID
- `PUT /api/clientes/{id}` - Actualizar
- `DELETE /api/clientes/{id}` - Eliminar

### Vehículos
- `POST /api/vehiculos` - Crear
- `GET /api/vehiculos` - Listar todos
- `GET /api/vehiculos/{id}` - Obtener por ID
- `GET /api/vehiculos/cliente/{clienteId}` - Por cliente
- `PUT /api/vehiculos/{id}` - Actualizar
- `DELETE /api/vehiculos/{id}` - Eliminar

### Técnicos
- `POST /api/tecnicos` - Crear
- `GET /api/tecnicos` - Listar todos
- `GET /api/tecnicos/{id}` - Obtener por ID
- `PUT /api/tecnicos/{id}` - Actualizar
- `DELETE /api/tecnicos/{id}` - Eliminar

### Repuestos
- `POST /api/repuestos` - Crear
- `GET /api/repuestos` - Listar todos
- `GET /api/repuestos/{id}` - Obtener por ID
- `PUT /api/repuestos/{id}` - Actualizar
- `DELETE /api/repuestos/{id}` - Eliminar

### Servicios Realizados
- `POST /api/servicios` - Crear
- `GET /api/servicios` - Listar todos
- `GET /api/servicios/{id}` - Obtener por ID
- `PUT /api/servicios/{id}` - Actualizar
- `DELETE /api/servicios/{id}` - Eliminar

### Órdenes de Servicio
- `POST /api/ordenes` - Crear
- `GET /api/ordenes` - Listar todas
- `GET /api/ordenes/{id}` - Obtener por ID
- `GET /api/ordenes/vehiculo/{vehiculoId}` - Por vehículo
- `GET /api/ordenes/tecnico/{tecnicoId}` - Por técnico
- `PUT /api/ordenes/{id}` - Actualizar
- `DELETE /api/ordenes/{id}` - Eliminar

### Facturas
- `POST /api/facturas` - Crear
- `GET /api/facturas` - Listar todas
- `GET /api/facturas/{id}` - Obtener por ID
- `GET /api/facturas/cliente/{clienteId}` - Por cliente
- `PUT /api/facturas/{id}` - Actualizar
- `DELETE /api/facturas/{id}` - Eliminar

### Empleados
- `POST /api/empleados` - Crear
- `GET /api/empleados` - Listar todos
- `GET /api/empleados/{id}` - Obtener por ID
- `PUT /api/empleados/{id}` - Actualizar
- `DELETE /api/empleados/{id}` - Eliminar

## Solución de Problemas

### Error: Puerto 8080 ya en uso
Cambiar el puerto en `application.properties`:
```properties
server.port=8081
```

### Error: No se puede crear el directorio data/
Verificar permisos de escritura en el directorio del proyecto.

### Error de compilación
Limpiar y recompilar:
```bash
./mvnw clean compile
```

## Características Implementadas

✅ **10+ Entidades CRUD**
✅ **Enum**: TipoPago (EFECTIVO, TARJETA, TRANSFERENCIA)
✅ **Record**: CategoriaVehiculo
✅ **Clase Abstracta**: Tecnico
✅ **Herencia**: Mecanico y Electrico extienden Tecnico
✅ **Polimorfismo**: getTipoTecnico() con diferentes implementaciones
✅ **Interfaces**: CrudRepository<T, ID>
✅ **Composición**: OrdenServicio contiene Repuestos y Servicios
✅ **Agregación**: Cliente tiene Vehículos
✅ **Persistencia CSV**: Todos los datos en archivos CSV
✅ **Cálculo Automático**: Totales en órdenes y facturas con IVA

## Próximos Pasos

1. Ejecutar la aplicación
2. Probar los endpoints con Postman o curl
3. Verificar los archivos CSV generados en la carpeta `data/`
4. Revisar el código para entender la arquitectura
5. Extender funcionalidades según necesidades
