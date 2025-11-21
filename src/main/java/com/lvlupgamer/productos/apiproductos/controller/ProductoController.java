package com.lvlupgamer.productos.apiproductos.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.lvlupgamer.productos.apiproductos.dto.ApiResponse;
import com.lvlupgamer.productos.apiproductos.dto.ProductoDTO;
import com.lvlupgamer.productos.apiproductos.model.Producto;
import com.lvlupgamer.productos.apiproductos.service.ProductoService;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductoDTO>> crear(
            @Valid @ModelAttribute ProductoDTO productoDTO,
            @RequestParam(value = "imagen", required = false) MultipartFile imagen) {
        try {
            log.info("Solicitud para crear producto: {}", productoDTO.getNombre());
            ProductoDTO creado = productoService.crear(productoDTO, imagen);

            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(true)
                    .message("Producto creado exitosamente")
                    .data(creado)
                    .code(201)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            log.error("Error al crear producto: {}", e.getMessage());
            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            ProductoDTO producto = productoService.obtenerPorId(id);
            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(true)
                    .message("Producto encontrado")
                    .data(producto)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener producto: {}", e.getMessage());
            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerTodos() {
        try {
            List<ProductoDTO> productos = productoService.obtenerTodos();
            ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                    .success(true)
                    .message("Productos obtenidos correctamente")
                    .data(productos)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener productos: {}", e.getMessage());
            ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerPorCategoria(@PathVariable Long idCategoria) {
        try {
            List<ProductoDTO> productos = productoService.obtenerPorCategoria(idCategoria);
            ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                    .success(true)
                    .message("Productos obtenidos correctamente")
                    .data(productos)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener productos por categoría: {}", e.getMessage());
            ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/disponibles")
    public ResponseEntity<ApiResponse<List<ProductoDTO>>> obtenerConStock() {
        try {
            List<ProductoDTO> productos = productoService.obtenerConStock();
            ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                    .success(true)
                    .message("Productos disponibles obtenidos")
                    .data(productos)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al obtener productos con stock: {}", e.getMessage());
            ApiResponse<List<ProductoDTO>> response = ApiResponse.<List<ProductoDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProductoDTO productoDTO) {
        try {
            ProductoDTO actualizado = productoService.actualizar(id, productoDTO);
            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(true)
                    .message("Producto actualizado exitosamente")
                    .data(actualizado)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al actualizar producto: {}", e.getMessage());
            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}/imagen")
    public ResponseEntity<ApiResponse<ProductoDTO>> actualizarImagen(
            @PathVariable Long id,
            @RequestParam("imagen") MultipartFile imagen) {
        try {
            ProductoDTO actualizado = productoService.actualizarImagen(id, imagen);
            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(true)
                    .message("Imagen del producto actualizada exitosamente")
                    .data(actualizado)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al actualizar imagen: {}", e.getMessage());
            ApiResponse<ProductoDTO> response = ApiResponse.<ProductoDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        try {
            productoService.eliminar(id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Producto eliminado exitosamente")
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error al eliminar producto: {}", e.getMessage());
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    // ENDPOINT PARA LA IMAGEN BINARIA
    @GetMapping("/imagen/{id}")
    public ResponseEntity<byte[]> obtenerImagen(@PathVariable Long id) {
        try {
            Producto producto = productoService.obtenerProductoEntity(id); // crea este método si no existe
            if (producto.getImagen() == null) {
                return ResponseEntity.notFound().build();
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", producto.getImagenTipo());
            headers.set("Content-Disposition", "inline; filename=\"" + producto.getImagenNombre() + "\"");
            return new ResponseEntity<>(producto.getImagen(), headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error al obtener imagen: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }
}
