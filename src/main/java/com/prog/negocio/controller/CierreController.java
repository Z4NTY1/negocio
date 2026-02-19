package com.prog.negocio.controller;

import com.prog.negocio.dto.CierreResponseDTO;
import com.prog.negocio.service.iservice.CierreService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/cierres")
@RequiredArgsConstructor

public class CierreController {

    private final CierreService service;

    @PostMapping("/generar")
    public ResponseEntity<CierreResponseDTO> generarCierre(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin) {

        return ResponseEntity.ok(service.generarCierre(inicio, fin));
    }

    @GetMapping("/listar")
    public List<CierreResponseDTO> listar() {
        return service.listarCierres();
    }

    @GetMapping("/reporte")
    public ResponseEntity<byte[]> descargarReportePorRango(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate rangoInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate rangoFin) {

        byte[] excel =
                service.generarReportePorRango(rangoInicio, rangoFin);

        String nombreArchivo = String.format(
                "cierre_%s_a_%s.xlsx",
                rangoInicio,
                rangoFin
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=" + nombreArchivo)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excel);
    }
}
