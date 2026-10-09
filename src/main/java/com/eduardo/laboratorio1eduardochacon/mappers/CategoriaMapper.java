package com.eduardo.laboratorio1eduardochacon.mappers;

import com.eduardo.laboratorio1eduardochacon.dto.CategoriaDTO;
import com.eduardo.laboratorio1eduardochacon.model.Categoria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper {

    CategoriaDTO toDto(Categoria categoria);

    List<CategoriaDTO> toDtoList(List<Categoria> categorias);

    // Los productos de una categoría se asignan al asociar el producto, no desde el body de la categoría
    @Mapping(target = "productos", ignore = true)
    Categoria toEntity(CategoriaDTO categoriaDTO);
}
