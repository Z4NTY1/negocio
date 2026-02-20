package com.prog.negocio.dto;

import com.prog.negocio.enums.TipoPago;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;

@Data
@AllArgsConstructor
public class VentaResponseDTO {

    private Long id;
    private BigDecimal total;
    private TipoPago tipoPago;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "America/Bogota")
    private LocalDateTime fecha;
}
