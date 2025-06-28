package com.alura.LiterAlura.repository;

import com.alura.LiterAlura.model.CategoriaIdioma;
import com.alura.LiterAlura.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    List<Libro> findByIdioma(CategoriaIdioma idiomaBuscado);
    List<Libro> findByDerechosAutor(boolean copyright);
}
