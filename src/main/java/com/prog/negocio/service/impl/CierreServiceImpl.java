package com.prog.negocio.service.impl;

import com.prog.negocio.dto.CierreResponseDTO;
import com.prog.negocio.dto.ResumenPagoDTO;
import com.prog.negocio.entity.CierreQuincenalEntity;
import com.prog.negocio.repository.CierreQuincenalRepository;
import com.prog.negocio.repository.GastoRepository;
import com.prog.negocio.repository.VentaRepository;
import com.prog.negocio.service.CierreService;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CierreServiceImpl implements CierreService {

    private final VentaRepository ventaRepository;
    private final GastoRepository gastoRepository;
    private final CierreQuincenalRepository cierreQuincenalRepository;

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
            throw new RuntimeException("Ya existe un cierre para ese rango");
        }

        Totales totales = calcularTotales(inicio, fin);

        CierreQuincenalEntity cierre = new CierreQuincenalEntity();
        cierre.setFechaInicio(inicio);
        cierre.setFechaFin(fin);
        cierre.setTotalVentas(totales.totalVentas());
        cierre.setTotalGastos(totales.totalGastos());
        cierre.setUtilidadNeta(totales.utilidad());

        CierreQuincenalEntity guardado = cierreQuincenalRepository.save(cierre);

        return mapToDTO(guardado);
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
                .map(this::mapToDTO)
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

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        List<ResumenPagoDTO> detalle =
                ventaRepository.obtenerResumenPorRango(inicioDateTime, finDateTime);

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Cierre");

            CellStyle tituloStyle = workbook.createCellStyle();
            Font tituloFont = workbook.createFont();
            tituloFont.setBold(true);
            tituloFont.setFontHeightInPoints((short) 16);
            tituloStyle.setFont(tituloFont);

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            CellStyle monedaStyle = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            monedaStyle.setDataFormat(format.getFormat("$ #,##0.00"));

            Row tituloRow = sheet.createRow(0);
            Cell tituloCell = tituloRow.createCell(0);
            tituloCell.setCellValue("REPORTE DE CIERRE");
            tituloCell.setCellStyle(tituloStyle);

            Row rangoRow = sheet.createRow(1);
            rangoRow.createCell(0).setCellValue("Desde " + inicio + " hasta " + fin);

            int startRow = 3;

            Row headerRow = sheet.createRow(startRow);
            String[] columnas = {"Tipo de Pago", "Cantidad Ventas", "Total Ventas"};

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowIdx = startRow + 1;

            for (ResumenPagoDTO resumen : detalle) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(resumen.getTipoPago().name());
                row.createCell(1).setCellValue(resumen.getCantidadVentas());

                Cell moneyCell = row.createCell(2);
                moneyCell.setCellValue(resumen.getTotalDinero().doubleValue());
                moneyCell.setCellStyle(monedaStyle);
            }

            int endRow = rowIdx - 1;
            int resumenRowIndex = endRow + 3;

            Row ventasRow = sheet.createRow(resumenRowIndex);
            ventasRow.createCell(0).setCellValue("TOTAL VENTAS:");
            Cell totalVentasCell = ventasRow.createCell(1);
            totalVentasCell.setCellValue(totales.totalVentas().doubleValue());
            totalVentasCell.setCellStyle(monedaStyle);

            Row gastosRow = sheet.createRow(resumenRowIndex + 1);
            gastosRow.createCell(0).setCellValue("TOTAL GASTOS:");
            Cell totalGastosCell = gastosRow.createCell(1);
            totalGastosCell.setCellValue(totales.totalGastos().doubleValue());
            totalGastosCell.setCellStyle(monedaStyle);

            Row utilidadRow = sheet.createRow(resumenRowIndex + 2);
            utilidadRow.createCell(0).setCellValue("UTILIDAD NETA:");
            Cell utilidadCell = utilidadRow.createCell(1);
            utilidadCell.setCellValue(totales.utilidad().doubleValue());
            utilidadCell.setCellStyle(monedaStyle);

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw new RuntimeException("Error generando reporte Excel", e);
        }
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

        LocalDateTime inicioDateTime = inicio.atStartOfDay();
        LocalDateTime finDateTime = fin.atTime(23, 59, 59);

        BigDecimal totalVentas = ventaRepository.sumTotalBetween(inicioDateTime, finDateTime);
        BigDecimal totalGastos = gastoRepository.sumBetween(inicioDateTime, finDateTime);
        BigDecimal utilidad = totalVentas.subtract(totalGastos);

        return new Totales(totalVentas, totalGastos, utilidad);
    }

    private void validarRango(LocalDate inicio, LocalDate fin) {

        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas no pueden ser nulas");
        }

        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException("La fecha inicio no puede ser mayor que la fecha fin");
        }
    }

    private CierreResponseDTO mapToDTO(CierreQuincenalEntity cierre) {

        return new CierreResponseDTO(
                cierre.getId(),
                cierre.getFechaInicio(),
                cierre.getFechaFin(),
                cierre.getTotalVentas(),
                cierre.getTotalGastos(),
                cierre.getUtilidadNeta()
        );
    }

    private record Totales(
            BigDecimal totalVentas,
            BigDecimal totalGastos,
            BigDecimal utilidad
    ) {}
}
