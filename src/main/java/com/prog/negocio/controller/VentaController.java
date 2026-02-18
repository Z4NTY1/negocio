package com.prog.negocio.controller;
import com.prog.negocio.dto.VentaRequestDTO;
import com.prog.negocio.dto.VentaResponseDTO;
import com.prog.negocio.service.iservice.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("ventas")
@RequiredArgsConstructor
public class VentaController {
    private final VentaService service;

    @PostMapping("/registrar")
    public ResponseEntity<?> registrar(@RequestBody VentaRequestDTO dto) {
        return ResponseEntity.ok(service.registrarVenta(dto));
    }

    @GetMapping("/dia")
    public List<VentaResponseDTO> ventasDelDia(@RequestParam String fecha) {
        return service.listarPorFecha(LocalDate.parse(fecha));
    }
}
