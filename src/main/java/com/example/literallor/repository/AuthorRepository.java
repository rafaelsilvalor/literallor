package com.example.literallor.repository;

import com.example.literallor.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /**
     * Busca autores que estavam vivos em um determinado ano.
     * A lógica verifica se o ano fornecido está entre o ano de nascimento e o de falecimento.
     * Trata também os casos em que o autor ainda está vivo (deathYear é nulo).
     */
    @Query("SELECT a FROM Author a WHERE a.birthYear <= :year AND (a.deathYear IS NULL OR a.deathYear >= :year)")
    List<Author> findAuthorsAliveInYear(int year);
}
