package com.prog.negocio.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Datos para crear un nuevo producto")
public class ProductoRequestDTO {

    @Schema(description = "Nombre del producto", example = "Empanada de pollo", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Schema(description = "Precio de venta al público", example = "2500.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El precio de venta es obligatorio")
    @DecimalMin(value = "0", message = "El precio de venta no puede ser negativo")
    private BigDecimal precioVenta;

    @Schema(description = "Costo de producción", example = "1200.00", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0", message = "El costo no puede ser negativo")
    private BigDecimal costo;

    @Schema(description = "Stock inicial del producto", example = "50", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "El stock inicial es obligatorio")
    @Min(value = 0, message = "El stock inicial no puede ser negativo")
    private Integer stockInicial;
}
