package com.eduardo.laboratorio1eduardochacon.controller;

import com.eduardo.laboratorio1eduardochacon.mappers.ProductoMapper;
import com.eduardo.laboratorio1eduardochacon.dto.ProductoDTO;
import com.eduardo.laboratorio1eduardochacon.model.Producto;
import com.eduardo.laboratorio1eduardochacon.service.ProductoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/producto")
@Slf4j

public class ProductoController {


    private final ProductoService productoService;
    private final ProductoMapper productoMapper;

    public ProductoController(ProductoService productoService, ProductoMapper productoMapper) {
        this.productoService = productoService;
        this.productoMapper = productoMapper;
    }

    @PreAuthorize("hasAuthority('producto:leer')")
    @GetMapping("/listar")
    public List<ProductoDTO> findAll() {
        log.info("Listando todos los productos");
        return productoMapper.toDtoList(productoService.findAll());
    }

    @PreAuthorize("hasAuthority('producto:leer')")
    @GetMapping("/listar/{id}")
    public ResponseEntity<ProductoDTO> find(@PathVariable Long id){
        log.info("Buscando producto por id");
        return productoService.findById(id)
                .map(productoMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('producto:escribir')")
    @PostMapping("/crear")
    public ResponseEntity<ProductoDTO> create(@RequestBody ProductoDTO productoDTO) {
        log.info("Creando producto");
        Producto creado = productoService.createProducto(productoMapper.toEntity(productoDTO));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(location).body(productoMapper.toDto(creado));
    }

    @PreAuthorize("hasAuthority('producto:escribir')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<ProductoDTO> update(@PathVariable Long id, @RequestBody ProductoDTO productoDTO) {
        log.info("Actualizando producto con id {}", id);
        return ResponseEntity.of(productoService.updateProducto(id, productoMapper.toEntity(productoDTO)).map(productoMapper::toDto));
    }




    @PreAuthorize("hasAuthority('producto:eliminar')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando producto con id {}", id);
        return productoService.deleteProducto(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
