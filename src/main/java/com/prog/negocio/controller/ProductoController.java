package com.prog.negocio.controller;

import com.prog.negocio.dto.ActualizarProductoDTO;
import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import com.prog.negocio.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@RequiredArgsConstructor
public class ProductoController {

    private final ProductoService productoService;

    @PostMapping("/crear")
    public ResponseEntity<ProductoResponseDTO> crear(@RequestBody ProductoRequestDTO request) {
        return ResponseEntity.ok(productoService.crear(request));
    }

    @GetMapping
    public List<ProductoResponseDTO> listar() {
        return productoService.listar();
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarProductoDTO request) {
        return ResponseEntity.ok(productoService.actualizar(id, request));
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }
}
