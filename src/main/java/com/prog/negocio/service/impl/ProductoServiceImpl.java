package com.prog.negocio.service.impl;

import com.prog.negocio.dto.ActualizarProductoDTO;
import com.prog.negocio.dto.ProductoRequestDTO;
import com.prog.negocio.dto.ProductoResponseDTO;
import com.prog.negocio.entity.ProductoEntity;
import com.prog.negocio.repository.ProductoRepository;
import com.prog.negocio.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoResponseDTO crear(ProductoRequestDTO request) {
        ProductoEntity producto = new ProductoEntity();
        producto.setNombre(request.getNombre());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setCosto(request.getCosto());
        producto.setStockActual(request.getStockInicial());
        producto.setActivo(true);

        productoRepository.save(producto);

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecioVenta(),
                producto.getCosto(),
                producto.getStockActual()
        );
    }

    @Override
    public ProductoResponseDTO actualizar(Long id, ActualizarProductoDTO request) {
        ProductoEntity producto = productoRepository.findById(id).orElseThrow();

        producto.setNombre(request.getNombre());
        producto.setPrecioVenta(request.getPrecioVenta());
        producto.setCosto(request.getCosto());

        productoRepository.save(producto);

        return obtenerPorId(id);
    }

    @Override
    public void eliminar(Long id) {

        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        producto.setActivo(false);

        productoRepository.save(producto);
    }

    @Override
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findByActivoTrue()
                .stream()
                .map(producto -> new ProductoResponseDTO(
                        producto.getId(),
                        producto.getNombre(),
                        producto.getPrecioVenta(),
                        producto.getCosto(),
                        producto.getStockActual()))
                .toList();
    }

    @Override
    public ProductoResponseDTO obtenerPorId(Long id) {
        ProductoEntity producto = productoRepository.findById(id)
                .orElseThrow();

        return new ProductoResponseDTO(
                producto.getId(),
                producto.getNombre(),
                producto.getPrecioVenta(),
                producto.getCosto(),
                producto.getStockActual()
        );
    }
}
