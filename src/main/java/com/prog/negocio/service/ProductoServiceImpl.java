package com.prog.negocio.service;

import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import com.prog.negocio.entity.Producto;
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
        Producto p = new Producto();
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
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto p = repository.findById(id).orElseThrow();

        p.setNombre(dto.getNombre());
        p.setPrecioVenta(dto.getPrecioVenta());
        p.setCosto(dto.getCosto());

        repository.save(p);

        return obtenerPorId(id);
    }

    @Override
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<ProductoResponseDTO> listar() {
        return repository.findAll()
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
        Producto p = repository.findById(id)
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
