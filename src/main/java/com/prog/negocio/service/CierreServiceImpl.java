package com.prog.negocio.service;

import com.prog.negocio.dto.CierreResponseDTO;
import com.prog.negocio.entity.CierreQuincenal;
import com.prog.negocio.repository.CierreRepository;
import com.prog.negocio.repository.GastoRepository;
import com.prog.negocio.repository.VentaRepository;
import com.prog.negocio.service.iservice.CierreService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CierreServiceImpl implements CierreService {

    private final VentaRepository ventaRepository;
    private final GastoRepository gastoRepository;
    private final CierreRepository cierreRepository;

    @Override
    public CierreResponseDTO generarCierre(LocalDate inicio, LocalDate fin) {

        BigDecimal totalVentas = ventaRepository.sumTotalBetween(
                inicio.atStartOfDay(),
                fin.atTime(23, 59, 59)
        );

        BigDecimal totalGastos = gastoRepository.sumBetween(inicio, fin);

        BigDecimal utilidad = totalVentas.subtract(totalGastos);

        CierreQuincenal cierre = new CierreQuincenal();
        cierre.setFechaInicio(inicio);
        cierre.setFechaFin(fin);
        cierre.setTotalVentas(totalVentas);
        cierre.setTotalGastos(totalGastos);
        cierre.setUtilidadNeta(utilidad);

        CierreQuincenal guardado = cierreRepository.save(cierre);

        return new CierreResponseDTO(
                guardado.getId(),
                guardado.getFechaInicio(),
                guardado.getFechaFin(),
                totalVentas,
                totalGastos,
                utilidad
        );
    }

    @Override
    public List<CierreResponseDTO> listarCierres() {

        return cierreRepository.findAll()
                .stream()
                .map(c -> new CierreResponseDTO(
                        c.getId(),
                        c.getFechaInicio(),
                        c.getFechaFin(),
                        c.getTotalVentas(),
                        c.getTotalGastos(),
                        c.getUtilidadNeta()
                ))
                .toList();
    }
}
