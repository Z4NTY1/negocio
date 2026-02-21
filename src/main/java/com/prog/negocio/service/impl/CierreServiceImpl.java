package com.prog.negocio.service.impl;

import com.prog.negocio.business.ErrorCodesNegocio;
import com.prog.negocio.dto.CierreResponseDTO;
import com.prog.negocio.dto.ResumenPagoDTO;
import com.prog.negocio.entity.CierreQuincenalEntity;
import com.prog.negocio.exceptions.BcExceptionFactory;
import com.prog.negocio.mapper.CierreMapper;
import com.prog.negocio.repository.CierreQuincenalRepository;
import com.prog.negocio.repository.GastoRepository;
import com.prog.negocio.repository.VentaRepository;
import com.prog.negocio.service.CierreReporteExcelService;
import com.prog.negocio.service.CierreService;
import com.prog.negocio.util.FechaUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CierreServiceImpl implements CierreService {

    private static final Logger log = LoggerFactory.getLogger(CierreServiceImpl.class);

    private final VentaRepository ventaRepository;
    private final GastoRepository gastoRepository;
    private final CierreQuincenalRepository cierreQuincenalRepository;
    private final CierreReporteExcelService reporteExcelService;

    /**
     * Genera y persiste un cierre para el rango de fechas: totales de ventas, gastos y utilidad.
     *
     * @param inicio fecha inicial del rango (inclusive)
     * @param fin    fecha final del rango (inclusive)
     * @return DTO del cierre guardado
     */
    @Override
    @Transactional
    public CierreResponseDTO generarCierre(LocalDate inicio, LocalDate fin) {

        validarRango(inicio, fin);

        if (cierreQuincenalRepository.findByFechaInicioAndFechaFin(inicio, fin).isPresent()) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.RANGO_CIERRE_EXISTE);
        }

        Totales totales = calcularTotales(inicio, fin);

        CierreQuincenalEntity cierre = new CierreQuincenalEntity();
        cierre.setFechaInicio(inicio);
        cierre.setFechaFin(fin);
        cierre.setTotalVentas(totales.totalVentas());
        cierre.setTotalGastos(totales.totalGastos());
        cierre.setUtilidadNeta(totales.utilidad());

        CierreQuincenalEntity guardado = cierreQuincenalRepository.save(cierre);
        log.info("Cierre generado: id={}, rango {} a {}", guardado.getId(), inicio, fin);

        return CierreMapper.toDTO(guardado);
    }

    /**
     * Lista todos los cierres almacenados.
     *
     * @return lista de cierres con totales
     */
    @Override
    @Transactional(readOnly = true)
    public List<CierreResponseDTO> listarCierres() {
        return cierreQuincenalRepository.findAll()
                .stream()
                .map(CierreMapper::toDTO)
                .toList();
    }

    /**
     * Genera un Excel con detalle de ventas por tipo de pago y resumen (ventas, gastos, utilidad).
     *
     * @param inicio fecha inicial del rango
     * @param fin    fecha final del rango
     * @return bytes del archivo .xlsx
     */
    @Override
    @Transactional(readOnly = true)
    public byte[] generarReportePorRango(LocalDate inicio, LocalDate fin) {

        validarRango(inicio, fin);

        Totales totales = calcularTotales(inicio, fin);

        LocalDateTime inicioDateTime = FechaUtil.inicioDelDia(inicio);
        LocalDateTime finDateTime = FechaUtil.finDelDia(fin);

        List<ResumenPagoDTO> detalle =
                ventaRepository.obtenerResumenPorRango(inicioDateTime, finDateTime);

        return reporteExcelService.generarExcel(
                inicio, fin, detalle,
                totales.totalVentas(), totales.totalGastos(), totales.utilidad()
        );
    }

    /**
     * Calcula ventas, gastos y utilidad para el rango sin persistir cierre.
     *
     * @param inicio fecha inicial
     * @param fin    fecha final
     * @return DTO con totales (id null)
     */
    @Override
    @Transactional(readOnly = true)
    public CierreResponseDTO calcularResumen(LocalDate inicio, LocalDate fin) {

        validarRango(inicio, fin);

        Totales totales = calcularTotales(inicio, fin);

        return new CierreResponseDTO(
                null,
                inicio,
                fin,
                totales.totalVentas(),
                totales.totalGastos(),
                totales.utilidad()
        );
    }

    private Totales calcularTotales(LocalDate inicio, LocalDate fin) {
        LocalDateTime inicioDateTime = FechaUtil.inicioDelDia(inicio);
        LocalDateTime finDateTime = FechaUtil.finDelDia(fin);

        BigDecimal totalVentas = ventaRepository.sumTotalBetween(inicioDateTime, finDateTime);
        BigDecimal totalGastos = gastoRepository.sumBetween(inicioDateTime, finDateTime);
        BigDecimal utilidad = totalVentas.subtract(totalGastos);

        return new Totales(totalVentas, totalGastos, utilidad);
    }

    private void validarRango(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.FECHAS_NULAS);
        }
        if (inicio.isAfter(fin)) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.FECHA_INICIO_MAYOR);
        }
    }

    private record Totales(
            BigDecimal totalVentas,
            BigDecimal totalGastos,
            BigDecimal utilidad
    ) {}
}
