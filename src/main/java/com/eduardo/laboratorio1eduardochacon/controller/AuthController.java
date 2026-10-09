package com.eduardo.laboratorio1eduardochacon.controller;

import com.eduardo.laboratorio1eduardochacon.dto.LoginRequestDTO;
import com.eduardo.laboratorio1eduardochacon.dto.LoginResponseDTO;
import com.eduardo.laboratorio1eduardochacon.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Login a través de la API. Es público (ver SecurityConfig): no se puede pedir token
 * para obtener un token.
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        return authService.login(request);
    }
}
