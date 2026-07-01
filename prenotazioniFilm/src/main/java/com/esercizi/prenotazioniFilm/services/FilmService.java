package com.esercizi.prenotazioniFilm.services;

import com.esercizi.prenotazioniFilm.entities.Film;
import com.esercizi.prenotazioniFilm.repositories.FilmRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilmService {
    private final FilmRepository filmRepository;

    public FilmService(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    public Film findById(Long id) {
        return filmRepository.findById(id).orElse(null);
    }

    public Film save(Film film) {
        return filmRepository.save(film);
    }
}
