package com.eduardo.laboratorio1eduardochacon.service;

import com.eduardo.laboratorio1eduardochacon.mappers.UsuarioMapper;
import com.eduardo.laboratorio1eduardochacon.model.Usuario;
import com.eduardo.laboratorio1eduardochacon.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    private final KeycloakAdminService keycloakAdminService;

    public UsuarioService(UsuarioRepository usuarioRepository,
                          UsuarioMapper usuarioMapper,
                          KeycloakAdminService keycloakAdminService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.keycloakAdminService = keycloakAdminService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        log.info("Buscando usuario por ID: {}", id);
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByUsername(String username) {
        log.info("Buscando usuario por username: {}", username);
        return usuarioRepository.findByUsername(username);
    }

    @Transactional
    public Usuario createUsuario(Usuario usuario) {
        if (usuarioRepository.findByUsername(usuario.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
        }

        String passwordPlano = usuario.getPassword();

        // 1) crear la cuenta en Keycloak (de ahí sale el login)
        keycloakAdminService.crearUsuario(usuario.getUsername(), passwordPlano, usuario.getRole());

        // 2) guardar en Postgres con la contraseña encriptada
        usuario.setPassword(passwordEncoder.encode(passwordPlano));
        return usuarioRepository.save(usuario);
    }



}