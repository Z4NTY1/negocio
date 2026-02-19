package com.prog.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ResumenGeneralDTO {
    private List<ResumenPagoDTO> detalle;
    private Long totalVentas;
    private BigDecimal totalDinero;
}
