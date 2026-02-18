package com.prog.negocio.service.iservice;

import com.prog.negocio.dto.CierreResponseDTO;
import java.time.LocalDate;
import java.util.List;

public interface CierreService {

    CierreResponseDTO generarCierre(LocalDate inicio, LocalDate fin);

    List<CierreResponseDTO> listarCierres();
}
