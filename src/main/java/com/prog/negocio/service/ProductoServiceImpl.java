package com.prog.negocio.service;

import com.prog.negocio.dto.ActualizarProductoDTO;
import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import com.prog.negocio.entity.ProductoEntity;
import com.prog.negocio.repository.ProductoRepository;
import com.prog.negocio.service.iservice.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {
    private final ProductoRepository repository;

    @Override
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        ProductoEntity p = new ProductoEntity();
        p.setNombre(dto.getNombre());
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setCosto(dto.getCosto());
        p.setStockActual(dto.getStockInicial());
        p.setActivo(true);

        repository.save(p);

        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getPrecioVenta(),
                p.getCosto(),
                p.getStockActual()
        );
    }

    @Override
    public ProductoResponseDTO actualizar(Long id, ActualizarProductoDTO dto) {
        ProductoEntity p = repository.findById(id).orElseThrow();

        p.setNombre(dto.getNombre());
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setCosto(dto.getCosto());

        repository.save(p);

        return obtenerPorId(id);
    }

    @Override
    public void eliminar(Long id) {

        ProductoEntity productoEntity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        productoEntity.setActivo(false);

        repository.save(productoEntity);
    }

    @Override
    public List<ProductoResponseDTO> listar() {
        return repository.findByActivoTrue()
                .stream()
                .map(p -> new ProductoResponseDTO(
                        p.getId(),
                        p.getNombre(),
                        p.getPrecioVenta(),
                        p.getCosto(),
                        p.getStockActual()))
                .toList();
    }

    @Override
    public ProductoResponseDTO obtenerPorId(Long id) {
        ProductoEntity p = repository.findById(id)
                .orElseThrow();

        return new ProductoResponseDTO(
                p.getId(),
                p.getNombre(),
                p.getPrecioVenta(),
                p.getCosto(),
                p.getStockActual()
        );
    }
}
