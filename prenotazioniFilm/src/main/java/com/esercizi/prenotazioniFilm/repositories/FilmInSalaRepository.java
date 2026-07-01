package com.esercizi.prenotazioniFilm.repositories;

import com.esercizi.prenotazioniFilm.entities.FilmInSala;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmInSalaRepository extends JpaRepository<FilmInSala, Long> {
    List<FilmInSala> findByFilmId(Long filmId);
}
