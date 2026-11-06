package com.eduardo.laboratorio1eduardochacon.controller;

import com.eduardo.laboratorio1eduardochacon.dto.UsuarioDTO;
import com.eduardo.laboratorio1eduardochacon.mappers.UsuarioMapper;
import com.eduardo.laboratorio1eduardochacon.model.Usuario;
import com.eduardo.laboratorio1eduardochacon.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final UsuarioMapper usuarioMapper;

    public UsuarioController(UsuarioService usuarioService, UsuarioMapper usuarioMapper) {
        this.usuarioService = usuarioService;
        this.usuarioMapper = usuarioMapper;

    }
    @PreAuthorize("hasAuthority('usuario:leer')")
    @GetMapping("/listar")
    public List<UsuarioDTO> findAll() {
        log.info("Authorities: {}", SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        log.info("Listando todos los usuarios");
        return usuarioMapper.toDtoList(usuarioService.findAll());
    }

    @PreAuthorize("hasAuthority('usuario:leer')")
    @GetMapping("/listar/{id}")
    public ResponseEntity<UsuarioDTO> find(@PathVariable Long id){
        log.info("Buscando usuario por id {}", id);
        return usuarioService.findById(id)
                .map(usuarioMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @PostMapping("/registro")
    public ResponseEntity<UsuarioDTO> registro(@RequestBody UsuarioDTO dto) {
        Usuario creado = usuarioService.createUsuario(usuarioMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioMapper.toDto(creado));
    }


}
