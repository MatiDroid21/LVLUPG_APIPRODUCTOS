package com.lvlupgamer.productos.apiproductos.dto;


import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Base64;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    
    private Long idProducto;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    @Size(max = 200, message = "El nombre no puede exceder 200 caracteres")
    private String nombre;
    
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @DecimalMax(value = "999999.99", message = "El precio no puede exceder 999999.99")
    private Double precio;
    
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long idCategoria;
    
    private String nombreCategoria;
    
    private String imagenNombre;
    private String imagenTipo;
    private Long imagenTamano;
    private String imagenBase64;
    
    public void setImagenFromBytes(byte[] imagenBytes) {
        if (imagenBytes != null) {
            this.imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
        }
    }
}

