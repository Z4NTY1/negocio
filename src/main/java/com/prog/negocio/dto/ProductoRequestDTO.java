package com.prog.negocio.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoRequestDTO {
    private String nombre;
    private BigDecimal precioVenta;
    private BigDecimal costo;
    private Integer stockInicial;
}
