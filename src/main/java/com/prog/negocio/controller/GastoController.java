package com.prog.negocio.controller;

import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.service.iservice.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {
    private final GastoService service;

    @PostMapping("/registrar")
    public void registrar(@RequestBody GastoDTO dto) {
        service.registrar(dto);
    }
}
