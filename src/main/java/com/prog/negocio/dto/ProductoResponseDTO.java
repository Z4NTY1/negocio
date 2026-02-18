package com.prog.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductoResponseDTO {
    private Long id;
    private String nombre;
    private BigDecimal precioVenta;
    private BigDecimal costo;
    private Integer stockActual;
}
