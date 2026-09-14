package com.esercitazione.quizStoria.repositories;

import com.esercitazione.quizStoria.entities.Domanda;
import com.esercitazione.quizStoria.entities.Domanda.QuestionType;
import com.esercitazione.quizStoria.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DomandaRepository extends JpaRepository<Domanda, Long> {

    List<Domanda> findByEvent(Evento event);

    List<Domanda> findByEventId(Long eventoId);

    List<Domanda> findByType(QuestionType type);

    List<Domanda> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM domanda ORDER BY RANDOM() LIMIT :limit", nativeQuery = true)
    List<Domanda> findRandomQuestions(@Param("limit") int limit);

    void deleteByEventId(Long eventoId);

    @Query("SELECT d FROM Domanda d WHERE " +
            "(:type IS NULL OR d.type = :type) AND " +
            "(:contents IS NULL OR :contents = '' OR LOWER(d.contents) LIKE LOWER(CONCAT('%', :contents, '%'))) AND " +
            "(:eventoTitolo IS NULL OR :eventoTitolo = '' OR LOWER(d.event.titolo) LIKE LOWER(CONCAT('%', :eventoTitolo, '%'))) " +
            "ORDER BY d.id ASC")
    List<Domanda> searchDomande(
            @Param("type") QuestionType type,
            @Param("contents") String contents,
            @Param("eventoTitolo") String eventoTitolo);
}
