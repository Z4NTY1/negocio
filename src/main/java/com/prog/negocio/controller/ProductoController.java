package com.prog.negocio.controller;

import com.prog.negocio.dto.ActualizarProductoDTO;
import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import com.prog.negocio.service.iservice.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {
    private final ProductoService service;

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody ProductoRequestDTO dto) {
        return ResponseEntity.ok(service.crear(dto));
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return service.listar();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id,
                                        @RequestBody ActualizarProductoDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
