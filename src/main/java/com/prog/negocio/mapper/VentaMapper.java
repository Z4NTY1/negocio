package com.prog.negocio.mapper;

import com.prog.negocio.dto.VentaResponseDTO;
import com.prog.negocio.entity.VentaEntity;

/**
 * Mapper para conversión entre VentaEntity y sus DTOs.
 */
public final class VentaMapper {

    private VentaMapper() {
    }

    /**
     * Convierte un VentaEntity a VentaResponseDTO.
     */
    public static VentaResponseDTO toDTO(VentaEntity entity) {
        return new VentaResponseDTO(
                entity.getId(),
                entity.getTotal(),
                entity.getTipoPago(),
                entity.getFecha()
        );
    }
}
