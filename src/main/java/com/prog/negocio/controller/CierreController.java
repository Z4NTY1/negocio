package com.prog.negocio.controller;

import com.prog.negocio.dto.CierreResponseDTO;
import com.prog.negocio.service.iservice.CierreService;
import lombok.RequiredArgsConstructor;
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
            @RequestParam LocalDate inicio,
            @RequestParam LocalDate fin) {

        return ResponseEntity.ok(service.generarCierre(inicio, fin));
    }

    @GetMapping("/listar")
    public List<CierreResponseDTO> listar() {
        return service.listarCierres();
    }
}
