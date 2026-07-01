package com.esercizi.prenotazioniFilm.repositories;

import com.esercizi.prenotazioniFilm.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRepository extends JpaRepository<Cinema, Long> {
}
