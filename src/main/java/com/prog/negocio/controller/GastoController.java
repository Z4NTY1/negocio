package com.prog.negocio.controller;

import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    @PostMapping("/registrar")
    public void registrar(@RequestBody GastoDTO request) {
        gastoService.registrar(request);
    }
}
