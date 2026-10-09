package com.eduardo.laboratorio1eduardochacon.service;

import com.eduardo.laboratorio1eduardochacon.mappers.CategoriaMapper;
import com.eduardo.laboratorio1eduardochacon.model.Categoria;
import com.eduardo.laboratorio1eduardochacon.model.Producto;
import com.eduardo.laboratorio1eduardochacon.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Slf4j
@Service
public class CategoriaService {

    private CategoriaRepository categoriaRepository;
    private CategoriaMapper categoriaMapper;

    public CategoriaService(CategoriaRepository categoriaRepository, CategoriaMapper categoriaMapper) {
        this.categoriaRepository = categoriaRepository;
        this.categoriaMapper = categoriaMapper;
    }

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Optional<Categoria> findById(Long id
    ) {
        log.info("Buscando categorías por ID: ", id);
        return categoriaRepository.findById(id);
    }

    @Transactional
    public Categoria createCategoria(Categoria categoria) {
        return categoriaRepository.save(categoria);
    }

    @Transactional
    public Optional<Categoria> updateCategoria(Long id, Categoria datos) {
        Optional<Categoria> categoriaOpt = categoriaRepository.findById(id);

        if (categoriaOpt.isEmpty()) {
            return Optional.empty();
        }

        Categoria categoriaExistente = categoriaOpt.get();
        categoriaExistente.setNombre(datos.getNombre());
        categoriaExistente.setDescripcion(datos.getDescripcion());

        return Optional.of(categoriaRepository.save(categoriaExistente));
    }

    @Transactional
    public boolean deleteCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            return false;
        }
        categoriaRepository.deleteById(id);
        return true;
    }

}
