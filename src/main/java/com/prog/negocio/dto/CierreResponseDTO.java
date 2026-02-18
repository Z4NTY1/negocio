package com.prog.negocio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class CierreResponseDTO {

    private Long id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal totalVentas;
    private BigDecimal totalGastos;
    private BigDecimal utilidadNeta;
}
