package com.eduardo.laboratorio1eduardochacon.controller;

import com.eduardo.laboratorio1eduardochacon.dto.CategoriaDTO;
import com.eduardo.laboratorio1eduardochacon.mappers.CategoriaMapper;
import com.eduardo.laboratorio1eduardochacon.model.Categoria;
import com.eduardo.laboratorio1eduardochacon.service.CategoriaService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;
import java.net.URI;
import java.util.List;


@Slf4j
@RestController
@RequestMapping("/categoria")

public class CategoriaController {
    private final CategoriaService categoriaService;
    private final CategoriaMapper categoriaMapper;

    public CategoriaController(CategoriaService categoriaService, CategoriaMapper categoriaMapper) {
        this.categoriaService = categoriaService;
        this.categoriaMapper = categoriaMapper;
    }

    @PreAuthorize("hasAuthority('categoria:leer')")
    @GetMapping("/listar")
    public List<CategoriaDTO> findAll() {
        log.info("Listando todas las categorias");
        return categoriaMapper.toDtoList(categoriaService.findAll());
    }

    @PreAuthorize("hasAuthority('categoria:leer')")
    @GetMapping("/listar/{id}")
    public ResponseEntity<CategoriaDTO> find(@PathVariable Long id){
        log.info("Buscando categoria por id");
        return categoriaService.findById(id)
                .map(categoriaMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasAuthority('categoria:escribir')")
    @PostMapping("/crear")
    public ResponseEntity<CategoriaDTO> create(@RequestBody CategoriaDTO categoriaDTO) {
        log.info("Creando categoria");
        Categoria creado = categoriaService.createCategoria(categoriaMapper.toEntity(categoriaDTO));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(creado.getId())
                .toUri();
        return ResponseEntity.created(location).body(categoriaMapper.toDto(creado));
    }

    @PreAuthorize("hasAuthority('categoria:escribir')")
    @PutMapping("/actualizar/{id}")
    public ResponseEntity<CategoriaDTO> update(@PathVariable Long id, @RequestBody CategoriaDTO categoriaDTO) {
        log.info("Actualizando categoria con id {}", id);
        return ResponseEntity.of(categoriaService.updateCategoria(id, categoriaMapper.toEntity(categoriaDTO)).map(categoriaMapper::toDto));
    }



    @PreAuthorize("hasAuthority('categoria:eliminar')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminando categoria con id {}", id);
        return categoriaService.deleteCategoria(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

}
