package com.lvlupgamer.productos.apiproductos.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import com.lvlupgamer.productos.apiproductos.model.Categoria;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "producto_seq")
    @SequenceGenerator(name = "producto_seq", sequenceName = "SEQ_PRODUCTOS", allocationSize = 1)
    @Column(name = "id_producto")
    private Long idProducto;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;
    
    @Column(name = "descripcion", columnDefinition = "CLOB")
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "999999.99", message = "El precio no puede exceder 999999.99")
    @Column(name = "precio", nullable = false)
    private Double precio;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(name = "stock", nullable = false)
    private Integer stock;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    @NotNull(message = "La categoría es obligatoria")
    private Categoria categoria;
    
    @Column(name = "fecha_ingreso")
    @Builder.Default
    private LocalDateTime fechaIngreso = LocalDateTime.now();
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "imagen")
    private byte[] imagen;
    
    @Column(name = "imagen_nombre", length = 200)
    private String imagenNombre;
    
    @Column(name = "imagen_tipo", length = 100)
    private String imagenTipo;
    
    @Column(name = "imagen_tamano")
    private Long imagenTamano;
}
