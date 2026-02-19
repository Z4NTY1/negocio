package com.prog.negocio.dto;

import com.prog.negocio.enumAtributte.TipoPago;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ResumenPagoDTO {

    private TipoPago tipoPago;
    private Long cantidadVentas;
    private BigDecimal totalDinero;
}
