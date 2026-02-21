package com.prog.negocio.mapper;

import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import com.prog.negocio.entity.ProductoEntity;

/**
 * Mapper para conversión entre ProductoEntity y sus DTOs.
 */
public final class ProductoMapper {

    private ProductoMapper() {
    }

    /**
     * Construye un ProductoEntity nuevo a partir de un request de creación.
     * No asigna id (lo genera la BD).
     */
    public static ProductoEntity toEntity(ProductoRequestDTO dto) {
        ProductoEntity entity = new ProductoEntity();
        entity.setNombre(dto.getNombre().trim());
        entity.setPrecioVenta(dto.getPrecioVenta());
        entity.setCosto(dto.getCosto());
        entity.setStockActual(dto.getStockInicial());
        entity.setActivo(true);
        return entity;
    }

    /**
     * Convierte un ProductoEntity a ProductoResponseDTO.
     */
    public static ProductoResponseDTO toDTO(ProductoEntity entity) {
        return new ProductoResponseDTO(
                entity.getId(),
                entity.getNombre(),
                entity.getPrecioVenta(),
                entity.getCosto(),
                entity.getStockActual()
        );
    }
}
