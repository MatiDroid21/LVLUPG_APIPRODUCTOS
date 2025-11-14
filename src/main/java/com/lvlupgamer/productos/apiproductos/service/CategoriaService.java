package com.lvlupgamer.productos.apiproductos.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.lvlupgamer.productos.apiproductos.dto.CategoriaDTO;
import com.lvlupgamer.productos.apiproductos.model.Categoria;
import com.lvlupgamer.productos.apiproductos.repository.CategoriaRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaDTO crear(CategoriaDTO categoriaDTO) throws Exception {
        log.info("Creando categoría: {}", categoriaDTO.getNombre());

        if (categoriaRepository.existsByNombre(categoriaDTO.getNombre())) {
            throw new Exception("La categoría " + categoriaDTO.getNombre() + " ya existe");
        }

        Categoria categoria = Categoria.builder()
                .nombre(categoriaDTO.getNombre())
                .descripcion(categoriaDTO.getDescripcion())
                .build();

        Categoria guardada = categoriaRepository.save(categoria);
        log.info("Categoría creada: {}", guardada.getIdCategoria());

        return convertirADTO(guardada);
    }

    public CategoriaDTO obtenerPorId(Long idCategoria) throws Exception {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new Exception("Categoría no encontrada"));
        return convertirADTO(categoria);
    }

    public CategoriaDTO obtenerPorNombre(String nombre) throws Exception {
        Categoria categoria = categoriaRepository.findByNombre(nombre)
                .orElseThrow(() -> new Exception("Categoría no encontrada"));
        return convertirADTO(categoria);
    }

    public List<CategoriaDTO> obtenerTodas() {
        return categoriaRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public CategoriaDTO actualizar(Long idCategoria, CategoriaDTO categoriaDTO) throws Exception {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new Exception("Categoría no encontrada"));

        if (categoriaDTO.getNombre() != null && !categoriaDTO.getNombre().equals(categoria.getNombre())) {
            if (categoriaRepository.existsByNombre(categoriaDTO.getNombre())) {
                throw new Exception("El nombre de categoría ya existe");
            }
            categoria.setNombre(categoriaDTO.getNombre());
        }

        if (categoriaDTO.getDescripcion() != null) {
            categoria.setDescripcion(categoriaDTO.getDescripcion());
        }

        Categoria actualizada = categoriaRepository.save(categoria);
        log.info("Categoría actualizada: {}", idCategoria);

        return convertirADTO(actualizada);
    }

    public void eliminar(Long idCategoria) throws Exception {
        Categoria categoria = categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new Exception("Categoría no encontrada"));

        categoriaRepository.deleteById(idCategoria);
        log.info("Categoría eliminada: {}", idCategoria);
    }

    private CategoriaDTO convertirADTO(Categoria categoria) {
        return CategoriaDTO.builder()
                .idCategoria(categoria.getIdCategoria())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .build();
    }
}
