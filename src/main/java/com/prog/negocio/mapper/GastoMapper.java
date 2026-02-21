package com.prog.negocio.mapper;

import com.prog.negocio.dto.GastoDTO;
import com.prog.negocio.entity.GastoEntity;

import java.time.LocalDateTime;

/**
 * Mapper para conversión entre GastoDTO y GastoEntity.
 */
public final class GastoMapper {

    private GastoMapper() {
    }

    /**
     * Construye un GastoEntity a partir de un GastoDTO.
     * Asigna la fecha actual como momento del registro.
     */
    public static GastoEntity toEntity(GastoDTO dto) {
        GastoEntity entity = new GastoEntity();
        entity.setCategoria(dto.getCategoria());
        entity.setFecha(LocalDateTime.now());
        entity.setValor(dto.getValor());
        entity.setDescripcion(dto.getDescripcion() != null ? dto.getDescripcion().trim() : "");
        return entity;
    }
}
