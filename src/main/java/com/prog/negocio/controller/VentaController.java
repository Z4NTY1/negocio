package com.prog.negocio.controller;

import com.prog.negocio.dto.ErrorResponseDTO;
import com.prog.negocio.dto.ResumenGeneralDTO;
import com.prog.negocio.dto.VentaRequestDTO;
import com.prog.negocio.dto.VentaResponseDTO;
import com.prog.negocio.service.VentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Ventas", description = "Registro y consulta de ventas")
@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @Operation(summary = "Registrar venta", description = "Registra una venta, descuenta stock y genera el movimiento contable")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Venta registrada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de la venta inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Stock insuficiente o producto inactivo",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<VentaResponseDTO> registrar(@Valid @RequestBody VentaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ventaService.registrarVenta(request));
    }

    @Operation(summary = "Ventas del día", description = "Lista todas las ventas realizadas en una fecha")
    @ApiResponse(responseCode = "200", description = "Lista de ventas del día")
    @GetMapping("/dia")
    public ResponseEntity<List<VentaResponseDTO>> ventasDelDia(
            @Parameter(description = "Fecha a consultar (yyyy-MM-dd)", example = "2026-02-20")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ventaService.listarPorFecha(fecha));
    }

    @Operation(summary = "Resumen diario", description = "Totales de ventas del día agrupados por tipo de pago")
    @ApiResponse(responseCode = "200", description = "Resumen del día")
    @GetMapping("/dia/resumen")
    public ResponseEntity<ResumenGeneralDTO> resumenDiario(
            @Parameter(description = "Fecha a consultar (yyyy-MM-dd)", example = "2026-02-20")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ventaService.resumenDiario(fecha));
    }

    @Operation(summary = "Resumen quincenal", description = "Totales de la quincena que contiene la fecha indicada (1-15 o 16-fin de mes)")
    @ApiResponse(responseCode = "200", description = "Resumen de la quincena")
    @GetMapping("/quincena/resumen")
    public ResponseEntity<ResumenGeneralDTO> resumenQuincenal(
            @Parameter(description = "Cualquier fecha dentro de la quincena (yyyy-MM-dd)", example = "2026-02-10")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        return ResponseEntity.ok(ventaService.resumenQuincenal(fecha));
    }
}
