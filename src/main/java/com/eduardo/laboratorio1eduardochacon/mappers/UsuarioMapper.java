package com.eduardo.laboratorio1eduardochacon.mappers;

import com.eduardo.laboratorio1eduardochacon.dto.UsuarioDTO;
import com.eduardo.laboratorio1eduardochacon.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    UsuarioDTO toDto(Usuario usuario);

    List<UsuarioDTO> toDtoList(List<Usuario> usuarios);

    @Mapping(target = "id", ignore = true)
    Usuario toEntity(UsuarioDTO usuarioDTO);
}
