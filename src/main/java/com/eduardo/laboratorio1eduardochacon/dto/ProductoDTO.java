package com.eduardo.laboratorio1eduardochacon.dto;

import java.math.BigDecimal;


public record ProductoDTO(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        Integer stock,
        CategoriaDTO categoria) {
}
