package com.prog.negocio.service.impl;

import com.prog.negocio.business.ErrorCodesNegocio;
import com.prog.negocio.dto.ResumenPagoDTO;
import com.prog.negocio.exceptions.BcExceptionFactory;
import com.prog.negocio.service.CierreReporteExcelService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class CierreReporteExcelServiceImpl implements CierreReporteExcelService {

    @Override
    public byte[] generarExcel(
            LocalDate inicio,
            LocalDate fin,
            List<ResumenPagoDTO> detalle,
            BigDecimal totalVentas,
            BigDecimal totalGastos,
            BigDecimal utilidadNeta) {

        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Cierre");

            CellStyle tituloStyle = crearEstiloTitulo(workbook);
            CellStyle headerStyle = crearEstiloHeader(workbook);
            CellStyle monedaStyle = crearEstiloMoneda(workbook);

            sheet.createRow(0).createCell(0).setCellValue("REPORTE DE CIERRE");
            sheet.getRow(0).getCell(0).setCellStyle(tituloStyle);

            sheet.createRow(1).createCell(0).setCellValue("Desde " + inicio + " hasta " + fin);

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

            int resumenRowIndex = rowIdx + 2;
            escribirFilaResumen(sheet, resumenRowIndex,     "TOTAL VENTAS:", totalVentas, monedaStyle);
            escribirFilaResumen(sheet, resumenRowIndex + 1, "TOTAL GASTOS:", totalGastos, monedaStyle);
            escribirFilaResumen(sheet, resumenRowIndex + 2, "UTILIDAD NETA:", utilidadNeta, monedaStyle);

            for (int i = 0; i < 3; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return out.toByteArray();

        } catch (IOException e) {
            throw BcExceptionFactory.create(ErrorCodesNegocio.ERROR_GENERANDO_REPORTE);
        }
    }

    private void escribirFilaResumen(XSSFSheet sheet, int rowIndex, String etiqueta,
                                     BigDecimal valor, CellStyle estilo) {
        Row row = sheet.createRow(rowIndex);
        row.createCell(0).setCellValue(etiqueta);
        Cell cell = row.createCell(1);
        cell.setCellValue(valor.doubleValue());
        cell.setCellStyle(estilo);
    }

    private CellStyle crearEstiloTitulo(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 16);
        style.setFont(font);
        return style;
    }

    private CellStyle crearEstiloHeader(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle crearEstiloMoneda(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        DataFormat format = workbook.createDataFormat();
        style.setDataFormat(format.getFormat("$ #,##0.00"));
        return style;
    }
}
