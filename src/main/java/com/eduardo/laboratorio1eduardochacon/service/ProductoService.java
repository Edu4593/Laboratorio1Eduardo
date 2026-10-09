package com.eduardo.laboratorio1eduardochacon.service;

import com.eduardo.laboratorio1eduardochacon.mappers.ProductoMapper;
import com.eduardo.laboratorio1eduardochacon.model.Categoria;
import com.eduardo.laboratorio1eduardochacon.model.Producto;
import com.eduardo.laboratorio1eduardochacon.repository.CategoriaRepository;
import com.eduardo.laboratorio1eduardochacon.repository.ProductoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductoService {


    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoMapper productoMapper;


    public ProductoService(ProductoRepository productoRepository, CategoriaRepository categoriaRepository, ProductoMapper productoMapper) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoMapper = productoMapper;
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Optional<Producto> findById(Long id
    ) {
        log.info("Buscando productos por ID: ", id);
        return productoRepository.findById(id);
    }



    @Transactional
    public Producto createProducto(Producto producto) {
        if (producto.getCategoria() == null || producto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("El producto debe estar asociado a una categoría válida.");
        }
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(producto.getCategoria().getId());

        if (categoriaOpt.isEmpty()) {
            throw new IllegalArgumentException("Categoría no encontrada.");
        }

        Categoria categoria = categoriaOpt.get();
        producto.setCategoria(categoria);
        return productoRepository.save(producto);
    }



    @Transactional
    public Optional<Producto> updateProducto(Long id, Producto datos) {
        if (datos.getCategoria() == null || datos.getCategoria().getId() == null) {
            throw new IllegalArgumentException("El producto debe estar asociado a una categoría válida.");
        }

        Categoria categoria = categoriaRepository.findById(datos.getCategoria().getId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada."));

        return productoRepository.findById(id).map(producto -> {
            producto.setNombre(datos.getNombre());
            producto.setDescripcion(datos.getDescripcion());
            producto.setPrecio(datos.getPrecio());
            producto.setStock(datos.getStock());
            producto.setCategoria(categoria);
            return productoRepository.save(producto);
        });
    }

    @Transactional
    public boolean deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            return false;
        }
        productoRepository.deleteById(id);
        return true;
    }





}