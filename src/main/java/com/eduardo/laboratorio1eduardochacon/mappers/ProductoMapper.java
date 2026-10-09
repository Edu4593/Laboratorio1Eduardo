package com.eduardo.laboratorio1eduardochacon.mappers;

import com.eduardo.laboratorio1eduardochacon.dto.ProductoDTO;
import com.eduardo.laboratorio1eduardochacon.model.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = CategoriaMapper.class)
public interface ProductoMapper {

    ProductoDTO toDto(Producto producto);

    List<ProductoDTO> toDtoList(List<Producto> productos);

    Producto toEntity(ProductoDTO productoDTO);
}
