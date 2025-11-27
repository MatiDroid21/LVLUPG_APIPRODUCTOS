package com.lvlupgamer.productos.apiproductos;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import com.lvlupgamer.productos.apiproductos.dto.ProductoDTO;
import com.lvlupgamer.productos.apiproductos.model.Producto;
import com.lvlupgamer.productos.apiproductos.model.Categoria;
import com.lvlupgamer.productos.apiproductos.repository.ProductoRepository;
import com.lvlupgamer.productos.apiproductos.service.ProductoService;
import com.lvlupgamer.productos.apiproductos.repository.CategoriaRepository;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductoServiceIntegrationTest {

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProductoRepository productoRepository;

    private Categoria categoria;

    @BeforeEach
    public void setUp() {
        // Asegurarse de que existe una categoría de prueba
        categoria = categoriaRepository.findById(1L)
                .orElseGet(() -> {
                    Categoria nuevaCategoria = Categoria.builder()
                            .nombre("Consolas Test")
                            .descripcion("Categoría de prueba")
                            .build();
                    return categoriaRepository.save(nuevaCategoria);
                });
    }

    @Test
    public void testGuardarProductoConCategoria() {
        // Given
        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("PlayStation 5 Test")
                .descripcion("Consola de última generación para testing")
                .precio(499.99)
                .stock(10)
                .idCategoria(categoria.getIdCategoria())
                .build();

        // When
        ProductoDTO guardado = null;
        try {
            guardado = productoService.crear(productoDTO, null);
        } catch (Exception e) {
            fail("No debería lanzar excepción: " + e.getMessage());
        }

        // Then
        assertNotNull(guardado.getIdProducto());
        assertEquals("PlayStation 5 Test", guardado.getNombre());
        assertEquals(499.99, guardado.getPrecio());
        assertEquals(10, guardado.getStock());

        System.out.println("✅ Producto guardado con ID: " + guardado.getIdProducto());
    }

    @Test
    public void testBuscarProductoPorId() {
        // Given - Primero guardamos un producto
        ProductoDTO nuevoProducto = ProductoDTO.builder()
                .nombre("Xbox Series X Test")
                .descripcion("Consola Microsoft")
                .precio(499.99)
                .stock(5)
                .idCategoria(categoria.getIdCategoria())
                .build();

        ProductoDTO creado = null;
        try {
            creado = productoService.crear(nuevoProducto, null);
        } catch (Exception e) {
            fail("Error al crear producto: " + e.getMessage());
        }

        // When - Buscamos el producto
        ProductoDTO encontrado = null;
        try {
            encontrado = productoService.obtenerPorId(creado.getIdProducto());
        } catch (Exception e) {
            fail("Error al buscar producto: " + e.getMessage());
        }

        // Then
        assertNotNull(encontrado);
        assertEquals("Xbox Series X Test", encontrado.getNombre());
        assertEquals(categoria.getIdCategoria(), encontrado.getIdCategoria());
        
        System.out.println("Producto encontrado: ID=" + encontrado.getIdProducto() + 
                         ", Nombre=" + encontrado.getNombre());
        System.out.println("Categoría: " + encontrado.getNombreCategoria());
        System.out.println("Stock disponible: " + encontrado.getStock());
    }

    @Test
    public void testActualizarStockProducto() {
        // Given - Crear producto inicial
        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("Nintendo Switch Test")
                .descripcion("Consola híbrida")
                .precio(299.99)
                .stock(15)
                .idCategoria(categoria.getIdCategoria())
                .build();

        ProductoDTO creado = null;
        try {
            creado = productoService.crear(productoDTO, null);
        } catch (Exception e) {
            fail("Error al crear producto: " + e.getMessage());
        }

        // When - Actualizar stock
        ProductoDTO updateDTO = ProductoDTO.builder()
                .stock(25)
                .build();

        ProductoDTO actualizado = null;
        try {
            actualizado = productoService.actualizar(creado.getIdProducto(), updateDTO);
        } catch (Exception e) {
            fail("Error al actualizar producto: " + e.getMessage());
        }

        // Then
        assertNotNull(actualizado);
        assertEquals(25, actualizado.getStock());

        System.out.println("Stock actualizado de " + creado.getStock() + " a " + actualizado.getStock());
    }

    @Test
    public void testObtenerTodosLosProductos() {
        // When
        List<ProductoDTO> productos = productoService.obtenerTodos();

        // Then
        assertNotNull(productos);
        assertTrue(productos.size() >= 0);

        System.out.println("Total de productos encontrados: " + productos.size());
        productos.forEach(p -> 
            System.out.println("Producto: " + p.getNombre() + 
                             " | Precio: $" + p.getPrecio() + 
                             " | Stock: " + p.getStock())
        );
    }

    @Test
    public void testObtenerProductosPorCategoria() {
        // Given - Crear varios productos en la misma categoría
        try {
            ProductoDTO producto1 = ProductoDTO.builder()
                    .nombre("Producto Cat Test 1")
                    .precio(100.0)
                    .stock(5)
                    .idCategoria(categoria.getIdCategoria())
                    .build();
            productoService.crear(producto1, null);

            ProductoDTO producto2 = ProductoDTO.builder()
                    .nombre("Producto Cat Test 2")
                    .precio(200.0)
                    .stock(10)
                    .idCategoria(categoria.getIdCategoria())
                    .build();
            productoService.crear(producto2, null);
        } catch (Exception e) {
            fail("Error al crear productos de prueba: " + e.getMessage());
        }

        // When
        List<ProductoDTO> productosPorCategoria = null;
        try {
            productosPorCategoria = productoService.obtenerPorCategoria(categoria.getIdCategoria());
        } catch (Exception e) {
            fail("Error al obtener productos por categoría: " + e.getMessage());
        }

        // Then
        assertNotNull(productosPorCategoria);
        assertTrue(productosPorCategoria.size() >= 2);

        System.out.println("Productos en categoría '" + categoria.getNombre() + "': " + 
                         productosPorCategoria.size());
        productosPorCategoria.forEach(p -> 
            System.out.println("- " + p.getNombre() + " (Stock: " + p.getStock() + ")")
        );
    }

    @Test
    public void testObtenerProductosConStock() {
        // When
        List<ProductoDTO> productosConStock = productoService.obtenerConStock();

        // Then
        assertNotNull(productosConStock);
        productosConStock.forEach(p -> 
            assertTrue(p.getStock() > 0, "El producto " + p.getNombre() + " debería tener stock > 0")
        );

        System.out.println("Productos con stock disponible: " + productosConStock.size());
        productosConStock.forEach(p -> 
            System.out.println("✓ " + p.getNombre() + " - Stock: " + p.getStock())
        );
    }

    @Test
    public void testActualizarPrecioProducto() {
        // Given
        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("Producto Precio Test")
                .descripcion("Para probar actualización de precio")
                .precio(100.0)
                .stock(5)
                .idCategoria(categoria.getIdCategoria())
                .build();

        ProductoDTO creado = null;
        try {
            creado = productoService.crear(productoDTO, null);
        } catch (Exception e) {
            fail("Error al crear producto: " + e.getMessage());
        }

        // When
        ProductoDTO updateDTO = ProductoDTO.builder()
                .precio(150.0)
                .build();

        ProductoDTO actualizado = null;
        try {
            actualizado = productoService.actualizar(creado.getIdProducto(), updateDTO);
        } catch (Exception e) {
            fail("Error al actualizar precio: " + e.getMessage());
        }

        // Then
        assertNotNull(actualizado);
        assertEquals(150.0, actualizado.getPrecio());

        System.out.println("Precio actualizado: $" + creado.getPrecio() + " → $" + actualizado.getPrecio());
    }

    @Test
    public void testEliminarProducto() {
        // Given
        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("Producto Para Eliminar")
                .precio(50.0)
                .stock(1)
                .idCategoria(categoria.getIdCategoria())
                .build();

        ProductoDTO creado = null;
        try {
            creado = productoService.crear(productoDTO, null);
        } catch (Exception e) {
            fail("Error al crear producto: " + e.getMessage());
        }

        Long idProducto = creado.getIdProducto();

        // When
        try {
            productoService.eliminar(idProducto);
        } catch (Exception e) {
            fail("Error al eliminar producto: " + e.getMessage());
        }

        // Then - Verificar que ya no existe
        assertThrows(Exception.class, () -> {
            productoService.obtenerPorId(idProducto);
        });

        System.out.println("✅ Producto eliminado correctamente con ID: " + idProducto);
    }

    @Test
    public void testProductoDuplicadoLanzaExcepcion() {
        // Given
        ProductoDTO productoDTO = ProductoDTO.builder()
                .nombre("Producto Duplicado Test")
                .precio(100.0)
                .stock(5)
                .idCategoria(categoria.getIdCategoria())
                .build();

        try {
            productoService.crear(productoDTO, null);
        } catch (Exception e) {
            fail("Error al crear primer producto: " + e.getMessage());
        }

        // When & Then - Intentar crear el mismo producto de nuevo
        Exception exception = assertThrows(Exception.class, () -> {
            productoService.crear(productoDTO, null);
        });

        assertTrue(exception.getMessage().contains("ya existe"));
        System.out.println("✅ Excepción esperada capturada: " + exception.getMessage());
    }
}
