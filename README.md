# LVLUPG_APIPRODUCTOS

API para la gestión de productos del proyecto LevelUpGamer DuocUC.

---

## Información General

- **Lenguaje:** Java + Spring Boot
- **Puerto por defecto:** 8080 (o el que configures)
- **Base URL:** `http://localhost:8080/api/productos`

---

## Estructura del DTO de Producto

{
"idProducto": 1,
"nombre": "Teclado gamer RGB",
"descripcion": "Teclado mecánico con retroiluminación RGB",
"precio": 39990,
"stock": 25,
"categoria": "Periféricos",
"urlImagen": "https://miservidor.com/imagenes/teclado.jpg",
"activo": true
}

text

---

## Endpoints

### Listar productos

**GET** `/api/productos`
- Obtiene todos los productos activos.
- **Respuesta ejemplo:**
[
{
"idProducto": 1,
"nombre": "Teclado gamer RGB",
...
},
{
"idProducto": 2,
"nombre": "Mouse inalámbrico",
...
}
]

text

---

### Obtener producto por ID

**GET** `/api/productos/{id}`

---

### Crear producto

**POST** `/api/productos`
- **Tipo:** JSON
- **Body ejemplo:**
{
"nombre": "Micrófono pro",
"descripcion": "Micrófono condensador de estudio",
"precio": 49990,
"stock": 10,
"categoria": "Audio",
"urlImagen": "https://miservidor.com/imagenes/microfono.jpg",
"activo": true
}

text
- **Respuesta:** Producto creado con ID asignado.

---

### Actualizar producto

**PUT** `/api/productos/{id}`
- **Tipo:** JSON
- **Body:** igual a creación
- **Respuesta:** Producto actualizado

---

### Eliminar producto

**DELETE** `/api/productos/{id}`
- Elimina producto por ID (opcionalmente puedes usar borrado lógico con flag `activo`)

---

## Ejemplos de pruebas curl

Listar productos:
curl -X GET http://localhost:8080/api/productos

text

Crear producto:
curl -X POST http://localhost:8080/api/productos
-H "Content-Type: application/json"
-d '{
"nombre": "Auriculares gamer",
"descripcion": "Sonido 7.1, micrófono removible",
"precio": 29990,
"stock": 15,
"categoria": "Audio",
"urlImagen": "https://miservidor.com/imagenes/auricular.jpg",
"activo": true
}'

text

Actualizar producto:
curl -X PUT http://localhost:8080/api/productos/1
-H "Content-Type: application/json"
-d '{
"nombre": "Auriculares gamer mejorados",
"precio": 31990,
...
}'

text

Eliminar producto:
curl -X DELETE http://localhost:8080/api/productos/1

text

---

## Validaciones y consideraciones

- El campo `nombre` es requerido y debe ser único.
- `precio` debe ser mayor o igual a 0.
- `stock` debe ser int >= 0.
- `urlImagen` debe ser un string válido (puede estar vacío).
- Usar el header `Content-Type: application/json` en POST y PUT.

---

## Notas finales

- Todos los endpoints responden con HTTP codes estándar y mensajes claros.
- Usar Postman o curl para pruebas rápidas durante desarrollo.
- En error, revisar el mensaje JSON recibido para entender el problema.

---