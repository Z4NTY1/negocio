package com.prog.negocio.service.iservice;

import com.prog.negocio.dto.ActualizarProductoDTO;
import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import java.util.List;

public interface ProductoService {
    ProductoResponseDTO crear(ProductoRequestDTO dto);

    ProductoResponseDTO actualizar(Long id, ActualizarProductoDTO dto);

    void eliminar(Long id);

    List<ProductoResponseDTO> listar();

    ProductoResponseDTO obtenerPorId(Long id);
}
