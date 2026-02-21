package com.prog.negocio.dto;

import com.prog.negocio.enums.TipoPago;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Datos para registrar una venta")
public class VentaRequestDTO {

    @Schema(description = "Método de pago utilizado", example = "EFECTIVO", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El tipo de pago es obligatorio")
    private TipoPago tipoPago;

    @Schema(description = "Lista de productos vendidos con su cantidad", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Los detalles de la venta son obligatorios")
    @NotEmpty(message = "La venta debe tener al menos un producto")
    @Valid
    private List<DetalleVentaDTO> detalles;
}
