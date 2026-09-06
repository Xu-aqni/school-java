package com.esercitazione.quizStoria.repositories;

import com.esercitazione.quizStoria.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByAnno(int anno);

    List<Evento> findByTitolo(String titolo);

    List<Evento> findByCategoria(String categoria);

    List<Evento> findByCivilta(String civilta);

    Optional<Evento> findFirstByOrderByAnnoDesc();

    Optional<Evento> findFirstByOrderByAnnoAsc();

    long countByCategoria(String categoria);
}
