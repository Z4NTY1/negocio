package com.prog.negocio.dto;

import com.prog.negocio.enumAtributte.TipoPago;
import lombok.Data;
import java.util.List;

@Data
public class VentaRequestDTO {
    private TipoPago tipoPago;
    private List<DetalleVentaDTO> detalles;
}
