package com.lvlupgamer.productos.apiproductos.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.lvlupgamer.productos.apiproductos.model.Producto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    Optional<Producto> findByNombre(String nombre);
    
    List<Producto> findByCategoria_IdCategoria(Long idCategoria);
    
    List<Producto> findByStockGreaterThan(Integer stock);
    
    boolean existsByNombre(String nombre);
}
