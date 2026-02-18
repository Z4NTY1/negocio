package com.prog.negocio.service;

import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.entity.Gasto;
import com.prog.negocio.entity.MovimientoContable;
import com.prog.negocio.enumAtributte.TipoMovimientoContable;
import com.prog.negocio.repository.GastoRepository;
import com.prog.negocio.repository.MovimientoContableRepository;
import com.prog.negocio.service.iservice.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {
    private final GastoRepository gastoRepository;
    private final MovimientoContableRepository movimientoRepository;
    @Override
    public void registrar(GastoDTO dto) {

        Gasto gasto = new Gasto();
        gasto.setCategoria(dto.getCategoria());
        gasto.setFecha(LocalDate.now());
        gasto.setValor(dto.getValor());
        gasto.setDescripcion(dto.getDescripcion());

        gastoRepository.save(gasto);

        MovimientoContable mov = new MovimientoContable();
        mov.setFecha(LocalDateTime.now());
        mov.setTipo(TipoMovimientoContable.EGRESO);
        mov.setMonto(dto.getValor());
        mov.setDescripcion("Gasto: " + dto.getCategoria());

        movimientoRepository.save(mov);
    }
}
