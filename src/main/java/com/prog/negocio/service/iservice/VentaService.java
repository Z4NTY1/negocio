package com.prog.negocio.service.iservice;

import com.prog.negocio.dto.ResumenGeneralDTO;
import com.prog.negocio.dto.VentaRequestDTO;
import com.prog.negocio.dto.VentaResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface VentaService {

    VentaResponseDTO registrarVenta(VentaRequestDTO dto);

    List<VentaResponseDTO> listarPorFecha(LocalDate fecha);

    ResumenGeneralDTO resumenDiario(LocalDate fecha);

    ResumenGeneralDTO resumenQuincenal(LocalDate fechaReferencia);
}
