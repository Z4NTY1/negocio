package com.prog.negocio.dto;

import com.prog.negocio.enumAtributte.TipoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class VentaResponseDTO {

    private Long id;
    private BigDecimal total;
    private TipoPago tipoPago;
    private LocalDateTime fecha;
}
