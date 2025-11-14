package com.lvlupgamer.productos.apiproductos.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lvlupgamer.productos.apiproductos.dto.ProductoDTO;
import com.lvlupgamer.productos.apiproductos.model.Categoria;
import com.lvlupgamer.productos.apiproductos.model.Producto;
import com.lvlupgamer.productos.apiproductos.repository.CategoriaRepository;
import com.lvlupgamer.productos.apiproductos.repository.ProductoRepository;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    private static final String[] EXTENSIONES_PERMITIDAS = {"jpg", "jpeg", "png", "gif"};
    private static final long TAMANIO_MAXIMO = 5 * 1024 * 1024;

    public ProductoDTO crear(ProductoDTO productoDTO, MultipartFile imagen) throws Exception {
        log.info("Creando producto: {}", productoDTO.getNombre());

        if (productoRepository.existsByNombre(productoDTO.getNombre())) {
            throw new Exception("El producto " + productoDTO.getNombre() + " ya existe");
        }

        Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                .orElseThrow(() -> new Exception("Categoría no encontrada"));

        Producto producto = Producto.builder()
                .nombre(productoDTO.getNombre())
                .descripcion(productoDTO.getDescripcion())
                .precio(productoDTO.getPrecio())
                .stock(productoDTO.getStock())
                .categoria(categoria)
                .build();

        if (imagen != null && !imagen.isEmpty()) {
            validarImagen(imagen);
            procesarImagen(producto, imagen);
        }

        Producto guardado = productoRepository.save(producto);
        log.info("Producto creado: {}", guardado.getIdProducto());

        return convertirADTO(guardado);
    }

    private void procesarImagen(Producto producto, MultipartFile imagen) throws IOException {
        producto.setImagen(imagen.getBytes());
        producto.setImagenNombre(imagen.getOriginalFilename());
        producto.setImagenTipo(imagen.getContentType());
        producto.setImagenTamano(imagen.getSize());
    }

    private void validarImagen(MultipartFile archivo) throws Exception {
        if (archivo.getSize() > TAMANIO_MAXIMO) {
            throw new Exception("El archivo es demasiado grande. Tamaño máximo: 5 MB");
        }

        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!esExtensionPermitida(extension)) {
            throw new Exception("Extensión de archivo no permitida. Use: jpg, jpeg, png, gif");
        }
    }

    private String obtenerExtension(String nombreArchivo) {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }

    private boolean esExtensionPermitida(String extension) {
        for (String ext : EXTENSIONES_PERMITIDAS) {
            if (ext.equalsIgnoreCase(extension)) {
                return true;
            }
        }
        return false;
    }

    public ProductoDTO obtenerPorId(Long idProducto) throws Exception {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));
        return convertirADTO(producto);
    }

    public List<ProductoDTO> obtenerTodos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> obtenerPorCategoria(Long idCategoria) throws Exception {
        if (!categoriaRepository.existsById(idCategoria)) {
            throw new Exception("Categoría no encontrada");
        }

        return productoRepository.findByCategoria_IdCategoria(idCategoria)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> obtenerConStock() {
        return productoRepository.findByStockGreaterThan(0)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO actualizar(Long idProducto, ProductoDTO productoDTO) throws Exception {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        if (productoDTO.getNombre() != null && !productoDTO.getNombre().equals(producto.getNombre())) {
            if (productoRepository.existsByNombre(productoDTO.getNombre())) {
                throw new Exception("El nombre de producto ya existe");
            }
            producto.setNombre(productoDTO.getNombre());
        }

        if (productoDTO.getDescripcion() != null) {
            producto.setDescripcion(productoDTO.getDescripcion());
        }

        if (productoDTO.getPrecio() != null) {
            producto.setPrecio(productoDTO.getPrecio());
        }

        if (productoDTO.getStock() != null) {
            producto.setStock(productoDTO.getStock());
        }

        if (productoDTO.getIdCategoria() != null) {
            Categoria categoria = categoriaRepository.findById(productoDTO.getIdCategoria())
                    .orElseThrow(() -> new Exception("Categoría no encontrada"));
            producto.setCategoria(categoria);
        }

        Producto actualizado = productoRepository.save(producto);
        log.info("Producto actualizado: {}", idProducto);

        return convertirADTO(actualizado);
    }

    public ProductoDTO actualizarImagen(Long idProducto, MultipartFile imagen) throws Exception {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        validarImagen(imagen);
        procesarImagen(producto, imagen);

        Producto actualizado = productoRepository.save(producto);
        log.info("Imagen del producto actualizada: {}", idProducto);

        return convertirADTO(actualizado);
    }

    public void eliminar(Long idProducto) throws Exception {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        productoRepository.deleteById(idProducto);
        log.info("Producto eliminado: {}", idProducto);
    }

    private ProductoDTO convertirADTO(Producto producto) {
        ProductoDTO dto = ProductoDTO.builder()
                .idProducto(producto.getIdProducto())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .stock(producto.getStock())
                .idCategoria(producto.getCategoria().getIdCategoria())
                .nombreCategoria(producto.getCategoria().getNombre())
                .imagenNombre(producto.getImagenNombre())
                .imagenTipo(producto.getImagenTipo())
                .imagenTamano(producto.getImagenTamano())
                .build();

        if (producto.getImagen() != null) {
            dto.setImagenFromBytes(producto.getImagen());
        }

        return dto;
    }
}
