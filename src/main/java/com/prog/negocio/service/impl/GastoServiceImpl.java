package com.prog.negocio.service.impl;

import com.prog.negocio.business.ErrorCodesNegocio;
import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.entity.GastoEntity;
import com.prog.negocio.entity.MovimientoContableEntity;
import com.prog.negocio.exceptions.BcExceptionFactory;
import com.prog.negocio.enums.TipoMovimientoContable;
import com.prog.negocio.mapper.GastoMapper;
import com.prog.negocio.repository.GastoRepository;
import com.prog.negocio.repository.MovimientoContableRepository;
import com.prog.negocio.service.GastoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {

    private static final Logger log = LoggerFactory.getLogger(GastoServiceImpl.class);

    private final GastoRepository gastoRepository;
    private final MovimientoContableRepository movimientoRepository;

    /**
     * Registra un gasto y genera el movimiento contable asociado (egreso).
     *
     * @param request datos del gasto (categoría, valor, descripción)
     */
    @Override
    @Transactional
    public void registrar(GastoDTO request) {

        validarGasto(request);

        GastoEntity gasto = GastoMapper.toEntity(request);
        gastoRepository.save(gasto);
        log.info("Gasto registrado: categoría={}, valor={}", request.getCategoria(), request.getValor());

        MovimientoContableEntity movimiento = new MovimientoContableEntity();
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setTipo(TipoMovimientoContable.EGRESO);
        movimiento.setMonto(request.getValor());
        movimiento.setDescripcion("Gasto: " + request.getCategoria());

        movimientoRepository.save(movimiento);
    }

    private void validarGasto(GastoDTO request) {
        if (request == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.GASTO_REQUEST_NULO);
        }
        if (request.getCategoria() == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.CATEGORIA_GASTO_NULA);
        }
        if (request.getValor() == null || request.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.VALOR_GASTO_INVALIDO);
        }
    }
}
