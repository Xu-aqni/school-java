package com.esercizi.prenotazioniFilm.services;

import com.esercizi.prenotazioniFilm.entities.FilmInSala;
import com.esercizi.prenotazioniFilm.repositories.FilmInSalaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmInSalaService {
    private final FilmInSalaRepository filmInSalaRepository;

    public FilmInSalaService(FilmInSalaRepository filmInSalaRepository) {
        this.filmInSalaRepository = filmInSalaRepository;
    }

    public List<FilmInSala> findAll() {
        return filmInSalaRepository.findAll();
    }

    public List<FilmInSala> findByFilm(Long filmId) {
        return filmInSalaRepository.findByFilmId(filmId);
    }

    public FilmInSala save(FilmInSala fis) {
        return filmInSalaRepository.save(fis);
    }
}
