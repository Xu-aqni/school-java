package com.esercitazione.quizStoria.repositories;

import com.esercitazione.quizStoria.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    List<Evento> findByAnno(int anno);

    List<Evento> findByTitolo(String titolo);

    List<Evento> findByCategoria(String categoria);

    List<Evento> findByCivilta(String civilta);

    List<Evento> findAllByOrderByIdAsc();

    Optional<Evento> findFirstByOrderByAnnoDesc();

    Optional<Evento> findFirstByOrderByAnnoAsc();

    // long countByCategoria(String categoria);

    @Query("SELECT e FROM Evento e WHERE " +
            "(:anno IS NULL OR e.anno = :anno) AND " +
            "(:titolo IS NULL OR :titolo = '' OR LOWER(e.titolo) LIKE LOWER(CONCAT('%', :titolo, '%'))) AND " +
            "(:luogo IS NULL OR :luogo = '' OR LOWER(e.luogo) LIKE LOWER(CONCAT('%', :luogo, '%'))) AND " +
            "(:civilta IS NULL OR :civilta = '' OR LOWER(e.civilta) LIKE LOWER(CONCAT('%', :civilta, '%'))) AND " +
            "(:categoria IS NULL OR :categoria = '' OR LOWER(e.categoria) = LOWER(:categoria))" +
            "ORDER BY e.id ASC")
    List<Evento> searchEvents(
            @Param("anno") Integer anno,
            @Param("titolo") String titolo,
            @Param("luogo") String luogo,
            @Param("civilta") String civilta,
            @Param("categoria") String categoria);
}
