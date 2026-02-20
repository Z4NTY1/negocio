package com.prog.negocio.controller;

import com.prog.negocio.dto.ResumenGeneralDTO;
import com.prog.negocio.dto.VentaRequestDTO;
import com.prog.negocio.dto.VentaResponseDTO;
import com.prog.negocio.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping("/registrar")
    public ResponseEntity<VentaResponseDTO> registrar(@RequestBody VentaRequestDTO request) {
        return ResponseEntity.ok(ventaService.registrarVenta(request));
    }

    @GetMapping("/dia")
    public List<VentaResponseDTO> ventasDelDia(@RequestParam String fecha) {
        return ventaService.listarPorFecha(LocalDate.parse(fecha));
    }

    @GetMapping("/dia/resumen")
    public ResumenGeneralDTO resumenDiario(@RequestParam LocalDate fecha) {
        return ventaService.resumenDiario(fecha);
    }

    @GetMapping("/quincena/resumen")
    public ResumenGeneralDTO resumenQuincenal(@RequestParam LocalDate fecha) {
        return ventaService.resumenQuincenal(fecha);
    }
}
