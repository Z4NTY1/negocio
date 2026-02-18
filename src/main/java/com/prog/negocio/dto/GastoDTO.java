package com.prog.negocio.dto;

import com.prog.negocio.enumAtributte.CategoriaGasto;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class GastoDTO {
    private CategoriaGasto categoria;
    private BigDecimal valor;
    private String descripcion;
}
