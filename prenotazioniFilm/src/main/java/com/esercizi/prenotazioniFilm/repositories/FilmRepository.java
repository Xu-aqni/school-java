package com.esercizi.prenotazioniFilm.repositories;

import com.esercizi.prenotazioniFilm.entities.Film;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FilmRepository extends JpaRepository<Film, Long> {
}