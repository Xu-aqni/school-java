package com.esercizi.prenotazioniFilm.repositories;

import com.esercizi.prenotazioniFilm.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
}
