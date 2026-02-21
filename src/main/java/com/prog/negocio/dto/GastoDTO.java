package com.prog.negocio.dto;

import com.prog.negocio.enums.CategoriaGasto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Datos para registrar un gasto")
public class GastoDTO {

    @Schema(description = "Categoría del gasto", example = "INSUMOS", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "La categoría es obligatoria")
    private CategoriaGasto categoria;

    @Schema(description = "Valor del gasto en pesos", example = "15000.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a cero")
    private BigDecimal valor;

    @Schema(description = "Descripción opcional del gasto", example = "Compra de harina")
    private String descripcion;
}
