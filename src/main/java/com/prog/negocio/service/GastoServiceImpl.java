package com.prog.negocio.service;

import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.entity.GastoEntity;
import com.prog.negocio.entity.MovimientoContableEntity;
import com.prog.negocio.enumAtributte.TipoMovimientoContable;
import com.prog.negocio.repository.GastoRepository;
import com.prog.negocio.repository.MovimientoContableRepository;
import com.prog.negocio.service.iservice.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {
    private final GastoRepository gastoRepository;
    private final MovimientoContableRepository movimientoRepository;
    @Override
    public void registrar(GastoDTO dto) {

        GastoEntity gastoEntity = new GastoEntity();
        gastoEntity.setCategoria(dto.getCategoria());
        gastoEntity.setFecha(LocalDateTime.now());
        gastoEntity.setValor(dto.getValor());
        gastoEntity.setDescripcion(dto.getDescripcion());

        gastoRepository.save(gastoEntity);

        MovimientoContableEntity mov = new MovimientoContableEntity();
        mov.setFecha(LocalDateTime.now());
        mov.setTipo(TipoMovimientoContable.EGRESO);
        mov.setMonto(dto.getValor());
        mov.setDescripcion("Gasto: " + dto.getCategoria());

        movimientoRepository.save(mov);
    }
}
