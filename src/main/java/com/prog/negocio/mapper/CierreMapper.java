package com.prog.negocio.mapper;

import com.prog.negocio.dto.CierreResponseDTO;
import com.prog.negocio.entity.CierreQuincenalEntity;

/**
 * Mapper para conversión entre CierreQuincenalEntity y CierreResponseDTO.
 */
public final class CierreMapper {

    private CierreMapper() {
    }

    /**
     * Convierte un CierreQuincenalEntity a CierreResponseDTO.
     */
    public static CierreResponseDTO toDTO(CierreQuincenalEntity entity) {
        return new CierreResponseDTO(
                entity.getId(),
                entity.getFechaInicio(),
                entity.getFechaFin(),
                entity.getTotalVentas(),
                entity.getTotalGastos(),
                entity.getUtilidadNeta()
        );
    }
}
