package com.eduardo.laboratorio1eduardochacon.repository;

import com.eduardo.laboratorio1eduardochacon.model.Categoria;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    @Override
    @EntityGraph(attributePaths = "productos")
    List<Categoria> findAll();

    @Override
    @EntityGraph(attributePaths = "productos")
    Optional<Categoria> findById(Long id);
}
