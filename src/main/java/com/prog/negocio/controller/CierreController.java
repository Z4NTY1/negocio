package com.prog.negocio.controller;

import com.prog.negocio.dto.CierreResponseDTO;
import com.prog.negocio.dto.ErrorResponseDTO;
import com.prog.negocio.service.CierreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Cierres", description = "Generación y consulta de cierres quincenales")
@RestController
@RequestMapping("/cierres")
@RequiredArgsConstructor
public class CierreController {

    private final CierreService cierreService;

    @Operation(summary = "Generar cierre", description = "Genera y persiste un cierre para el rango de fechas indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cierre generado correctamente"),
            @ApiResponse(responseCode = "409", description = "Ya existe un cierre para ese rango",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/generar")
    public ResponseEntity<CierreResponseDTO> generarCierre(
            @Parameter(description = "Fecha de inicio del rango (formato yyyy-MM-dd)", example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @Parameter(description = "Fecha de fin del rango (formato yyyy-MM-dd)", example = "2026-02-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        return ResponseEntity.ok(cierreService.generarCierre(inicio, fin));
    }

    @Operation(summary = "Listar cierres", description = "Devuelve todos los cierres almacenados")
    @ApiResponse(responseCode = "200", description = "Lista de cierres")
    @GetMapping("/listar")
    public ResponseEntity<List<CierreResponseDTO>> listar() {
        return ResponseEntity.ok(cierreService.listarCierres());
    }

    @Operation(summary = "Descargar reporte Excel", description = "Genera y descarga un archivo .xlsx con el detalle del rango indicado")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Archivo Excel generado"),
            @ApiResponse(responseCode = "400", description = "Fechas inválidas",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @GetMapping("/reporte")
    public ResponseEntity<byte[]> descargarReportePorRango(
            @Parameter(description = "Fecha de inicio (yyyy-MM-dd)", example = "2026-02-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangoInicio,
            @Parameter(description = "Fecha de fin (yyyy-MM-dd)", example = "2026-02-15")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate rangoFin) {

        byte[] excel = cierreService.generarReportePorRango(rangoInicio, rangoFin);
        String nombreArchivo = String.format("cierre_%s_a_%s.xlsx", rangoInicio, rangoFin);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }
}
