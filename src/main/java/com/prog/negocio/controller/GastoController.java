package com.prog.negocio.controller;

import com.prog.negocio.dto.ErrorResponseDTO;
import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.service.GastoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Gastos", description = "Registro de gastos del negocio")
@RestController
@RequestMapping("/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    @Operation(summary = "Registrar gasto", description = "Registra un gasto y genera el movimiento contable (egreso)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Gasto registrado correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos del gasto inválidos",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class)))
    })
    @PostMapping("/registrar")
    public ResponseEntity<Void> registrar(@Valid @RequestBody GastoDTO request) {
        gastoService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
