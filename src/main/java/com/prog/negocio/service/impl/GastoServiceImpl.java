package com.prog.negocio.service.impl;

import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.entity.GastoEntity;
import com.prog.negocio.entity.MovimientoContableEntity;
import com.prog.negocio.enums.TipoMovimientoContable;
import com.prog.negocio.repository.GastoRepository;
import com.prog.negocio.repository.MovimientoContableRepository;
import com.prog.negocio.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {

    private final GastoRepository gastoRepository;
    private final MovimientoContableRepository movimientoRepository;

    @Override
    public void registrar(GastoDTO request) {

        GastoEntity gasto = new GastoEntity();
        gasto.setCategoria(request.getCategoria());
        gasto.setFecha(LocalDateTime.now());
        gasto.setValor(request.getValor());
        gasto.setDescripcion(request.getDescripcion());

        gastoRepository.save(gasto);

        MovimientoContableEntity movimiento = new MovimientoContableEntity();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo(TipoMovimientoContable.EGRESO);
        movimiento.setMonto(request.getValor());
        movimiento.setDescripcion("Gasto: " + request.getCategoria());

        movimientoRepository.save(movimiento);
    }
}
