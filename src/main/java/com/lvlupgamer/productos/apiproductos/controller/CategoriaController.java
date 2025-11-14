package com.lvlupgamer.productos.apiproductos.controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.lvlupgamer.productos.apiproductos.dto.ApiResponse;
import com.lvlupgamer.productos.apiproductos.dto.CategoriaDTO;
import com.lvlupgamer.productos.apiproductos.service.CategoriaService;



import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoriaDTO>> crear(@Valid @RequestBody CategoriaDTO categoriaDTO) {
        try {
            log.info("Solicitud para crear categoría: {}", categoriaDTO.getNombre());
            CategoriaDTO creada = categoriaService.crear(categoriaDTO);

            ApiResponse<CategoriaDTO> response = ApiResponse.<CategoriaDTO>builder()
                    .success(true)
                    .message("Categoría creada exitosamente")
                    .data(creada)
                    .code(201)
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (Exception e) {
            log.error("Error al crear categoría: {}", e.getMessage());
            ApiResponse<CategoriaDTO> response = ApiResponse.<CategoriaDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> obtenerPorId(@PathVariable Long id) {
        try {
            CategoriaDTO categoria = categoriaService.obtenerPorId(id);
            ApiResponse<CategoriaDTO> response = ApiResponse.<CategoriaDTO>builder()
                    .success(true)
                    .message("Categoría encontrada")
                    .data(categoria)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener categoría: {}", e.getMessage());
            ApiResponse<CategoriaDTO> response = ApiResponse.<CategoriaDTO>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoriaDTO>>> obtenerTodas() {
        try {
            List<CategoriaDTO> categorias = categoriaService.obtenerTodas();
            ApiResponse<List<CategoriaDTO>> response = ApiResponse.<List<CategoriaDTO>>builder()
                    .success(true)
                    .message("Categorías obtenidas correctamente")
                    .data(categorias)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al obtener categorías: {}", e.getMessage());
            ApiResponse<List<CategoriaDTO>> response = ApiResponse.<List<CategoriaDTO>>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(400)
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoriaDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CategoriaDTO categoriaDTO) {
        try {
            CategoriaDTO actualizada = categoriaService.actualizar(id, categoriaDTO);
            ApiResponse<CategoriaDTO> response = ApiResponse.<CategoriaDTO>builder()
                    .success(true)
                    .message("Categoría actualizada exitosamente")
                    .data(actualizada)
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al actualizar categoría: {}", e.getMessage());
            ApiResponse<CategoriaDTO> response = ApiResponse.<CategoriaDTO>builder()
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
            categoriaService.eliminar(id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(true)
                    .message("Categoría eliminada exitosamente")
                    .code(200)
                    .build();
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Error al eliminar categoría: {}", e.getMessage());
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .success(false)
                    .message(e.getMessage())
                    .code(404)
                    .build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
